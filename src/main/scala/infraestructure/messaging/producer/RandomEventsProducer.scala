package infraestructure.messaging.producer;

object RandomEventsProducer extends App {
  for (_ <- 0 to 100) {
    val event = RandomEventsGenerator.produceEvent()
    Producer.produce(event.eventType.toString, event.userID, event)
    Thread.sleep(1000)
  }
}
