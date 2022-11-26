package infraestructure.messaging.consumer.commands

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.typesafe.scalalogging.Logger
import domain.commands.SaveEventsCommand
import domain.entities.EventType.{AddToCart, BackProduct, BackStore, OrderPlaced, SelectProduct, SelectStore}
import domain.entities._
import infraestructure.Injector
import infraestructure.messaging.MessageTypeReference
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.serialization.Serdes._

import java.util.Properties

object SaveEventsCommandRepository extends App {

  private val objectMapper = new ObjectMapper()
  objectMapper.registerModule(DefaultScalaModule)
  private val LOGGER = Logger("SaveEventsCommandRepository")
  LOGGER.info("Starting SaveEventsCommandRepository")

  val kafkaStreamProps: Properties = {
    val props = new Properties()
    props.put("bootstrap.servers", s"${sys.env.getOrElse("KAFKA_BOOTSTRAP_SERVERS", "localhost:9093")}")
    props.put("application.id", "save-events-command-consumer")
    props
  }

  val streams = new KafkaStreams(streamTopology, kafkaStreamProps)
  Runtime.getRuntime.addShutdownHook(new Thread(() => streams.close()))
  streams.start()

  val forSavingEventsRepository = Injector.forSavingEventsRepository

  def streamTopology = {
    val streamsBuilder = new StreamsBuilder()
    streamsBuilder
      .stream[String, String](EventType.values.map(eventType => eventType.toString))
      .foreach(
        (key, value) => {
          LOGGER.info(s"Read message with key: $key and value $value")
          var ID: Option[String] = None
          try {
            val message = objectMapper.readValue(value, MessageTypeReference)
            ID = Some(message.ID)
            val event = objectMapper.readValue(message.value, EventTypeReference).eventType match {
              case SelectStore => objectMapper.readValue(message.value, StoreEventTypeReference)
              case SelectProduct => objectMapper.readValue(message.value, ProductEventTypeReference)
              case BackStore => objectMapper.readValue(message.value, StoreEventTypeReference)
              case BackProduct => objectMapper.readValue(message.value, ProductEventTypeReference)
              case AddToCart => objectMapper.readValue(message.value, ProductEventTypeReference)
              case OrderPlaced => objectMapper.readValue(message.value, ProductsEventTypeReference)
            }
            LOGGER.info(s"[$ID] saving event $event")
            SaveEventsCommand(forSavingEventsRepository, event).execute() match {
              case Left(error) => LOGGER.warn(s"[$ID] couldn't save event: ${error.getMessage}")
              case Right(recommendations) =>  LOGGER.info(s"[$ID] event saved")
            }

          } catch {
            case e: Exception => LOGGER.error(s"[$ID] Error deserializing message with key $key. Exception: $e", e)
          }
        }
      )
    streamsBuilder.build()
  }

}
