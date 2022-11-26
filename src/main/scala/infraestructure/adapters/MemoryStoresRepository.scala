package infraestructure.adapters

import domain.entities.Store
import domain.ports.ForQueryingStoresRepository

object MemoryStoresRepository extends ForQueryingStoresRepository{

  private val stores = List(
    Store("10", "Home Burgers", Set("burger", "fast food")),
    Store("20", "Donde Pepe", Set("corn", "hot dog", "fast food")),
    Store("30", "El Corral", Set("burger", "hot dog", "fast food")),
    Store("40", "Papa Johns", Set("pizza")),
    Store("50", "Seratta", Set("italian", "spaghetti")),
    Store("60", "Wing Station", Set("wings")),
    Store("70", "KFC", Set("chicken")),
  )

  override def getStoresByCategory(category: String): Either[Throwable, List[Store]] = {
    val storesWithCategory = stores.filter(store => store.categories.contains(category))

    if (storesWithCategory.isEmpty) Left(new Throwable("no recommendations found"))
    else Right(storesWithCategory)
  }

  override def getStoreByID(ID: String): Either[Throwable, Store] = {
    stores.find(store => store.ID == ID).toRight(new Throwable("store not found"))
  }
}
