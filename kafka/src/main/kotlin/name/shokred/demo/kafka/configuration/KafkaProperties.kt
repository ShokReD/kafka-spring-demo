package name.shokred.demo.kafka.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "kafka")
class KafkaProperties {
    var bootstrapServer = "localhost:9092"
    var groupIdConfig = "kafka-demo"
    var outputTopicName = "output-topic"
    var inputTopicName = "input-topic"
    var partitionCount = 6
    var replicationFactor : Short = 1
}