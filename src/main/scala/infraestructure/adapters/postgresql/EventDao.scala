package infraestructure.adapters.postgresql

import domain.entities.{Event, ProductEvent, ProductsEvent, StoreEvent}
import org.apache.commons.dbcp2.BasicDataSource

import java.sql.{Connection, PreparedStatement, Timestamp}

case class EventDao(dataSource: BasicDataSource) {
  private val insertSQL = "INSERT INTO %s (%s) VALUES (%s)"

  def saveEvent[A <: Event](event: A): Either[Throwable, A] = {
    event match {
      case storeEvent: StoreEvent =>
        val sql = String.format(insertSQL, storeEvent.eventType.toString, "event_type, user_id, timestamp, store_id", "?, ?, ?, ?")
        try {
          val (connection, ps) = setBasePreparedStatement(storeEvent, sql)
          ps.setString(4, storeEvent.storeID)

          executeSave(event, connection, ps)
        } catch {
          case e: Exception => Left(e)
        }
      case productEvent: ProductEvent =>
        val sql = String.format(insertSQL, productEvent.eventType.toString, "event_type, user_id, timestamp, store_id, product_id", "?, ?, ?, ?, ?")
        try {
          val (connection: Connection, ps: PreparedStatement) = setBasePreparedStatement(productEvent, sql)
          ps.setString(4, productEvent.storeID)
          ps.setString(5, productEvent.productID)

          executeSave(event, connection, ps)
        } catch {
          case e: Exception => Left(e)
        }
      case productsEvent: ProductsEvent =>
        val sql = String.format(insertSQL, productsEvent.eventType.toString, "event_type, user_id, timestamp, store_id, product_ids", "?, ?, ?, ?, ?")
        try {
          val (connection: Connection, ps: PreparedStatement) = setBasePreparedStatement(productsEvent, sql)
          ps.setString(4, productsEvent.storeID)
          ps.setString(5, productsEvent.productIDs.toString())

          executeSave(event, connection, ps)
        } catch {
          case e: Exception => Left(e)
        }
    }
  }

  private def executeSave[A <: Event](event: A, connection: Connection, ps: PreparedStatement): Either[Throwable, A] = {
    ps.execute()
    connection.close()

    Right(event)
  }

  private def setBasePreparedStatement(event: Event, sql: String): (Connection, PreparedStatement) = {
    val connection = dataSource.getConnection
    val ps = connection.prepareStatement(sql)
    ps.setString(1, event.eventType.toString)
    ps.setString(2, event.userID)
    ps.setTimestamp(3, new Timestamp(event.timestamp))
    (connection, ps)
  }
}

object EventDao {
  def getEventDao: EventDao = {
    val dbUrl = sys.env.getOrElse("PG_CONN_STRING", "jdbc:postgresql://localhost:5432/postgres")
    val connectionPool = new BasicDataSource()
    connectionPool.setUsername(sys.env.getOrElse("PG_USER", "postgres"))
    connectionPool.setPassword(sys.env.getOrElse("PG_PASSWD", "postgres"))
    connectionPool.setDriverClassName("org.postgresql.Driver")
    connectionPool.setUrl(dbUrl)
    connectionPool.setInitialSize(5)

    EventDao(connectionPool)
  }
}
