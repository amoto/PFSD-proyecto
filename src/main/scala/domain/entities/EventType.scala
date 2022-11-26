package domain.entities

import com.fasterxml.jackson.core.`type`.TypeReference
import domain.entities

object EventType extends Enumeration {
  type EventType = Value

  val SelectStore: entities.EventType.Value = Value("SELECT_STORE")
  val SelectProduct: entities.EventType.Value = Value("SELECT_PRODUCT")
  val BackStore: entities.EventType.Value = Value("BACK_STORE")
  val BackProduct: entities.EventType.Value = Value("BACK_PRODUCT")
  val AddToCart: entities.EventType.Value = Value("ADD_TO_CART")
  val OrderPlaced: entities.EventType.Value = Value("ORDER_PLACED")
}

class EventTypeTypeReference extends TypeReference[EventType.type]