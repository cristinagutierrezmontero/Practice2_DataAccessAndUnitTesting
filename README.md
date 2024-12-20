# Practice2
 
# ServiceOne


## Description
1. Microservice:
    Develop Quarkus microservice named serviceOne
2. Data Access:
Reactive ORM with Hibernate:
- Configure and integrate a NoSQL database (e.g., MongoDB) in service-one.
Implement CRUD operations to interact with the NoSQL database for the
following entities

- Implement a reactive data access layer in service-one using Hibernate with
Panache for reactive operations. This should include defining two simple entities
named “Employee” and “ Department” and two repositories for asynchronous
database interactions.

- Each Employ must belong to only one department

3. Asynchronous & Reactive RESTful Services in JAX-RS:
service-one microservice

- Implement reactive asynchronous GET, POST, PUT, and DELETE requests for
CRUD operations on the Employee entity.

4. Fault Tolerance:
- Integrate fault tolerance into one of your services using Quarkus' fault tolerance
features.
- Implement retry and circuit breaker mechanisms to handle failures gracefully
5. Unit Testing:
- Implement the required Unit testing for service-one and service-two with
junit and rest-assured


   
## Technologies
- Quarkus
- Java 17

## Running the application in dev mode

To run ServiceOne you need to replace this lines in application.properties file (/ServiceOne/src/main/resources)
with a mongodb instance. In my case, i used a local mongo docker container for this. 

```
quarkus.mongodb.connection-string=mongodb://localhost:27017/my-company
quarkus.mongodb.database=my-company
```

```shell script
# Run service-one
cd Practice1\ServiceOne
./mvnw quarkus:dev
```

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.



## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

# ServiceTwo


## Description
1. Microservice:
    Develop Quarkus microservice named serviceTwo
2. Implement a REST client that consume the 4 APIs from the service-one
microservice

3. Fault Tolerance:
- Integrate fault tolerance into one of your services using Quarkus' fault tolerance
features.
- Implement retry and circuit breaker mechanisms to handle failures gracefully.
4. Unit Testing:
- Implement the required Unit testing for service-one and service-two with
junit and rest-assured

## Technologies
- Quarkus
- Java 17

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script

# Run serviceOne
cd Practice2\ServiceOne
./mvnw quarkus:dev

# Run serviceTwo
cd Practice2\ServiceTwo
./mvnw quarkus:dev
```

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

# Postman collection

Practice2.postman_collection.json


