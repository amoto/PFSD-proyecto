package domain.entities

import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.module.scala.JsonScalaEnumeration
import domain.entities.EventType.EventType

case class ProductsEvent(@JsonScalaEnumeration(classOf[EventTypeTypeReference]) override val eventType: EventType, override val userID: String, override val timestamp: Long, storeID: String, productIDs: List[String]) extends Event(eventType, userID, timestamp)

object ProductsEventTypeReference extends TypeReference[ProductsEvent]
