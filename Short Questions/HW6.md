# Homework 6 - Spring Boot Introduction
**Author: M.Y. Yang**

## Question 1
> Common Spring Boot Annotations

See [SpringBoot-Annotations.md](Spring-Boot-Annotations)

## Question 2
> Explain how `@Column` defines a column in the example below.
> ```java
> @Column(columnDefinition = "varchar(255) default 'John Snow'")
> private String name;
> 
> @Column(name="STUDENT_NAME", length=50, nullable=false, unique=false)
> private String studentName;
> ```

| Field         | Column Name      | Type           | Default Value      | Nullable | Unique  |
|---------------|------------------|----------------|--------------------|----------|---------|
| `name`        | `name` (default) | `varchar(255)` | `'John Snow'`      | `true`   | `false` |
| `studentName` | `STUDENT_NAME`   | `varchar(50)`  | *(none specified)* | `false`  | `false` |

JPA will throw an error if `studentName` is null during persist
(because of `nullable = false` and no default value is specified).

## Question 3
> What would be the default column names for the columns below?
> ```java
> @Column
> private String firstName;
> 
> @Column
> private String operatingSystem;
> ```

| Field             | Column Name       |
|-------------------|-------------------|
| `firstName`       | `firstName`       |
| `operatingSystem` | `operatingSystem` |


## Question 4
> Layers in Spring Boot applications

| Layer          | Key Annotation                       | Responsibility                 |
|----------------|--------------------------------------|--------------------------------|
| Presentation   | `@Controller`, `@RestController`     | Handle HTTP requests/responses |
| Service        | `@Service`                           | Business logic                 |
| Data Access    | `@Repository`                        | Database operations            |
| Model/Entity   | `@Entity`                            | Represents database tables     |
| DTO (Optional) | Plain POJO classes (no specific one) | Data transfer between layers   |


## Question 5
