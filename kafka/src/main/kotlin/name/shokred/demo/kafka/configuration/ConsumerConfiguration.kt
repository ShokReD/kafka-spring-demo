package name.shokred.demo.kafka.configuration

import name.shokred.demo.kafka.dto.Pair
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer

@Configuration
@EnableConfigurationProperties(KafkaProperties::class)
class ConsumerConfiguration(@Autowired val kafkaProperties: KafkaProperties) {

    @Bean
    fun consumerFactory(): ConsumerFactory<String, Pair<String, String>> {
        val jsonDeserializer = JsonDeserializer<Pair<String, String>>().apply {
            addTrustedPackages("name.shokred.demo.kafka.dto")
        }

        val props = mapOf(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaProperties.bootstrapServer,
                ConsumerConfig.GROUP_ID_CONFIG to kafkaProperties.groupIdConfig
        )

        return DefaultKafkaConsumerFactory<String, Pair<String, String>>(props, StringDeserializer(), jsonDeserializer)
    }

    @Bean
    fun kafkaListenerContainerFactory(consumerFactory: ConsumerFactory<String, Pair<String, String>>)
            : ConcurrentKafkaListenerContainerFactory<String, Pair<String, String>> {
        return ConcurrentKafkaListenerContainerFactory<String, Pair<String, String>>().also {
            it.consumerFactory = consumerFactory
        }
    }
}