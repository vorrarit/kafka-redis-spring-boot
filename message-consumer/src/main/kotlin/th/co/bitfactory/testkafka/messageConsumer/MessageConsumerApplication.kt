package th.co.bitfactory.testkafka.messageConsumer

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.repository.CrudRepository
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.Serializable
import java.util.logging.Logger

@SpringBootApplication
@EnableCaching
class MessageConsumerApplication

fun main(args: Array<String>) {
	runApplication<MessageConsumerApplication>(*args)
}

@Service
class Consumer(@Autowired val simpleMessageRepository: SimpleMessageRepository) {
	companion object {
		val log = Logger.getLogger(this::class.java.simpleName)
	}

	@KafkaListener(topics = ["m"], groupId = "1")
	@CacheEvict(value = ["simpleMessageGetAll"], allEntries=true)
	fun process(message: String) {
		log.info("receiving message " + message)
		simpleMessageRepository.save(SimpleMessage(null, message))
	}
}

@RestController
@RequestMapping("/api/v1/simple")
class SimpleMessageController(val simpleMessageRepository: SimpleMessageRepository) {

	companion object {
		val log = Logger.getLogger(this::class.java.simpleName)
	}

	@GetMapping()
	@Cacheable("simpleMessageGetAll")
	fun getAll(): List<SimpleMessage> {
		log.info("query SimpleMessage")
		return simpleMessageRepository.findAll().filterNotNull()
	}

}
@Configuration
@EnableKafka
class KafkaConfig {

	@Bean
	fun kafkaListenerContainerFactory() = ConcurrentKafkaListenerContainerFactory<String, String>().apply { consumerFactory = consumerFactory() }

	@Bean
	fun consumerFactory() = DefaultKafkaConsumerFactory<String, String>(consumerConfigs())

	@Bean
	fun consumerConfigs() = mapOf(
			ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
			ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
			ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java
	)
}

@Document
data class SimpleMessage(@Id var id: String?, var message: String): Serializable

interface SimpleMessageRepository: CrudRepository<SimpleMessage, String>

