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
> JPA vs Hibernate

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

#### All Comments under the post with id 4
![image](https://github.com/user-attachments/assets/ba4d367b-2b0e-4b5a-ae53-fafbb4870300)

#### Comments with keyword "Mauritius" after created `2025-04-24T21:03:43.222037` under this post
![image](https://github.com/user-attachments/assets/2510eec3-6e58-4a67-a1a6-1771a6fd9ae6)

</details>


## Question 13
> What is JPQL?
