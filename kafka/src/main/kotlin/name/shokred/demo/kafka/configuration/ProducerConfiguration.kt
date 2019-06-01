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
        /* here we can configure producer through spring.kafka.producer.{ProducerConfig.properties}
         * for example "spring.kafka.producer.bootstrap.servers"
         * in application.yaml this config would look like:
         *
         * spring:
         *   kafka:
         *     producer:
         *       bootstrap.servers: localhost:9092
         *       ker.serializer: org.apache.kafka.common.serialization.StringSerializer
         *       value.serializer: org.springframework.kafka.support.serializer.JsonSerializer
        */
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