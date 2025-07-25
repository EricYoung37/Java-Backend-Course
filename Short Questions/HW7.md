# Homework 7 — Spring Data Pt. 1
**Author: M. Yang**

## Question 1
> Spring Data Annotations

See [Spring-Boot-Annotations.md - Persistence Layer](Spring-Boot-Annotations.md#4-persistence-layer-jpahibernate).


## Question 2
> - DTO (Data Transfer Object)
> - VO (Value Object)
> - Payload
> - PO (Persistent Object) / Entity
> - Model
> - DAO (Data Access Object)

| **Term**    | **Description**                                                                                              |
|-------------|--------------------------------------------------------------------------------------------------------------|
| **DTO**     | Used to transfer data between layers or services; contains no business logic, only fields with accessors.    |
| **VO**      | Represents a value without identity (identifier); immutable and equal by content; used in domain modeling.   |
| **Payload** | Refers to the actual data content in a message or API request/response; often in JSON/XML format.            |
| **PO**      | Represents a database entity; fields map directly to database columns; used with ORM tools.                  |
| **Model**   | General representation of data or business logic; may refer to PO, VO, or domain models in various contexts. |
| **DAO**     | Encapsulates database access logic; provides CRUD operations; separates persistence from business logic.     |


<details>
<summary>VO Example</summary>

```java
import java.math.BigDecimal;
import java.util.Objects;

public final class Money {
    private final BigDecimal amount;
    private final String currency;

    public Money(BigDecimal amount, String currency) {
        if (amount == null || currency == null) {
            throw new IllegalArgumentException("Amount and currency must not be null");
        }
        this.amount = amount;
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currencies must match");
        }
        return new Money(this.amount.add(other.amount), this.currency);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money)) return false;
        Money money = (Money) o;
        return amount.equals(money.amount) && currency.equals(money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return amount + " " + currency;
    }
}
```
</details>


## Question 3
> `@JsonProperty("some_prop")`

`@JsonProperty` is an annotation provided by Jackson, a popular library for JSON processing in Java.
It is used to map Java object fields to JSON properties during **serialization** (converting Java objects to JSON) and **deserialization** (converting JSON to Java objects).

### Example

```java
import com.fasterxml.jackson.annotation.JsonProperty;

public class Person {
    @JsonProperty("first_name")
    private String firstName;
    
    // constructor, getters, and, setters
}

public class JsonPropertyExample {
    public static void main(String[] args) {
        // serialization
        Person person = new Person("John");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(person);
        System.out.println(json);  // {"first_name":"John"}
        
        // deserialization
        String json = "{\"first_name\":\"John\"}";
        Person person = objectMapper.readValue(json, Person.class);
        System.out.println(person.getFirstName()); // John
    }
}
```


## Question 4
> Explain the dependency below
> ```xml
> <dependency>
>     <groupId>com.fasterxml.jackson.core</groupId>
>     <artifactId>jackson-databind</artifactId>
>     <version>2.13.3</version>
>     <scope>compile</scope>
> </dependency>
> ```

◾ `artifactId: jackson-databind`:
This is the artifact ID and refers to the specific module `jackson-databind` within the Jackson library,
which provides the functionality for **binding Java objects to JSON and vice versa**.

◾ `scope: compile`:
The scope indicates the classpath visibility of the dependency.
In this case, `compile` means that this dependency is available at compile time, during both build and runtime.


## Question 5
> What is a Spring Boot starter?
> 
> Dependencies in `spring-boot-starter-web`
> 
> Other Common Spring Boot Starters 

A Spring Boot Starter is a **pre-configured set of dependencies** provided by Spring Boot to simplify the setup of **common functionalities** in a Spring Boot application.

### Primary Dependencies in `spring-boot-starter-web`

| **Dependency**                   | **Purpose**                                                               |
|----------------------------------|---------------------------------------------------------------------------|
| `spring-boot-starter`            | Core starter: includes logging, Spring Boot core, and auto-configuration. |
| `spring-web`                     | Core Spring Web support (HTTP, REST, client/server-side communication).   |
| `spring-webmvc`                  | Spring MVC framework: DispatcherServlet, controllers, view resolution.    |
| `spring-boot-starter-json`       | JSON support including Jackson libraries for object mapping.              |
| `spring-boot-starter-tomcat`     | Embedded Tomcat server for running web applications.                      |
| `spring-boot-starter-validation` | Bean validation using Hibernate Validator (JSR-380).                      |
| `jakarta.servlet-api`            | Servlet API required for building web applications.                       |

**Simplified Dependency Tree**
```
spring-boot-starter-web
├── spring-boot-starter
│   ├── spring-boot
│   └── spring-boot-autoconfigure
├── spring-boot-starter-json
│   ├── jackson-databind
│   ├── jackson-core
│   └── jackson-annotations
├── spring-web
├── spring-webmvc
├── spring-boot-starter-tomcat
│   ├── tomcat-embed-core
│   ├── tomcat-embed-el
│   └── tomcat-embed-websocket
├── spring-boot-starter-validation
└── jakarta.servlet-api
```

### Other Common Spring Boot Starters

| **Starter**                      | **Description**                                                       |
|----------------------------------|-----------------------------------------------------------------------|
| `spring-boot-starter-data-jpa`   | For working with databases using Spring Data JPA and Hibernate        |
| `spring-boot-starter-security`   | Provides authentication and authorization using Spring Security       |
| `spring-boot-starter-test`       | Includes testing libraries like JUnit, Mockito, Hamcrest, etc.        |
| `spring-boot-starter-thymeleaf`  | Enables server-side rendering using the Thymeleaf template engine     |
| `spring-boot-starter-validation` | Provides bean validation using Hibernate Validator                    |
| `spring-boot-starter-aop`        | Adds support for aspect-oriented programming with Spring AOP          |
| `spring-boot-starter-actuator`   | Adds production-ready features like health checks and metrics         |
| `spring-boot-starter-mail`       | Supports sending email using JavaMail                                 |


## Question 6
> Explain `@RequestMapping(value = "/users", method = RequestMethod.POST)`.
> 
> List CRUD operations using `@RequestMapping`.

`@RequestMapping(value = "/users", method = RequestMethod.POST)` **maps** HTTP POST **requests** sent **to** `/users` endpoint
to the annotated **controller method**.

CRUD Operations using `@RequestMapping`

| **CRUD Operation** | **`@RequestMapping` Style**                                             | **Functionally Equivalent Annotation** |
|--------------------|-------------------------------------------------------------------------|----------------------------------------|
| **Create**         | `@RequestMapping(value = "/users", method = RequestMethod.POST)`        | `@PostMapping("/users")`               |
| **Read (Single)**  | `@RequestMapping(value = "/users/{id}", method = RequestMethod.GET)`    | `@GetMapping("/users/{id}")`           |
| **Read (All)**     | `@RequestMapping(value = "/users", method = RequestMethod.GET)`         | `@GetMapping("/users")`                |
| **Update**         | `@RequestMapping(value = "/users/{id}", method = RequestMethod.PUT)`    | `@PutMapping("/users/{id}")`           |
| **Delete**         | `@RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)` | `@DeleteMapping("/users/{id}")`        |


## Question 7
> What is `ResponseEntity`?
> 
> Why do we need it?

`ResponseEntity` is a Spring class used in controller methods to customize the HTTP response
by setting the **status code**, **headers**, and **body**, typically for REST APIs.

| **Without `ResponseEntity`**                                                                           | **With `ResponseEntity`**                                                                    |
|--------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------|
| Spring will **automatically determine the status code** based on the method and any exceptions thrown. | **Custom HTTP status codes**, headers, and body can be set explicitly.                       |
| If a method returns an object, it will **automatically be serialized** into the response body.         | **More control** over the response body, such as returning error details or custom messages. |
| The default **status code is 200 OK** for successful requests.                                         | The **status code can be customized** (e.g., `201 Created`, `404 Not Found`).                |


## Question 8
> What is `ResultSet` in JDBC (Java Database Connectivity)?
> 
> Steps for Using JDBC

In JDBC, a `ResultSet` is an object that represents the result set of a database **query**.

**Steps for Using JDBC**
1. Load the JDBC driver (optional in modern JDBC).
2. Create a connection to the database.
3. Create a statement or `PreparedStatement` for executing the SQL query.
4. Execute the query using `executeQuery()` method to get a `ResultSet`.
5. Process the `ResultSet` by iterating through rows and extracting data.
6. Close all resources (ResultSet, Statement, Connection) to release database connections and avoid memory leaks.


## Question 9
> Spring Data JPA vs. Hibernate vs. JDBC

| **Aspect**                     | **Spring Data JPA**                                                             | **Hibernate**                                                     | **JDBC**                                          |
|--------------------------------|---------------------------------------------------------------------------------|-------------------------------------------------------------------|---------------------------------------------------|
| **Overview**                   | **ORM API standard**; abstracts JDBC; implemented by frameworks like Hibernate. | **JPA implementation** with extra features; uses JDBC internally. | **Low-level SQL API**; used by JPA and Hibernate. |
| **Abstraction Level**          | High (automates CRUD operations, repository pattern)                            | Medium (ORM tool with more control)                               | Low (direct SQL interaction)                      |
| **Ease of Use**                | Very easy (minimal configuration, auto-generated queries)                       | Moderate (requires entity mapping, custom queries)                | Hard (manual SQL, connection management)          |
| **Flexibility & Querying**     | Low (auto-generated queries, JPQL support)                                      | High (HQL, Criteria API, native SQL)                              | Very high (full control over SQL)                 |
| **Performance**                | Moderate (some overhead due to abstraction)                                     | Good (performance tuning, caching)                                | High (no ORM overhead)                            |
| **Transactions & Maintenance** | Built-in transaction management, easy to maintain                               | Requires manual transaction handling, more complex maintenance    | Manual transaction handling, hard to maintain     |


## Question 10
> Read this [example](https://github.com/CTYue/chuwa-eij-tutorial/blob/main/02-java-core/src/main/java/com/chuwa/exercise/oa/api/FoodOutletJackson.java).
> How would `readTree()` be useful?
> ```java
> FoodOutlet foodOutlet = objectMapper.readValue(resBody, FoodOutlet.class);
> // compared with using objectMapper.readTree()
> ```

`readTree()` parses a JSON string into a `JsonNode` tree,
which allows traversal and querying of JSON elements **without needing a predefined Java class**
(like `FoodOutlet.class` in the example).

Therefore,
```java
// long code with class definitions

FoodOutlet foodOutlet = objectMapper.readValue(resBody, FoodOutlet.class);
int total_pages = foodOutlet.getTotal_pages();

// class FoodOutlet
// class Data
// class UserRating
```
can be replaced by
```java
// only two lines of code

JsonNode foodOutletTree = objectMapper.readTree(resBody);
int total_pages = foodOutletTree.path("total_pages").asInt();
```


## Question 11
> Serialization vs. Deserialization

- **Serialization:** converting an **object** into a **byte stream** (e.g., JSON, XML, or binary/native format) that can be easily stored in a file, sent over a network, or transferred to another system.
- **Deserialization:** the reverse process of serialization.

## Question 12
> Use stream API to get the average of the array `[20, 3, 78, 9, 6, 53, 73, 99, 24, 32]`.

```java
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int[] numbers = {20, 3, 78, 9, 6, 53, 73, 99, 24, 32};
        double average = Arrays.stream(numbers)
                               .average()
                               .orElse(Double.NaN);
        System.out.println("Average: " + average);
    }
}
```