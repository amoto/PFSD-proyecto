package infraestructure.adapters.postgresql

import com.typesafe.scalalogging.Logger
import domain.entities.Event
import domain.ports.ForSavingEventsRepository

object PostgreSQLEventsRepository extends ForSavingEventsRepository {

  lazy private val eventsDAO = EventDao.getEventDao
  private val LOGGER = Logger("PostgreSQLEventsRepository")

  override def saveEvent[A <: Event](event: A): Either[Throwable, A] = {
    eventsDAO.saveEvent(event) match {
      case Left(error) =>
        LOGGER.error(s"There was an error saving the event", error)
        Left(error)
      case Right(value) =>
        LOGGER.info("Event saved")
        Right(value)
    }
  }
}
