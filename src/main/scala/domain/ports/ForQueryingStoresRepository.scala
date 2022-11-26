package domain.ports

import domain.entities.Store

trait ForQueryingStoresRepository {
  def getStoresByCategory(category: String): Either[Throwable, List[Store]]
  def getStoreByID(ID: String): Either[Throwable, Store]
}
