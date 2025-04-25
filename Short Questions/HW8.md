# Homework 8 — Spring Data Pt. 2
**Author: M. Yang**

## Question 1
> Spring Boot Annotations Review

See [Spring-Boot-Annotations.md](Spring-Boot-Annotations.md).

## Question 2
> Write Two APIs: post and comment, where one post is mapped to many comments.

See [new redbook](../Projects/redbook).


## Question 3
> Use Postman to test the mini project from Question 2.

<details>
<summary>Post API</summary>

### Create a Post
![image](https://github.com/user-attachments/assets/aff5c0c9-a146-4c85-914d-fb952c9432c8)

### Get All Posts
![image](https://github.com/user-attachments/assets/4ebcc7b3-45e4-401e-bdaa-cbb7d6653289)

### Get a Post by ID
![image](https://github.com/user-attachments/assets/8e2da074-c928-4776-9681-8d5b71362b99)

### Update a Post by ID
![image](https://github.com/user-attachments/assets/a64bd1a4-98b1-4590-b4d1-5052d2cc4053)

### Delete a Post by ID
![image](https://github.com/user-attachments/assets/2ac76ae8-bc95-4a54-a420-d77d6e73cb8a)

**All comments under this post are also deleted.**
![image](https://github.com/user-attachments/assets/05f90f83-338c-4afe-bc6c-5ada4782d759)

</details>

<details>
<summary>Comment API</summary>

### Create a Comment
![image](https://github.com/user-attachments/assets/62216ac1-4491-4e26-9881-8453db4263c1)

### Get All Comments by Post ID
![image](https://github.com/user-attachments/assets/00722bbd-8ddc-42ec-b3dd-aa1eab909d9e)

### Get a Comment by Post ID & Comment ID
![image](https://github.com/user-attachments/assets/e475c78c-9ff5-401b-b461-200901e88b2b)

### Update a Comment by Post ID & Comment ID
![image](https://github.com/user-attachments/assets/ee6f5d26-65d6-4f55-a1d9-b2d7166b8079)

### Delete a Comment by Post ID & Comment ID
![image](https://github.com/user-attachments/assets/770bb427-8191-4cd3-9ef3-9500d4e62739)

</details>


## Question 4
> JPA vs. Hibernate

See [HW7 Question 9](HW7.md#question-9)


## Question 5
> What is HikariCP?
> 
> Benefits of connection pools

HikariCP is a high-performance **JDBC connection pool library** for Java applications.

| Benefit                                        | Description                                                                                                                                  |
|------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------|
| **Improved Performance**                       | Reduces overhead by reusing existing connections, leading to faster response times.                                                          |
| **Efficient Resource & Connection Management** | Manages limited connections, automatically validates, and replaces faulty connections, ensuring optimal usage and freeing up idle resources. |
| **Scalability**                                | Can scale to handle large numbers of simultaneous database requests.                                                                         |
| **Reliability**                                | Provides automatic recovery from failures and ensures better fault tolerance.                                                                |
| **Thread Management**                          | Optimizes resource sharing and synchronization between threads.                                                                              |


## Question 6
> - `@OneToMany`
> - `@ManyToOne`
> - `@ManyToMany`

See [Spring-Boot-Annotations.md](Spring-Boot-Annotations.md#-onetomany).


## Question 7
> - `CascadeType`
> - `orphanRemoval`

See [Spring-Boot-Annotations.md](Spring-Boot-Annotations.md#-cascade).


## Question 8
> `FetchType`

See [Spring-Boot-Annotations.md](Spring-Boot-Annotations.md#-fetch).


## Question 9
> JPA Repository Method Naming Convention
> 
> Is manual method implementation needed?

### JPA Repository Method Naming Convention

```
<action>[Distinct]By
<Property1>
[<comparison>]
[<LogicalOP><Property2><comparison>]...[OrderBy<Property>[order]]
```

**Components Breakdown**

| Part               | Description                                        | Examples                              |
|--------------------|----------------------------------------------------|---------------------------------------|
| **`<action>`**     | `find`, `delete`, `count`, etc.                    | `findBy`, `deleteBy`, `countBy`       |
| **`Distinct`**     | (Optional) Returns unique results                  | `findDistinctBy`                      |
| **`By`**           | Starts the query conditions                        |                                       |
| **`<Property>`**   | field name, entity                                 | `Email`, `FirstName`                  |
| **`<comparison>`** | (Optional) Comparison operator                     | `Like`, `GreaterThan`, `In`, `IsNull` |
| **`<LogicalOP>`**  | Logical operator to chain conditions (`And`, `Or`) | `And`, `Or`                           |
| **`OrderBy`**      | (Optional) Sorts results by a property             | `OrderByAge`                          |
| **`<order>`**      | (Optional) Sorting direction (`Asc`/`Desc`)        | `OrderByDateDesc`                     |


**Example Method Name**

```java
findDistinctByDepartmentNameContainingAndSalaryBetweenOrEmployeeStatusInOrderByHireDateDesc(
    String departmentNameFragment, 
    double minSalary, 
    double maxSalary, 
    List<String> statuses
);
```

| **Component**                | **Explanation**                                                                                          |
|------------------------------|----------------------------------------------------------------------------------------------------------|
| `findDistinct`               | Ensures the query returns distinct (non-duplicate) results.                                              |
| `ByDepartmentNameContaining` | Filters the results where the `departmentName` contains the given fragment (`departmentNameFragment`).   |
| `AndSalaryBetween`           | Filters employees whose salary is between `minSalary` and `maxSalary` (inclusive).                       |
| `OrEmployeeStatusIn`         | Filters employees whose `employeeStatus` is `In` the list provided (`statuses`), with an `Or` condition. |
| `OrderByHireDateDesc`        | Orders the results by `hireDate` in descending order (most recently hired employees appear first).       |


### Is manual method implementation needed?
**No.**
If the method **follows** Spring Data JPA’s **naming rules**, it will be **auto-implemented at runtime** by Spring.
Manual implementation is only necessary if the query is too complex or does not fit the naming conventions,
in which case custom queries (JPQL or native SQL) can be used.

**JPQL Query Example**
```java
@Query("SELECT p FROM Person p WHERE p.age > ?1")
List<Person> findPeopleAboveAge(int age);
```


## Question 10
> Add an advanced JPA repository method to the project in Question 2.

See [new redbook](../Projects/redbook/src/main/java/com/company/backend/redbook/service/impl).

<details>
<summary>Postman Test Results</summary>

### All Comments under the post with id 4
![image](https://github.com/user-attachments/assets/ba4d367b-2b0e-4b5a-ae53-fafbb4870300)

### Comments with keyword "Mauritius" after created `2025-04-24T21:03:43.222037` under this post
![image](https://github.com/user-attachments/assets/2510eec3-6e58-4a67-a1a6-1771a6fd9ae6)

</details>


## Question 13
> What is JPQL?

JPQL (Java Persistence Query Language) is an object-oriented query language, **similar in syntax to SQL**,
used for **querying entity** objects, their attributes, and relationships as defined in the Java Persistence API (JPA), rather than operating directly on database tables.


## Question 14
> `@NamedQuery` vs. `@NamedQueries`

In the Java Persistence API (JPA), `@NamedQuery` and `@NamedQueries` are annotations used to define **static, precompiled JPQL queries** associated with an **entity** class.
- `@NamedQuery` defines a single query
- `@NamedQueries` serves as a container for declaring **multiple** `@NamedQuery`

```java
@Entity
@NamedQueries({
        @NamedQuery(
                name = "Employee.findByDepartment",
                query = "SELECT e FROM Employee e WHERE e.department = :department"
        ),
        @NamedQuery(
                name = "Employee.findByName",
                query = "SELECT e FROM Employee e WHERE e.name = :name"
        )
})
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String department;

    // Getters and setters
}
```
```java

@Service
public class EmployeeService {

    @Autowired
    private EntityManager entityManager;

    public List<Employee> findEmployeesByDepartment(String department) {
        TypedQuery<Employee> query = entityManager.createNamedQuery(
                "Employee.findByDepartment",
                Employee.class);
        query.setParameter("department", department);
        return query.getResultList();
    }

    public List<Employee> findEmployeesByName(String name) {
        TypedQuery<Employee> query = entityManager.createNamedQuery(
                "Employee.findByName",
                Employee.class);
        query.setParameter("name", name);
        return query.getResultList();
    }
}
```


## Question 15
> `@Query`
> 
> Where to write SQL or JPQL

`@Query` in Spring Data JPA (not part of standard JPA) to define custom **SQL or JPQL queries** directly within the **repository interfaces**.

```java
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String department;

    // Getters and setters
}
```

```java
// JPQL
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT e FROM Employee e WHERE e.department = :department")
    // e is a necessary alias for the Employee entity
    List<Employee> findByDepartment(@Param("department") String department);
}

// Native SQL
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query(value = "SELECT * FROM employee WHERE department = :department", nativeQuery = true)
    List<Employee> findByDepartmentNative(@Param("department") String department);
}
```

`:department` is a **named parameter** that acts as a placeholder for a value to be **supplied at runtime**,
with the value provided dynamically when executing the query,
either via **method arguments** or set programmatically
(use `entityManager.createQuery` and `setParameter()` like [Question 14](#question-14)).


## Question 16
> - HQL
> - `Criteria`

### ◆ HQL

HQL (Hibernate Query Language) is an object-oriented query language used in Hibernate, similar to JPQL.

### ◆ `Criteria`

`Criteria` is an object-oriented **API in Hibernate** for building dynamic, **type-safe** queries programmatically, typically used as an **alternative to** HQL or SQL for querying the database.

```java
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String department;

    // Getters and setters
}
```
```java
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.hibernate.Criteria;
import javax.persistence.criteria.*;

import java.util.List;

public class EmployeeService {
    
    @Autowired
    private SessionFactory sessionFactory;

    public List<Employee> getEmployeesByDepartment(String departmentName) {
        // Open a session
        Session session = sessionFactory.openSession();

        // Create CriteriaBuilder
        CriteriaBuilder builder = session.getCriteriaBuilder();

        // Create a CriteriaQuery object for Employee
        CriteriaQuery<Employee> criteriaQuery = builder.createQuery(Employee.class);

        // Define the root of the query (the entity)
        Root<Employee> root = criteriaQuery.from(Employee.class);

        // Add condition (where department equals the provided value)
        criteriaQuery.select(root)
                     .where(builder.equal(root.get("department"), departmentName));

        // Execute the query and get the results
        Query<Employee> query = session.createQuery(criteriaQuery);
        List<Employee> employees = query.getResultList();

        // Close the session
        session.close();

        return employees;
    }
}
```


## Question 17
> `EntityManager`

`EntityManager` is a fundamental **interface in JPA** used to **manage entity** objects, perform **database operations**, and handle the persistence context.
- **persistence context:** set of entity instances that are managed by the `EntityManager` during a particular **session**.

```java
@PersistenceContext // tells Spring to automatically inject EntityManager
private EntityManager entityManager;

// @Transactional
public Employee getEmployeeById(Long id) {
    return entityManager.find(Employee.class, id);
}

// @Transactional
public void createEmployee(Employee employee) {
    entityManager.persist(employee);
}
```

Another example in [Question 14](#question-14).


## Question 18
> - `Session`
> - `SessionFactory`

### `Session`

The `Session` is the primary **interface in Hibernate** for performing CRUD operations on persistent entities,
representing **a single unit of work** typically **used within a transaction** to manage entities and execute queries.

### `SessionFactory`

The `SessionFactory` is a thread-safe, heavyweight object responsible for **creating `Session`** instances,
typically **instantiated once** during application startup and shared throughout the application.

```java
@Service
public class EmployeeService {

    @Autowired
    private SessionFactory sessionFactory;

    public void saveEmployee(Employee employee) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.save(employee);

        session.getTransaction().commit();

        session.close();
    }

    public Employee getEmployee(Long id) {
        Session session = sessionFactory.openSession();
        
        Employee employee = session.get(Employee.class, id);
        
        session.close();

        return employee;
    }
}
```

Another example in [Question 14](#question-14).


## Question 19
> What is a transaction?
> 
> How to manage transactions?

A transaction in the context of a database and persistence framework like Hibernate is a logical unit of work that **groups multiple database operations** into **a single, atomic operation**.

A transaction typically involves the following principles, often referred to as ACID properties:
- **Atomicity:** All operations within the transaction are treated as a single unit, and **either all** are completed successfully, **or none** are.
- **Consistency:** The transaction ensures that the database transitions from one valid state to another, maintaining its **integrity**.
- **Isolation:** The operations of a transaction are **isolated from others**, meaning the effects of one transaction are not visible to other transactions until it is committed.
- **Durability:** Once a transaction is committed, its **effects are permanent** and survive system crashes.

### With `EntityManager` (Manual Management)
```java
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EmployeeService {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit");

    public void saveEmployee(Employee employee) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(employee);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
```

### With `SessionFactory`
```java
public void saveEmployee(Employee employee) {
    Session session = sessionFactory.openSession();

    session.beginTransaction();

    try {
        session.save(employee);

        session.getTransaction().commit();
    } catch (Exception e) {
        session.getTransaction().rollback();
        e.printStackTrace();
    } finally {
        session.close();
    }
}
```

### With `EntityManager` and `Transactional` in Spring
See [Question 17](#question-17).


## Question 20
> Hibernate Caching Mechanism

Hibernate enhances performance by using a multi-level caching mechanism—primarily **first-level** and **second-level** caches—to reduce database access through temporary data storage.

| Cache Type             | Scope                              | Enabled by Default | Description                                                                                   | Lifecycle                            |
|------------------------|------------------------------------|--------------------|-----------------------------------------------------------------------------------------------|--------------------------------------|
| First-Level Cache      | Per Hibernate session              | Yes                | Stores **entities within a session**; repeated retrievals fetch from cache, not the database. | Until the session is closed          |
| Second-Level Cache     | Across sessions (`SessionFactory`) | No                 | Shared cache for **entities across sessions**; requires external cache provider.              | While the `SessionFactory` is active |
| Query Cache (Optional) | Query results (HQL/`Criteria`)     | No                 | Caches **results of queries** (not entities); depends on second-level cache.                  | While the `SessionFactory` is active |

### First-level Cache Example
```java
Session session = sessionFactory.openSession();
Employee emp1 = session.get(Employee.class, 1); // hits DB
Employee emp2 = session.get(Employee.class, 1); // fetched from first-level cache
```

### Second-level Cache Example

<details>
<summary>Add a Cache Provider Dependency</summary>

**EHCache**
```xml
<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-ehcache</artifactId>
    <version>5.4.32.Final</version>
</dependency>
<dependency>
    <groupId>org.ehcache</groupId>
    <artifactId>ehcache</artifactId>
    <version>3.9.5</version>
</dependency>
```

**Infinispan**
```xml
<dependency>
    <groupId>org.infinispan</groupId>
    <artifactId>infinispan-core</artifactId>
    <version>12.0.0.Final</version>
</dependency>
```
</details>

<details>
<summary>Enable Second-level cache for Hibernate</summary>

```properties
# application.properties
hibernate.cache.use_second_level_cache=true
hibernate.cache.region.factory_class=org.hibernate.cache.ehcache.EhCacheRegionFactory
```
</details>

```java
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Entity
public class Department {
    @Id
    private Long id;
    // fields...
}
```


### Query Cache Example

<details>
<summary>Enable Query cache for Hibernate</summary>

```properties
# application.properties
hibernate.cache.use_query_cache=true
```
</details>

```java
@Service
public class DepartmentService {

    @Autowired
    private SessionFactory sessionFactory;

    public List<Department> getDepartments() {
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("from Department");
            query.setCacheable(true);  // Enable query caching
            return query.list();
        }
    }
}
```

## Question 21
> First-level Cache vs. Second-level Cache

See [Question 20](#question-20).


## Question 22
> `@Transactional`

`@Transactional` is an annotation used **in Spring** Framework to declaratively manage transactions.
It indicates that the annotated **method** or **class** should be **executed within a transactional context**,
meaning that all operations within that scope are treated as a single unit of work — **either** all succeed (commit) **or** all fail (rollback).

See [Question 17](#question-17) for a code example.