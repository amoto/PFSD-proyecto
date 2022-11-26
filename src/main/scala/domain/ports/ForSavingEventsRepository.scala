package domain.ports

import domain.entities.Event

trait ForSavingEventsRepository {
  def saveEvent[A <: Event](event: A): Either[Throwable, A]
}
