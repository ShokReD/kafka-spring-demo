package name.shokred.demo.kafka.service.impl

import name.shokred.demo.kafka.dto.Pair
import name.shokred.demo.kafka.service.ConsumerService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service

@Service
class ConsumerServiceImpl : ConsumerService {
    @KafkaListener(topics = ["\${kafka.output-topicname}"])
    override fun consume(@Payload message: Pair<String, String>, @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) key: String): String {
        return "Received message = $message with key = $key"
    }
}