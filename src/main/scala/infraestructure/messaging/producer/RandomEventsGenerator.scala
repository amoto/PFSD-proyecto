package infraestructure.messaging.producer

import domain.entities.EventType.{AddToCart, BackProduct, BackStore, EventType, OrderPlaced, SelectProduct, SelectStore}
import domain.entities._

import java.util.Date
import scala.util.Random;

object RandomEventsGenerator {

  lazy val userIDs = List("1", "2", "3", "4", "5", "6", "7")
  lazy val storeIDs = List("10", "20", "30", "40", "50", "60", "70")
  lazy val productIDs = List("100", "200", "300", "400", "500", "600", "700")
  val eventTypes: List[EventType] = EventType.values.toList
  val random = new Random()

  def produceEvent(): Event = {
    val eventType = eventTypes(random.nextInt(eventTypes.size))
    val userID = userIDs(random.nextInt(userIDs.size))
    val storeID = storeIDs(random.nextInt(storeIDs.size))
    eventType match {
      case SelectStore => StoreEvent(eventType, userID, new Date().getTime, storeID)
      case BackStore => StoreEvent(eventType, userID, new Date().getTime, storeID)
      case SelectProduct =>
        val productID = generateRandomProductID
        ProductEvent(eventType, userID, new Date().getTime, storeID, productID)
      case BackProduct =>
        createRandomProductEvent(eventType, userID, storeID)
      case AddToCart =>
        createRandomProductEvent(eventType, userID, storeID)
      case OrderPlaced => ProductsEvent(eventType, userID, new Date().getTime, storeID, productIDs)
    }
  }

  private def generateRandomProductID = {
    productIDs(random.nextInt(productIDs.size))
  }

  private def createRandomProductEvent(eventType: EventType, userID: String, storeID: String) = {
    val productID = generateRandomProductID
    ProductEvent(eventType, userID, new Date().getTime, storeID, productID)
  }
}
