package name.shokred.demo.kafka.configuration

import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.KafkaAdmin

@Configuration
@EnableConfigurationProperties(KafkaProperties::class)
class TopicConfig(@Autowired val kafkaProperties: KafkaProperties) {

    @Bean
    fun kafkaAdmin(): KafkaAdmin {
        val map = mapOf(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaProperties.bootstrapServer)

        return KafkaAdmin(map)
    }

    @Bean
    fun outputTopic(): NewTopic {
        return NewTopic(
                kafkaProperties.outputTopicName,
                kafkaProperties.partitionCount,
                kafkaProperties.replicationFactor
        )
    }
}