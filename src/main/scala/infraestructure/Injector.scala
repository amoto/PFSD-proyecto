package infraestructure

import domain.ports.{ForQueryingStoresRepository, ForSavingEventsRepository}
import infraestructure.adapters.postgresql.{PostgreSQLEventsRepository, PostgreSQLStoresRepository}

object Injector {
  val forQueryingStoresRepository: ForQueryingStoresRepository = PostgreSQLStoresRepository
  val forSavingEventsRepository: ForSavingEventsRepository = PostgreSQLEventsRepository
}
