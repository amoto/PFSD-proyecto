package domain.entities

import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.module.scala.JsonScalaEnumeration
import domain.entities.EventType.EventType

case class ProductEvent(@JsonScalaEnumeration(classOf[EventTypeTypeReference]) override val eventType: EventType, override val userID: String, override val timestamp: Long, storeID: String, productID: String) extends Event(eventType, userID, timestamp)

object ProductEventTypeReference extends TypeReference[ProductEvent]
