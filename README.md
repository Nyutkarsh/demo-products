# Items API (Spring Boot, Java 17)

A simple RESTful backend application to manage a collection of items (in-memory).

## Tech
- Java 17
- Spring Boot 3
- Bean Validation (jakarta.validation)
- In-memory repository using ArrayList (thread-safe via synchronization)

## How to Run
```bash
mvn clean test
mvn spring-boot:run
```

## Server Starts at in local machine
- http://localhost:8080

#### CURL command to create item:
```
curl --location --request POST 'http://localhost:8080/api/v1/items' \
--header 'Content-Type: application/json' \
--header 'Cookie: Cookie_4=value' \
--data-raw '{
"name": "Item 1",
"description": "desc",
"price": 10.5
}'
```


#### CURL command to get item by id
```
curl --location --request GET 'http://localhost:8080/api/v1/items/1' \
--header 'Cookie: Cookie_4=value'
```