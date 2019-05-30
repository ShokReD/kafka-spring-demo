package name.shokred.demo.kafka.controller

import name.shokred.demo.kafka.service.ProducerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import name.shokred.demo.kafka.dto.Pair

@RestController
class MainController(@Autowired val producerService: ProducerService) {

    @PostMapping("/produce")
    fun produce(
            @RequestParam key: String?,
            @RequestBody value: Pair<String, String>?,
            @RequestParam topic: String?
    ): ResponseEntity<Pair<String, String>> {
        producerService.produce(key, value, topic)

        return ResponseEntity.accepted().body(value)
    }
}