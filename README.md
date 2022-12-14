## How to run

- Compile the project and generate jar
  - `sbt assembly`
    - Check jar is created in target/scala-2.13/ecom-events-assembly-0.1.jar
- Run kafka
  - `docker-compose -f dockerFiles/kafka-docker-compose.yml up -d`
- Run postgresql
  - `cd dockerFiles && docker-compose -f postgres-docker-compose.yml up -d && cd -`
- Build random producer docker image
  - `docker build -f dockerFiles/randomProducer.Dockerfile -t random-events-producer:latest .`
- Run random producer docker container
  - `docker run -e KAFKA_BOOTSTRAP_SERVERS=dockerfiles_kafka_1:9092 --net dockerfiles_default --name random_events_producer random-events-producer:latest`
- Build events collector docker image
  - `docker build -f dockerFiles/saveEvents.Dockerfile -t save-events:latest .`
- Run events collector docker container
  - `docker run -e KAFKA_BOOTSTRAP_SERVERS=dockerfiles_kafka_1:9092 -e PG_CONN_STRING=jdbc:postgresql://dockerfiles_postgres_1:5432/postgres --net dockerfiles_default --name save_events save-events:latest`
- Build recommendations generator docker image
  - `docker build -f dockerFiles/generateRecommendations.Dockerfile -t generate-recommendations:latest .`
- Run recommendations generator docker container
  - `docker run -e KAFKA_BOOTSTRAP_SERVERS=dockerfiles_kafka_1:9092 -e PG_CONN_STRING=jdbc:postgresql://dockerfiles_postgres_1:5432/postgres --net dockerfiles_default --name generate_recommendations generate-recommendations:latest`
