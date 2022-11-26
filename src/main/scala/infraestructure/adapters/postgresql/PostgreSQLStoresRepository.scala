package infraestructure.adapters.postgresql

import com.typesafe.scalalogging.Logger
import domain.entities.Store
import domain.ports.ForQueryingStoresRepository

object PostgreSQLStoresRepository extends ForQueryingStoresRepository {

  lazy private val storesDao = StoreDao.getStoreDao
  private val LOGGER = Logger("PostgreSQLStoresRepository")

  override def getStoresByCategory(category: String): Either[Throwable, List[Store]] = {
    storesDao.getStoresByCategory(category) match {
      case Left(error) =>
        LOGGER.error(s"There was an error querying stores by category", error)
        Left(error)
      case Right(value) =>
        LOGGER.info(s"Found ${value.size} stores for category $category")
        Right(value)
    }
  }

  override def getStoreByID(ID: String): Either[Throwable, Store] = {
    storesDao.getStoreByID(ID) match {
      case Left(error) =>
        LOGGER.error(s"There was an error querying store by ID", error)
        Left(error)
      case Right(value) =>
        LOGGER.info(s"Store $value found for ID $ID")
        Right(value)
    }
  }
}
