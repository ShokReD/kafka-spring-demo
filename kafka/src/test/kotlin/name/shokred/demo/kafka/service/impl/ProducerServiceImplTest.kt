package name.shokred.demo.kafka.service.impl

import name.shokred.demo.kafka.KafkaApplication
import name.shokred.demo.kafka.configuration.KafkaProperties
import name.shokred.demo.kafka.dto.Pair
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.kafka.test.utils.KafkaTestUtils
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@EmbeddedKafka
@SpringBootTest(classes = [KafkaApplication::class])
@EnableConfigurationProperties(KafkaProperties::class)
class ProducerServiceImplTest {

    @Autowired
    private lateinit var embeddedKafkaBroker: EmbeddedKafkaBroker

    @Autowired
    private lateinit var kafkaProperties: KafkaProperties

    private val producerServiceImpl = ProducerServiceImpl()
    private lateinit var kafkaTemplate: KafkaTemplate<String, Pair<String, String>>
    private lateinit var consumer: Consumer<String, Pair<String, String>>

    @Before
    fun init() {
        initProducer()
        initConsumer()
    }

    private fun initProducer() {
        kafkaTemplate = KafkaTemplate(
                DefaultKafkaProducerFactory<String, Pair<String, String>>(
                        mapOf(
                                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to embeddedKafkaBroker.brokerAddresses.joinToString(separator = ",") {
                                    "${it.host}:${it.port}"
                                },
                                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
                                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java
                        )
                )
        ).apply {
            defaultTopic = kafkaProperties.outputTopicName
        }

        producerServiceImpl.kafkaTemplate = kafkaTemplate
    }

    private fun initConsumer() {
        consumer = DefaultKafkaConsumerFactory(
                mapOf(
                        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to embeddedKafkaBroker.brokerAddresses.joinToString(separator = ",") {
                            "${it.host}:${it.port}"
                        },
                        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
                        ConsumerConfig.GROUP_ID_CONFIG to kafkaProperties.groupIdConfig
                ),
                StringDeserializer(),
                JsonDeserializer<Pair<String, String>>(Pair::class.java)
        )
                .createConsumer().apply {
                    subscribe(listOf(kafkaProperties.outputTopicName, TOPIC_NAME))
                }
    }

    @Test
    fun produce() {
        producerServiceImpl.produce("123", Pair("456", "789"), TOPIC_NAME)

        val singleRecord = KafkaTestUtils.getSingleRecord(consumer, TOPIC_NAME, 1000)

        Assert.assertEquals("123", singleRecord.key())
        Assert.assertEquals(Pair("456", "789"), singleRecord.value())
    }

    @Test
    fun produceDefault() {
        producerServiceImpl.produce("123", Pair("456", "789"), null)

        val singleRecord = KafkaTestUtils.getSingleRecord(consumer, kafkaProperties.outputTopicName, 1000)

        Assert.assertEquals("123", singleRecord.key())
        Assert.assertEquals(Pair("456", "789"), singleRecord.value())
    }

    @Test
    fun getKeyIfNull() {
        Assert.assertEquals("key", producerServiceImpl.getKey(null))
    }

    @Test
    fun getKey() {
        Assert.assertEquals("key1", producerServiceImpl.getKey("key1"))
    }

    @Test
    fun getValueIfNull() {
        Assert.assertEquals(Pair("first", "second"), producerServiceImpl.getValue(null))
    }

    @Test
    fun getValue() {
        Assert.assertEquals(Pair("1", "2"), producerServiceImpl.getValue(Pair("1", "2")))
    }

    @After
    fun tearDown() {
        consumer.close()
    }

    companion object {

        private const val TOPIC_NAME = "topic"
    }
}