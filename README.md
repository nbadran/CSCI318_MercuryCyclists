# MercuryCyclists: A Spring Boot Application for Microservices

## Introduction
Welcome to the MercuryCyclists project! This application is built using Spring Boot, a framework that simplifies and accelerates the development of web applications and microservices utilizing the Java Spring Framework. Since its inception in April 2014, Spring Boot has been renowned for its auto-configuration capabilities and opinionated approach to configuration, making it a popular choice for developing enterprise-level applications.

Our application leverages Spring Boot for its simplicity in coding and execution, benefiting both our development team and the end-users. Currently in its beta form, we anticipate major updates leading to an estimated completion date of October 30, 2022.

This README provides an overview of the project's structure, the functionality of each Java class, examples of input and output, and instructions for building and running the program. Our architecture ensures optimal security and ethical compliance with Australian programming guidelines by utilizing private variables as the model's foundation. Additionally, we have integrated an H2 in-memory database for temporary data storage.

## Project Structure
Our application follows a multi-tier architecture, dividing responsibilities among different layers for better management and scalability. The project consists of four main microservices, each addressing a specific aspect of the application:

- Procurement Service
- Inventory Service
- Sales Service
- Business Intelligence (BI) Service

## Procurement Service

### Supplier Package
- Supplier Class: Represents supplier entities with fields such as supplierId and supplierName, including setters, getters, constructors, and a toString() method.
- SupplierController: Handles API requests related to suppliers, utilizing annotations like @RestController, @RequestMapping, @GetMapping, @PostMapping, @PutMapping, and @DeleteMapping.
- SupplierService: Contains business logic for managing suppliers, annotated with @Service.
- SupplierRepository: Extends JpaRepository for data access, annotated with @Repository.

### Contact Package
- Contact Class: Represents contact entities with fields such as name, phone, and id, including setters, getters, constructors, and a toString() method.
- ContactController: Handles API requests related to contacts, utilizing annotations like @RestController, @GetMapping, @PostMapping, @PutMapping, and @DeleteMapping.
- ContactService: Contains business logic for managing contacts, annotated with @Service.
- ContactRepository: Extends JpaRepository for data access, annotated with @Repository.

## Inventory Service
### Part Package
- Part Class: Represents part entities with fields such as partName, description, supplierId, quantity, and product, including setters, getters, constructors, and a toString() method.

### Product Package
- Product Class: Represents product entities with fields such as productName, price, comment, stockQuantity, and parts, including setters, getters, constructors, and a toString() method.
- ProductController: Handles API requests related to products, utilizing annotations like @RestController, @GetMapping, @PostMapping, and @PutMapping.
- ProductRepository: Extends JpaRepository for data access, annotated with @Repository.
- PartController: Handles API requests related to parts, utilizing annotations like @RestController, @GetMapping, @PostMapping, and @PutMapping.
- PartRepository: Extends JpaRepository for data access, annotated with @Repository.

## Sales Service
### Store Package
- Store Class: Represents store entities with fields such as ID, address, manager, and name.
- StoreController: Handles API requests related to stores, utilizing annotations like @RestController, @GetMapping, @PostMapping, @PutMapping, and @DeleteMapping.
- StoreRepository: Extends JpaRepository for data access, annotated with @Repository.
### Sale Package
- Sale Class: Represents sale entities with fields such as saleId, quantity, dateAndTime, and productId, including setters, getters, constructors, and a toString() method.
- SaleController: Handles API requests related to sales, utilizing annotations like @RestController, @GetMapping, @PostMapping, @PutMapping, and @DeleteMapping.
- SaleRepository: Extends JpaRepository for data access, annotated with @Repository.
### InStoreSale Package
- InStoreSale Class: Extends Sale, adding fields such as receiptNo and an object of Store.
- InStoreSaleRepository: Extends JpaRepository for data access, annotated with @Repository.
### OnlineSale Package
- OnlineSale Class: Extends Sale, adding fields such as customerAddress and customerName.
- OnlineSaleRepository: Extends JpaRepository for data access, annotated with @Repository.

## Business Intelligence (BI) Service
### SaleEvent Package
- SaleEvent Class: Represents events related to sales, utilized by the Business Intelligence Service for processing.


## Domain-Driven Design Principles
Our application follows Domain-Driven Design (DDD) principles, focusing on solving business problems through code. The domain is divided into four subdomains:

1. Procurement Domain
Entities: Supplier, Contact
2. Inventory Domain
Entities: Part, Product
3. Sales Domain
Entities: Store, Sale, InStoreSale, OnlineSale
4. Business Intelligence Domain
Entities: SaleEvent
Entities are identified by unique IDs, ensuring consistency throughout the system. Value objects, such as SalesEvent, SalesQuantity, and KafkaEvent, are used for encapsulating temporary data. Services in each domain handle business logic and operations, interacting with repositories for data persistence.

## Building and Running the Application
### Prerequisites
- Java 11 or higher
- Maven
- Docker (for running Kafka)

### Steps to Build and Run
1. Clone the Repository
```bash
git clone https://github.com/yourusername/MercuryCyclists.git
cd MercuryCyclists
```
2. Build the Project

```bash
mvn clean install
```

3. Run the Application
```bash
mvn spring-boot:run
```

4. Run Kafka
```bash
docker-compose up
```
5. Example Requests (Get All Suppliers)
```bash
curl -X GET http://localhost:8080/supplier
```

6. Exaple Requests (Create a New Product)
```bash
curl -X POST http://localhost:8080/product -H "Content-Type: application/json" -d '{"productName": "New Product", "price": 100.0}'
Conclusion
```




