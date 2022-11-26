FROM openjdk:8-jre-alpine

USER root
COPY target/scala-2.13/ecom-events-assembly-0.1.jar app.jar
ENTRYPOINT ["java", "-cp", "app.jar", "infraestructure.messaging.producer.RandomEventsProducer"]
