package domain.commands

import domain.entities.Event
import domain.ports.ForSavingEventsRepository

case class SaveEventsCommand[A <: Event](forSavingEventsRepository: ForSavingEventsRepository, event: A) extends Command[A] {
  override def execute(): Either[Throwable, A] = {
    forSavingEventsRepository.saveEvent(event)
  }
}
