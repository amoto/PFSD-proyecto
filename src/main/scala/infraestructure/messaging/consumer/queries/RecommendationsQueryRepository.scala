package infraestructure.messaging.consumer.queries

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.typesafe.scalalogging.Logger
import domain.entities.StoreEventTypeReference
import domain.queries.RecommendationsQuery
import infraestructure.Injector
import infraestructure.messaging.MessageTypeReference
import infraestructure.messaging.producer.Producer
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.serialization.Serdes._

import java.util.Properties

object RecommendationsQueryRepository extends App {
  private val objectMapper = new ObjectMapper()
  objectMapper.registerModule(DefaultScalaModule)
  private val LOGGER = Logger("RecommendationsQueryRepository")
  LOGGER.info("Starting RecommendationsQueryRepository")

  val kafkaStreamProps: Properties = {
    val props = new Properties()
    props.put("bootstrap.servers", s"${sys.env.getOrElse("KAFKA_BOOTSTRAP_SERVERS", "localhost:9093")}")
    props.put("application.id", "recommendations-query-consumer")
    props
  }

  val streams = new KafkaStreams(streamTopology, kafkaStreamProps)
  Runtime.getRuntime.addShutdownHook(new Thread(() => streams.close()))
  streams.start()

  val forQueryingStoresRepository = Injector.forQueryingStoresRepository

  def streamTopology = {
    val streamsBuilder = new StreamsBuilder()
    streamsBuilder
      .stream[String, String]("BACK_STORE")
      .foreach(
        (key, value) => {
          LOGGER.info(s"Read message with key: $key and value $value")
          var ID: Option[String] = None
          try {
            val message = objectMapper.readValue(value, MessageTypeReference)
            ID = Some(message.ID)
            val storeBackEvent = objectMapper.readValue(message.value, StoreEventTypeReference)
            LOGGER.info(s"[$ID] generating recommendations for store ${storeBackEvent.storeID}")
            RecommendationsQuery(forQueryingStoresRepository, storeBackEvent.storeID).execute() match {
              case Left(error) => LOGGER.warn(s"[$ID] couldn't find recommendations for message: ${error.getMessage}")
              case Right(recommendations) => Producer.produce("recommendations", storeBackEvent.userID, recommendations, ID)
            }

          } catch {
            case e: Exception => LOGGER.error(s"[$ID] Error deserializing message with key $key. Exception: $e", e)
          }
        }
      )
    streamsBuilder.build()
  }

}
