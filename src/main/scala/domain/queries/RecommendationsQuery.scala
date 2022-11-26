package domain.queries

import domain.entities.Store
import domain.ports.ForQueryingStoresRepository

case class RecommendationsQuery(forQueryingStoresRepository: ForQueryingStoresRepository, storeID: String) extends Query[List[Store]] {
  override def execute(): Either[Throwable, List[Store]] = {
    val storeOption = forQueryingStoresRepository.getStoreByID(storeID)
    storeOption match {
      case Left(error) => Left(new Throwable(s"cannot generate recommendations because => ${error.getMessage}", error))
      case Right(store) => store.categories.headOption match {
        case Some(category) => forQueryingStoresRepository.getStoresByCategory(category) match {
          case Left(error) => Left(new Throwable(s"cannot generate recommendations because => ${error.getMessage}", error))
          case Right(value) =>
            val recommendations = value.filter(store => store.ID != storeID)
            if (recommendations.isEmpty) Left(new Throwable(s"cannot generate recommendations because => the only store matching was the source of the recommendation"))
            else Right(recommendations)
        }
        case None => Left(new Throwable("cannot generate recommendations because => the store is not categorized"))
      }
    }
  }
}
