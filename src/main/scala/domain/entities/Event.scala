package domain.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.module.scala.JsonScalaEnumeration
import domain.entities.EventType.EventType

@JsonIgnoreProperties(ignoreUnknown = true)
class Event(@JsonScalaEnumeration(classOf[EventTypeTypeReference]) val eventType: EventType, val userID: String, val timestamp: Long)

object EventTypeReference extends TypeReference[Event]
