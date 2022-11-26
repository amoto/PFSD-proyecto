package infraestructure.messaging.producer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.typesafe.scalalogging.Logger
import infraestructure.messaging.Message
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer

import java.util.{Date, Properties, UUID}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


object Producer extends AutoCloseable {
  val LOGGER: Logger = Logger("TransactionsProducer")
  val objectMapper = new ObjectMapper()
  objectMapper.registerModule(DefaultScalaModule)

  val kafkaProducerProps: Properties = {
    val props = new Properties()
    val bootstrapServers = sys.env.getOrElse("KAFKA_BOOTSTRAP_SERVERS", "localhost:9093")
    LOGGER.info(bootstrapServers)
    props.put("bootstrap.servers", bootstrapServers)
    props.put("key.serializer", classOf[StringSerializer].getName)
    props.put("value.serializer", classOf[StringSerializer].getName)
    props
  }

  val producer = new KafkaProducer[String, String](kafkaProducerProps)

  def produce(topic: String, key: String, value: Any, ID: Option[String] = None): Unit = {
    val id = ID.getOrElse(UUID.randomUUID().toString)
    val message = Message(key, objectMapper.writeValueAsString(value), new Date().getTime, id)
    LOGGER.info(s"[$id] Sending message $message to topic $topic")
    val messageSent = producer.send(new ProducerRecord[String, String](topic, message.key, objectMapper.writeValueAsString(message)))
    Future {
      LOGGER.info(s"[$id] Message Sent - ${messageSent.get()}")
    }
  }

  def close(): Unit = {
    producer.close()
  }
}
