package infraestructure.adapters

import com.typesafe.scalalogging.Logger
import domain.entities.Event
import domain.entities.EventType.EventType
import domain.ports.ForSavingEventsRepository

import scala.collection.mutable

object MemoryEventsRepository extends ForSavingEventsRepository {
  private val LOGGER = Logger("MemoryEventsRepository")

  private val storage: mutable.Map[EventType, List[Event]] = mutable.HashMap[EventType, List[Event]]()

  override def saveEvent[A <: Event](event: A): Either[Throwable, A] = {
    storage.put(event.eventType, storage.getOrElse(event.eventType, List()) :+ event)
    LOGGER.info(s"memory events storage updated $storage")

    Right(event)
  }
}
