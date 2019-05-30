package name.shokred.demo.kafka.service.impl

import name.shokred.demo.kafka.service.ProducerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import name.shokred.demo.kafka.dto.Pair as myPair

@Service
class ProducerServiceImpl : ProducerService {
    @Autowired
    lateinit var kafkaTemplate: KafkaTemplate<String, myPair<String, String>>

    override fun produce(key: String?, value: name.shokred.demo.kafka.dto.Pair<String, String>?, topic: String?) {
        if (topic == null) {
            kafkaTemplate.sendDefault(getKey(key), getValue(value))
        } else {
            kafkaTemplate.send(topic, getKey(key), getValue(value))
        }
    }

    fun getKey(key: String?): String {
        return key ?: "key"
    }

    fun getValue(value: myPair<String, String>?): myPair<String, String> {
        return value ?: myPair("first", "second")
    }
}