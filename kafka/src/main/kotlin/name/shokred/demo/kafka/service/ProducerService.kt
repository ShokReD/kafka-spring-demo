package name.shokred.demo.kafka.service

import name.shokred.demo.kafka.dto.Pair

interface ProducerService {
    fun produce(key: String?, value: Pair<String, String>?, topic: String?)
}