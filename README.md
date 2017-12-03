[![Build Status](https://travis-ci.org/bilalwahla/reactive-demo.svg?branch=master)](https://travis-ci.org/bilalwahla/reactive-demo)

# Building a Reactive Microservice


## What you'll build

You’ll build a reactive tweet service using [Spring WebFlux](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html#spring-webflux) that uses Spring Data Reactive Repository for reading and writing the tweets to [mongoDB](https://www.mongodb.com/).

Service will accept **GET** & **POST** requests at

```
http://localhost:8080/tweets
```

and **GET**, **PUT** and **DELETE** requests at

```
http://localhost:8080/tweets/{id}
```

where 'id' is the identifier for the tweet to perform the action on.

Service also accepts **GET** requests at

```
http://localhost:8080/stream/tweets
```

producing (responding with) text/event-stream.

## What you'll need

* About an hour
* [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/index.html) or later
* [mongoDB](https://www.mongodb.com/)

## Build with Maven

In order to write this service I have used [Spring Boot](https://projects.spring.io/spring-boot/) making it  super simple to include all the required dependecies wihout having to manage each and every one. For this service following are the only Spring Boot dependencies we need to define for it to fetch all (transitively) for us:

* spring-boot-starter-webflux
* spring-boot-starter-data-mongodb-reactive

Rest of the dependencies defined in the script are scoped only for testing.

The [Spring Boot Maven plugin](https://github.com/spring-projects/spring-boot/tree/master/spring-boot-tools/spring-boot-maven-plugin) provides many convenient features

* It collects all the jars on the classpath and builds a single, runnable "über-jar", which makes it more convenient to execute and transport your service.
* It searches for the ```public static void main()``` method to flag as a runnable class.
* It provides a built-in dependency resolver that sets the version number to match [Spring Boot dependencies](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-dependencies/pom.xml). You can override any version you wish, but it will default to Boot’s chosen set of versions.

To build the service run

```
./mvnw clean build
```

and to run (by default on port 8080) of course you need to first have [mongoDB](https://www.mongodb.com/) installed and up and running (by default on port 27017). Then you can simply run the following command and give the service a go

```
./mvnw spring-boot:run
```

