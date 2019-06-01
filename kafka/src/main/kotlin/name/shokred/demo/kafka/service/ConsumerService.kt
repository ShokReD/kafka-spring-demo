package name.shokred.demo.kafka.service

import name.shokred.demo.kafka.dto.Pair

interface ConsumerService {
    fun consume(message: Pair<String, String>, key: String): String
}