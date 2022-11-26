package infraestructure.adapters.postgresql

import domain.entities.Store
import org.apache.commons.dbcp2.BasicDataSource

import scala.collection.mutable.ListBuffer

case class StoreDao(dataSource: BasicDataSource) {
  private val queryStoreByID = "SELECT id, name, categories FROM stores WHERE id = ?"
  private val queryStoresByCategory = "SELECT id, name, categories FROM stores WHERE categories LIKE ?"

  def getStoreByID(ID: String): Either[Throwable, Store] = {
    try {
      val connection = dataSource.getConnection
      val ps = connection.prepareStatement(queryStoreByID)
      ps.setString(1, ID)
      val rs = ps.executeQuery()
      rs.next()
      val categories = rs.getString(3)
      val store = Store(
        rs.getString(1),
        rs.getString(2),
        categories.substring(1, categories.length-1).split(",").toSet
      )
      connection.close()

      Right(store)
    }catch {
      case e: Exception => Left(e)
    }
  }

  def getStoresByCategory(category: String): Either[Throwable, List[Store]] = {
    try {
      val connection = dataSource.getConnection
      val ps = connection.prepareStatement(queryStoresByCategory)
      ps.setString(1, s"%$category%")
      val result = ListBuffer[Store]()
      val rs = ps.executeQuery()
      while (rs.next()) {
        val categories = rs.getString(3)
        result.addOne(Store(
          rs.getString(1),
          rs.getString(2),
          categories.substring(1, categories.length-1).split(",").toSet
        ))
      }
      connection.close()

      Right(result.toList)
    }catch {
      case e: Exception => Left(e)
    }
  }
}

object StoreDao {
  def getStoreDao: StoreDao = {
    val dbUrl = sys.env.getOrElse("PG_CONN_STRING", "jdbc:postgresql://localhost:5432/postgres")
    val connectionPool = new BasicDataSource()
    connectionPool.setUsername(sys.env.getOrElse("PG_USER", "postgres"))
    connectionPool.setPassword(sys.env.getOrElse("PG_PASSWD", "postgres"))
    connectionPool.setDriverClassName("org.postgresql.Driver")
    connectionPool.setUrl(dbUrl)
    connectionPool.setInitialSize(5)

    StoreDao(connectionPool)
  }
}
