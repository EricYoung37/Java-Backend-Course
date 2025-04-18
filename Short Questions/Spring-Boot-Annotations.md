# Spring Boot Annotations
**Author: M.Y. Yang**

## Basic Concepts
### Inversion of Control (IoC)
- Inversion of Control (IoC) in Spring is a design principle where the control of **object creation and lifecycle management is transferred** from the programmer to the Spring framework.

### Spring Container
- The Spring container, a core component of the Spring Framework, manages the lifecycle and dependencies of **application objects**, known as **beans**.
- It uses **dependency injection** (DI) to assemble these components, promoting **loose coupling** and modularity.
- The container reads configuration **metadata**, which can be in XML, Java annotations, or Java code, to understand how to instantiate, configure, and wire beans together.
- Spring **Application Context** represents the Spring IoC container.

### Spring Boot Annotations
- Spring Boot annotations are a form of **metadata** that provide instructions to the **Spring container** on how to handle **classes** and **methods**.
- They **simplify configuration** and reduce boilerplate code.



## 1. Core Configuration

### ◆ `@SpringBootApplication`
Combination of `@SpringBootConfiguration`, `@EnableAutoConfiguration` and `ComponentScan`.
- `@SpringBootConfiguration`: annotated **class** provides Spring Boot **application** `@Configuration` (app-level config).
- `@EnableAutoConfiguration`: enables autoconfiguration of the Spring Application Context to **provide possibly needed beans** based on the classpath (how to use sub-configs).
- `@ComponentScan`: configures component scanning directives for use with `@Configuration` classes (what sub-configs to use).

```java
package com.chuwa.redbook; // pay attention to the location

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RedbookApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedbookApplication.class, args);
	}

}
```


### ◆ `@Configuration`
Annotated **class** declares one or more `@Bean` methods.


### ◆ `@Bean`

Annotated **method** produces a bean managed by the Spring container.

Used with `@Configuration` or `Component`.

```java
package com.chuwa.redbook.config; // pay attention to the folder

import org.modelmapper.ModelMapper; // 3rd-party package
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfig {
    
    // Register a ModelMapper instance as a Bean into the IoC container
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
```


### ◆ `@Autowired`
Marks a **constructor**, **field**, **setter** method, or **config method** as to be autowired by Spring's **dependency injection** facilities.
```java
package com.chuwa.redbook.controller;

import org.springframework.beans.factory.annotation.Autowired;
// import ...

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    @Autowired
    private PostService postService;
    
    // ...
}
```

```java
package com.chuwa.redbook.service.impl; // pay attention to the impl location

import org.springframework.beans.factory.annotation.Autowired;
// import ...

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    // ...
}
```

## 2. Web Layer (REST API)

### ◆ `@RestController`
### ◆ `@RequestMapping`
### ◆ `@PostMapping`
### ◆ `@GetMapping`
### ◆ `@PutMapping`
### ◆ `@DeleteMapping`
### ◆ `@Valid`
### ◆ `@RequestBody`
### ◆ `@RequestParam`
### ◆ `@PathVariable`


## 3. Service Layer

### ◆ `@Service`


## 4. Persistence Layer (JPA/Hibernate)

### ◆ `@Entity`
### ◆ `@Table`
### ◆ `@UniqueConstraint`
### ◆ `@Id`
### ◆ `@GeneratedValue`
### ◆ `@Column`
### ◆ `@OneToMany`
### ◆ `@ManyToOne`
### ◆ `@JoinColumn`
### ◆ `@CreationTimestamp`
### ◆ `@UpdateTimestamp`

### ◆ `@Repository`


## 5. Exception Handling

### ◆ `@ControllerAdvice`
### ◆ `@ExceptionHandler`
### ◆ `@ResponseStatus`


------------


**References**
- [Spring Boot API](https://docs.spring.io/spring-boot/api/java/index.html)
- [gindex/spring-boot-annotation-list](https://github.com/gindex/spring-boot-annotation-list)
- [CTYue/springboot-redbook](https://github.com/CTYue/springboot-redbook/blob/07_01_validation/src/main/java/com/chuwa/redbook/RedbookApplication.java)