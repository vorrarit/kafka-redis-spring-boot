package th.co.bitfactory.testkafka.messageProducer

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class MessageProducerApplication

fun main(args: Array<String>) {
	runApplication<MessageProducerApplication>(*args)
}

@Configuration
class KafkaConfig {
	@Bean
	fun producerFactory() = DefaultKafkaProducerFactory<Int, String>(producerConfigs())

	@Bean
	fun producerConfigs() = mapOf(
			ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
			ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
			ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java
	)

	@Bean
	fun kafkaTemplate() = KafkaTemplate<Int, String>(producerFactory())
}

@RestController
@RequestMapping("message-producer/api/v1")
class MessageProducerController {

	@Autowired
	private lateinit var template: KafkaTemplate<Int, String>

	@GetMapping("publish/{message}")
	fun publish(@PathVariable message: String) {
		template.send("m1", message)
		template.flush()
	}
}
