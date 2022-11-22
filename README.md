# Book Project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .


## Provision your Kafka Cluster
The demo requires access to an Apache Kafka cluster, and the quickest way to get started free is on [Confluent Cloud](https://www.confluent.io/confluent-cloud/tryfree), which provides Kafka as a fully managed service.



## Write the cluster information into Quarkus properties file
From the Confluent Cloud Console, navigate to your Kafka cluster and then select Clients in the lefthand navigation. From the Clients view, create a new client and click Java to get the connection information customized to your cluster.

Create new credentials for your Kafka cluster and Schema Registry, writing in appropriate descriptions so that the keys are easy to find and delete later. The Confluent Cloud Console will show a configuration similar to below with your new credentials automatically populated (make sure Show API keys is checked). Copy and paste it into a src/main/resources/application.properties file on your machine.
```shell script
kafka.bootstrap.servers={{ BOOTSTRAP_SERVERS }}
kafka.security.protocol=SASL_SSL
kafka.sasl.mechanism=PLAIN
kafka.sasl.jaas.config=org.apache.kafka.common.security.plain.PlainLoginModule required \
    username='{{ CLUSTER_API_KEY }}' \
    password='{{ CLUSTER_API_SECRET }}';

mp.messaging.connector.smallrye-kafka.schema.registry.url={{ SR_URL }}
mp.messaging.connector.smallrye-kafka.basic.auth.credentials.source=USER_INFO
mp.messaging.connector.smallrye-kafka.basic.auth.user.info={{ SR_API_KEY }}:{{ SR_API_SECRET }}
```


## Download and setup the Confluent CLI
The demo has some steps for Kafka topic management and/or reading from or writing to Kafka topics, for which you can use the Confluent Cloud Console or install the Confluent CLI. Instructions for installing Confluent CLI and configuring it to your Confluent Cloud environment is available from within the Confluent Cloud Console: navigate to your Kafka cluster, click on the CLI and tools link, and run through the steps in the Confluent CLI tab.


## Create a topic
Create the following topics for use during this demo. Use the following command to create the topic:
```shell script
confluent kafka topic create books --partitions 1
confluent kafka topic create published-books --partitions 1
```


## Edit OpenTelemetry Exporter configuration
From the Elastic Cloud Console left panel, navigate to Management --> Integrations --> Elastic APM. Under the APM Agents, navigate to OpenTelemtry tab. Copy and paste OTEL_EXPORTER_OTLP_ENDPOINT and the secret token from OTEL_EXPORTER_OTLP_HEADERS into otlp/otel-collector-config.yaml file on your machine.

```shell script
exporters:
  otlp/elastic:
    endpoint: {{APM_OTLP_ENDPOINT}}
    tls:
      insecure_skip_verify: true
    headers:
      Authorization: "Bearer {{APM_SECRET_TOKEN}}" 
  logging:
    loglevel: debug
```


## Run OpenTelemetry Exporter
```shell script
docker-compose up -d
```


## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.


## Test the application by posting a Book record
```
curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"isbn": "1234567890123", "title": "Sample Book Title", "author": "John Doe"}' \
  http://localhost:8080/books
```


## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/Book-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Related Guides

- SmallRye Reactive Messaging - Kafka Connector ([guide](https://quarkus.io/guides/kafka-reactive-getting-started)): Connect to Kafka with Reactive Messaging

