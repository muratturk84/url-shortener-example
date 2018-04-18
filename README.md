# url-shortener-example

Sample URL Shortener Application

Written with Java 8 using Spring Boot, Gradle, MongoDB, Lombok, Google Guava and Docker.

## Build, test, run

In order to test (Be careful that MondoDB is up and running. You can run with ``./mongod``), run:
```
./gradlew test
```
In order to build, run:
```
./gradlew build
```
In order to start web server, run:
```
./gradlew bootRun
```
In order to push to docker cloud, run:
```
./gradlew buildDocker
```

## Docker

Docker image is stored at: ``muratturk/url-shortener-example``

In order to install dependencies and image, run:
```
./docker-compose up
```
In order to stop docker:
```
./docker-compose down
```
