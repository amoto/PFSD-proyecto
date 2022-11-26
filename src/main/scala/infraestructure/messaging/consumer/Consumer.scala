package infraestructure.messaging.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.typesafe.scalalogging.Logger
import domain.entities.ProductEventTypeReference
import infraestructure.messaging.MessageTypeReference
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.serialization.Serdes._

import java.util.Properties

object Consumer extends App {
  val objectMapper = new ObjectMapper()
  objectMapper.registerModule(DefaultScalaModule)
  val LOGGER = Logger("EventsConsumer")
  LOGGER.info("Starting EventsConsumer")

  val kafkaStreamProps: Properties = {
    val props = new Properties()
    props.put("bootstrap.servers", s"${sys.env.getOrElse("KAFKA_BOOTSTRAP_SERVERS", "localhost:9093")}")
    props.put("application.id", "events-consumer")
    props
  }

  val streams = new KafkaStreams(streamTopology, kafkaStreamProps)
  Runtime.getRuntime.addShutdownHook(new Thread(() => streams.close()))
  streams.start()

  def streamTopology = {
    val streamsBuilder = new StreamsBuilder()
    streamsBuilder
      .stream[String, String]("ADD_TO_CART")
      .foreach(
        (key, value) => {
          LOGGER.info(s"Read message with key: $key and value $value")
          var ID: Option[String] = None
          try {
            val message = objectMapper.readValue(value, MessageTypeReference)
            ID = Some(message.ID)
            val deserializedEvent = objectMapper.readValue(message.value, ProductEventTypeReference)
            LOGGER.info(s"message deserialized $deserializedEvent")
          } catch {
            case e: Exception => LOGGER.error(s"Error deserializing message with id $ID and key $key. Exception: $e", e)
          }
        }
      )
    streamsBuilder.build()
  }

}
