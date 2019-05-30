package name.shokred.demo.kafka.configuration

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonSerializer

@Configuration
@EnableConfigurationProperties(KafkaProperties::class)
class ProducerConfiguration {

    @Autowired
    lateinit var kafkaProperties: KafkaProperties

    @Bean
    fun <T> producerFactory(): ProducerFactory<String, T> {
        val props = mapOf(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaProperties.bootstrapServer,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java
        )

        return DefaultKafkaProducerFactory(props)
    }

    @Bean
    fun <T> kafkaTemplate(producerFactory: ProducerFactory<String, T>): KafkaTemplate<String, T> {
        return KafkaTemplate(producerFactory).apply {
            defaultTopic = kafkaProperties.outputTopicName
        }
    }
}