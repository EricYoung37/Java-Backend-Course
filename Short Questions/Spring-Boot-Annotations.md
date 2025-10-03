# Spring Boot Annotations
**Author: M.Y. Yang**

<details>
<summary>Table of Contents</summary>

1. [Basic Concepts](#basic-concepts)
2. [Different Types of Bean Injection](#constructor-injection)
3. [Persistence Layer](#4-persistence-layer-jpahibernate)
4. [Exception Handling](#5-exception-handling)
5. [Aspect-Oriented Programming (AOP)](#6-aspect-oriented-programming)

</details>

## Basic Concepts
### Inversion of Control (IoC)
- Inversion of Control (IoC) in Spring is a design principle where the control of **object creation and lifecycle management is transferred** from the programmer to the Spring framework.

### Dependency Injection (DI)
- Dependency Injection (DI) in Spring Boot is a design pattern where **Spring automatically provides a class with the objects it depends on**, rather than the class creating them itself.

### Spring Container
- The Spring container, a core component of the Spring Framework, manages the lifecycle and dependencies of **application objects**, known as **beans**.
- It uses **dependency injection** to assemble these components, promoting **loose coupling** and modularity.
- The container reads configuration **metadata**, which can be in XML, Java annotations, or Java code, to understand how to instantiate, configure, and wire beans together.
- Spring **Application Context** represents the Spring IoC container.

### Spring Boot Annotations
- Spring Boot annotations are a form of **metadata** that provide instructions to the **Spring container** on how to handle **classes** and **methods**.
- They **simplify configuration** and reduce boilerplate code.

### Classpath
The classpath is essentially the set of locations (**folders** and **JAR files**) where the JVM will look for **compiled `.class` files** and **resources** when running a program.

- **Compiled artifacts:** Java bytecode files (`.class`) generated from source code or libraries.
- **Locations:** Directories containing compiled classes (e.g., `target/classes`) or archive files (e.g., `.jar` files) packaged for distribution.
- **Utilization:** The JVM, through its **class-loading** mechanism, locates and loads these classes into memory for execution.
- `.jar` file = `.class` files + resources (e.g., `.properties`)

## 1. Core Configuration

### ◆ `@SpringBootApplication`
Combination of `@SpringBootConfiguration`, `@EnableAutoConfiguration` and `ComponentScan`.
- `@SpringBootConfiguration`: annotated **class** provides Spring Boot **application** `@Configuration` (app-level config).
- `@EnableAutoConfiguration`: enables auto-configuration of the Spring Application Context to **provide possibly needed beans** based on the classpath (how to use sub-configs).
- `@ComponentScan`: configures component scanning directives for use with `@Configuration` classes (what sub-configs to use).

```java
package com.chuwa.redbook; // pay attention to the location

// imports

@SpringBootApplication
public class RedbookApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedbookApplication.class, args);
	}

}
```


### ◆ `@Component`

Annotated **class** is a candidate for **auto-detection** when using annotation-based configuration and classpath scanning (`@ComponentScan`).

**Specializations:** `@Configuration`, `@Controller`, `@Service`, `@Repository`, `@ControllerAdvice`.


### ◆ `@Configuration`
Annotated **class** declares one or more `@Bean` methods.


### ◆ `@Bean`

Annotated **method** produces a bean managed by the Spring container.

Used with `@Configuration` or `Component`.

```java
package com.chuwa.redbook.config; // pay attention to the folder (package)

import org.modelmapper.ModelMapper; // 3rd-party package
// imports

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

Resolves dependencies **by type**.


#### Field Injection
```java
package com.chuwa.redbook.controller;

// imports

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    @Autowired
    private PostService postService;
    
    // ...
}
```

```java
// pay attention to the implementation and interface locations

package com.chuwa.redbook.service.impl;

import com.chuwa.redbook.service.PostService; // interface

// imports

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    // ...
}
```

#### Setter Injection
```java
// package, imports

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private PostService postService;

    @Autowired
    public void setPostService(PostService postService) {
        this.postService = postService;
    }

    // ...
}
```

#### Constructor Injection
```java
// package, imports

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;
    
    @Autowired // optional if only one constructor (since Spring 4.3)
    public PostController(PostService postService) {
        this.postService = postService;
    }

    // ...
}
```

| **Aspect**        | **Constructor Injection**                                                                                | **Setter Injection**                                                                                              | **Field Injection**                                                                                                                          |
|-------------------|----------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------|
| **Conciseness**   | **Less concise**: Requires explicit constructors with parameters for each dependency.                    | **Moderate**: Requires setter methods but avoids constructor complexity.                                          | **Most concise**: No setters or constructors needed, dependencies injected directly into fields.                                             |
| **Immutability**  | **High**: Once the object is created, its dependencies cannot change.                                    | **Low**: Dependencies can be modified at any time after the object is created.                                    | **Low**: Dependencies can be changed post-construction.                                                                                      |
| **Flexibility**   | **Low**: All dependencies must be provided at construction time, limiting flexibility.                   | **High**: Dependencies can be set anytime after object creation.                                                  | **High**: Dependencies can be set anytime after object creation without any method call.                                                     |
| **Dependencies**  | **Explicit**: Dependencies are required and clearly defined in the constructor.                          | **Implicit**: Dependencies can be set optionally via setters, which may not be immediately obvious.               | **Implicit**: Dependencies are injected directly into fields, making them hidden from the constructor or setter methods.                     |
| **Encapsulation** | **High**: Dependencies are passed via constructor and are not directly accessible after object creation. | **Moderate**: Dependencies can be set or modified after object creation, breaking strict encapsulation.           | **Low**: Dependencies are directly injected into fields, bypassing encapsulation principles.                                                 |
| **Testability**   | **High**: Dependencies can easily be mocked in unit tests, and all required dependencies are clear.      | **Moderate**: Dependencies can be mocked, but there's a risk of uninitialized dependencies if setters are missed. | **Low**: Harder to mock or test effectively, as dependencies are directly injected into fields and may not be visible or easily replaceable. |


#### ◆ `@Resource`

Similar to `@Autowired`, but resolves dependencies **by name** by default.

If no bean with the specified name exists, **fall back** to injection **by type**.

Can**not** be used for constructor injection.

◾ **Default Behavior**
```java
@Component
public class MyService {

    @Resource
    private MyRepository myRepo;  // Injected by name, i.e., myRepo
}
```

◾ **With Explicit Bean Name**
```java
@Component
public class MyService {

    @Resource(name = "myRepositoryA")  // Injects the bean with the name 'myRepositoryA'
    private MyRepository myRepository;
}
```


#### ◆ `@Primary`

Indicates that a bean should be given **preference** when **multiple candidates** are qualified to autowire a single-valued dependency.


#### ◆ `@Qualifier`

Used alongside `@Autowired` to **choose** a bean to inject when **multiple candidates of the same type** exist
(**overrides** `@Primary`).

Used on a **field** or **parameter**.

```java
// service interface

public interface PostService {
    void publishPost();
}
```

```java
// service primary implementation

@Service
@Primary
public class SimplePostService implements PostService {
    @Override
    public void publishPost() {
        System.out.println("Publishing a simple post...");
    }
}
```

```java
// service alternative implementation

@Service("advancedPostService")
public class AdvancedPostService implements PostService {
    @Override
    public void publishPost() {
        System.out.println("Publishing an advanced post with SEO and tags...");
    }
}

```

```java
@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;
    private final PostService advancedPostService;

    @Autowired // optional if only one constructor (since Spring 4.3)
    public PostController(PostService postService,
                          @Qualifier("advancedPostService") PostService advancedPostService) {
        this.postService = postService; // Will get SimplePostService (because it's @Primary)
        this.advancedPostService = advancedPostService;
    }

    @GetMapping("/simple")
    public void postSimple() {
        postService.publishPost();
    }

    @GetMapping("/advanced")
    public void postAdvanced() {
        advancedPostService.publishPost();
    }
}
```


## 2. Web Layer (REST API)

### ◆ `@RestController`

Combination of `Controller` and `ResponseBody`.

- `Controller`: Annotated **class** is a "Controller" (for example, a web controller).
- `ResponseBody`: Indicates a **method return value** should be bound to the web response body.


### ◆ `@RequestMapping`

Maps requests to controllers methods.

Used at the **class level** to express **shared mappings**
or at the **method level** to narrow down to a specific **endpoint mapping** (less preferred).

```java
package com.chuwa.redbook.controller; // pay attention to the folder (package)

// imports

@RestController
@RequestMapping("/api/v1/posts") // shared mappings
public class PostController {
    // ...
}
```


### ◆ `@PostMapping`

Maps HTTP `POST` requests onto specific handler methods.


### ◆ `@Valid`
Marks a property, **method parameter** (typically **DTOs**), or method return type for validation cascading.


### ◆ `@RequestBody`
Binds a method parameter to the request body
(deserialized into an `Object` through an `HTTPMessageReader`).

```java
// package, imports

import com.chuwa.redbook.payload.PostDto; // DTO class in another custom package

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
    
    // postService injection

    @PostMapping()
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto) {
        PostDto postResponse = postService.createPost(postDto);
        return new ResponseEntity<>(postResponse, HttpStatus.CREATED);
    }
}
```


### ◆ `@GetMapping`

Maps HTTP `GET` requests onto specific handler methods.


### ◆ `@RequestParam`

Binds a method parameter to a web request parameter (e.g., query parameter).

```java
// package, imports

import com.chuwa.redbook.util.AppConstants; // AppConstants class in another custom package

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
    
    // postService injection

    @GetMapping()
    public ResponseEntity<List<PostDTO>> getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir
    ) {
        return postService.getAllPost(pageNo, pageSize, sortBy, sortDir);
    }
    
    // example URL: /api/v1/posts?pageNo={pageNo}&sortBy={sortBy}
}
```


### ◆ `@PathVariable`

Binds a method parameter to a **URI template variable**.

```java
// package, imports

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
    
    // postService injection
    
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }
    
    // example URL: /api/v1/posts/{id}
}
```


### ◆ `@PutMapping`

Maps HTTP `PUT` requests onto specific handler methods.

```java
// package, imports

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
    
    // postService injection

    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePostById(@Valid @RequestBody PostDto postDto, @PathVariable(name = "id") long id) {
        PostDto postResponse = postService.updatePost(postDto, id);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }
}
```


### ◆ `@DeleteMapping`

Maps HTTP `DELETE` requests onto specific handler methods.

```java
// package, imports

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
    
    // postService injection

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") long id) {
        postService.deletePostById(id);
        return new ResponseEntity<>("Post entity deleted successfully.", HttpStatus.OK);
    }
}
```


### ◆ `@PatchMapping`

Maps HTTP `PATCH` requests onto specific handler methods.

`@RequestBody` is a **map** for **partial updates** (fields specified by the client).

```java
@PatchMapping("/{id}")
public ResponseEntity<PostDto> partiallyUpdatePost(
        @RequestBody Map<String, Object> updates,
        @PathVariable(name = "id") long id) {
    PostDto updatedPost = postService.partiallyUpdatePost(id, updates);
    return new ResponseEntity<>(updatedPost, HttpStatus.OK);
}
```



## 3. Service Layer

### ◆ `@Service`

```java
// pay attention to the implementation and interface locations

package com.chuwa.redbook.service.impl;

import com.chuwa.redbook.service.PostService; // interface

// imports

@Service
public class PostServiceImpl implements PostService {
    // ...
}
```



## 4. Persistence Layer (JPA/Hibernate)

### ◆ `@Entity`

Annotated class is a JPA entity providing object-relational mapping (ORM).

### ◆ `@Table`

Provides additional configuration for an entity, e.g., table name.

### ◆ `@UniqueConstraint`

Defines unique constraints on table columns.

```java
package com.chuwa.redbook.entity; // pay attention to the folder (package)

// imports

@Entity
@Table(
        name = "posts",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"title"})
        }
)
public class Post {
    // ...
}
```


### ◆ `@Id`

Marks the primary key field.


### ◆ `@GeneratedValue`

Provides generation strategy for a primary key.


### ◆ `@Column`

Provides additional configuration for a field, e.g. column name.

```java
// package, imports

@Entity
@Table(name = "posts" /* other configs */ )
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title", nullable = false)
    private String title;
    
    // ...
}
```


### ◆ `@OneToMany`

Indicates `Current Entity : Annotated Field = 1 : N`.

`mappedBy = {field_owned_by_the_other_entity}`.

In JPA, the side **without** `mappedBy` is the **owning side** that controls the mapping,
i.e., **the side managing the foreign key** (see [`@JoinColumn`](#-joincolumn)).

```java
// package, imports

@Entity
@Table(name = "posts" /* other configs */ )
public class Post {
    
    // ...

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();
    
    // post is a field owned (mapped) by the Comment entity
}
```

**Clarification**
- `Comment` is the owning side (JPA/DB mapping), writing to the join column.
- `Post` is the parent (domain model / lifecycle), managing the children's lifecycle.


### ◆ `cascade`
Defines **which operations on the parent** entity should be **automatically applied to the child** entity.

| CascadeType | Description                                                                        |
|-------------|------------------------------------------------------------------------------------|
| `PERSIST`   | Saves the child entity when the parent is persisted                                |
| `MERGE`     | Updates the child entity when the parent is merged                                 |
| `REMOVE`    | Deletes the child entity when the parent is deleted                                |
| `REFRESH`   | Reloads the child entity when the parent is refreshed from the database            |
| `DETACH`    | Detaches the child entity when the parent is detached from the persistence context |
| `ALL`       | Applies all of the above cascade operations                                        |

Combination is possible, e.g., `cascade = { PERSIST, MERGE }`.


### ◆ `orphanRemoval = true`

If a child entity is **removed** from the parent’s **COLLECTION**,
it should be **deleted from** the **DATABASE** automatically.

**For parent to own full cycle of child, `CascadeType.ALL` is not enough**.
```java
@Entity
public class Author {
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL) // No orphanRemoval
    private List<Book> books = new ArrayList<>();
}

@Entity
public class Book {
    @ManyToOne
    private Author author;
}
```

In this case, if `author.getBooks().remove(book);` is executed, the `Book` entity
removed from the **collection** is **not** removed from the **database**.

However, if **children are shared across parents**,
`CascadeType.REMOVE` and `orphanRemoval = true` should **not** be used.

### ◆ `@ManyToOne`

Indicates `Current Entity : Annotated Field = N : 1`.


### ◆ `@JoinColumn`

Indicates a **foreign key**:
a column (with `name`) in the current entity
refers to a primary key in the reference entity (inferred from the annotated field).

Used with `ManyToOne`, `@OneToOne`, and `@ManyToMany`, `@JoinTable`.

```java
// package, imports

@Entity
@Table(name = "comments")
public class Comment {
    
    // ...

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false) // For JPA, Comment is the owning side.
    private Post post;
}
```


### ◆ `fetch`

- `FetchType.LAZY`: Load the related entity only when it is accessed (**on-demand**).
- `FetchType.EAGER`: Load the related entity **immediately along** with the parent.

#### Default Fetch Types by Relation
Overriding may be needed.

| Relationship Type | Default FetchType |
|-------------------|-------------------|
| `@OneToMany`      | `LAZY`            |
| `@ManyToMany`     | `LAZY`            |
| `@OneToOne`       | `EAGER`           |
| `@ManyToOne`      | `EAGER`           |



### ◆ `@JoinTable`

Specifies an association using a join table.

```java
// public class Current

@ManyToMany
@JoinTable(
        name = "join_table_name",
        joinColumns = @JoinColumn(name = "fk_current"), // FK referencing the current entity
        inverseJoinColumns = @JoinColumn(name = "fk_target") // FK referencing the target entity
)
private Collection<T> targets;
```


### ◆ `@ManyToMany`

Indicates `Current Entity : Annotated Field = N : N`.

```java
@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(
            cascade = CascadeType.ALL
            // fetch = FetchType.LAZY is default
            // orphanRemoval is not supported in ManyToMany
    )
    @JoinTable(
      name = "author_book", 
      joinColumns = @JoinColumn(name = "author_id"), 
      inverseJoinColumns = @JoinColumn(name = "book_id"))
    private Set<Book> books = new HashSet<>();

    // Constructors, getters, setters...
    
    public Author() { }

    public Author(String name) {
        this.name = name;
    }
    
    // the other side has a similar method
    public void addBook(Book book) {
        books.add(book);
        book.getAuthors().add(this);
    }

    // Getters and Setters
}


@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToMany(mappedBy = "books")  // This side doesn't need @JoinTable
    private Set<Author> authors = new HashSet<>();

    // Constructors, getters, setters...

    public Book() { }

    public Book(String title) {
        this.title = title;
    }

    public void addAuthor(Author author) {
        authors.add(author);
        author.getBooks().add(this);
    }

    // Getters and Setters
}
```


### ◆ `@CreationTimestamp`

Marks a property as the creation timestamp of the containing entity.


### ◆ `@UpdateTimestamp`

Marks a property as the update timestamp of the containing entity.

```java
// package, imports

@Entity
@Table(name = "posts" /* other configs */ )
public class Post {
    
    // ...

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;
}
```


### ◆ `@Repository`

Denotes a class as a Data Access Object (DAO) or repository,
primarily used for interacting with a database.

```java
package com.chuwa.redbook.dao; // pay attention to the folder (package)

import com.chuwa.redbook.entity.Post; // entity class in another custom package

// imports

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // no code needed
}
```



## 5. Exception Handling

### ◆ `@ResponseStatus`

Marks a **controller method**, **exception handler method**, or **exception class** with the HTTP status to return, specified via `code()` (alias `value()`) and optionally `reason()`.

#### ◆ On Controller Methods
```java
@PostMapping
@ResponseStatus(HttpStatus.CREATED) // 201
public PostDto createPost(@RequestBody PostDto postDto) {
    return postService.createPost(postDto); // body
}
```

#### ◆ On `@ExceptionHandler` Methods
```java
@ExceptionHandler(ResourceNotFoundException.class)
@ResponseStatus(HttpStatus.NOT_FOUND) // 404
public ErrorDetails handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
    return new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false)); // body
}
```

#### ◆ On Exception Classes
```java
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource Not Found")
// reason will be seen by user, e.g., in Postman
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message); // message stored in JVM / output in IDE logs, not seen by user
    }
}
```
#### ◆ Less Control than `ResponseEntity`
When a method returns a `ResponseEntity`:
- **HTTP status:** The status in `ResponseEntity` always **takes precedence** over `@ResponseStatus`.
- **Reason phrase:** `@ResponseStatus(reason = "...")` is also **ignored**. Spring does not use it if `ResponseEntity` specifies the status.
- **Headers:** Only `ResponseEntity` can carry custom headers; `@ResponseStatus` **cannot**.

```java
@PostMapping()
@ResponseStatus(value = HttpStatus.OK, reason = "This is ignored")
public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto) {
    PostDto postResponse = postService.createPost(postDto);
    return new ResponseEntity<>(postResponse, HttpStatus.CREATED); // this is used
}
```

### ◆ `@ControllerAdvice`

Declares `@ExceptionHandler`, `@InitBinder`, or `@ModelAttribute` methods to be **shared across** multiple `@Controller` classes
(**all by default**).


### ◆ `@ExceptionHandler`

Annotation for handling exceptions in specific handler **classes** and/or handler **methods**.

#### ◆ Inside a Controller
- **Only** handles exceptions thrown **within that specific controller**.
- If the exception occurs **in another controller, it won’t be caught**.

```java
// This example uses both ResponseEntity and @ResponseStatus
@RestController
@RequestMapping("/posts")
public class PostController {

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404
    public ErrorDetails handleNotFound(ResourceNotFoundException ex, WebRequest request) {
        return new ErrorDetails(
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );
    }
}
```

The exception in the `PostService.getPostById` method is **not caught internally** and **propagates** to the `PostController.getPostById` method and gets intercepted.
```java
@Service
public class PostServiceImpl implements PostService {
    // Beans' injection
    @Override
    public PostDto getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        // exception bubbles up to controller
        return modelMapper.map(post, PostDto.class);
    }
}
```

#### ◆ With `@ControllerAdvice`
- Makes the handler **global** — it **intercepts** exceptions from **any controller** in the application.

```java
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFound(ResourceNotFoundException ex,
                                                               WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                                                     request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
}
```

#### Interaction with [Spring AOP](#6-aspect-oriented-programming)
1. `@ExceptionHandler` and `@ControllerAdvice` can handle exceptions that **propagate** from the service layer to the controller layer.
For **exceptions thrown in utility methods or internal logic** within the service layer that are **caught and not re-thrown**, **Spring AOP** provides a better approach for centralized handling (e.g., logging, wrapping, or metrics).
2. When using Spring AOP to intercept service methods called by controllers, exceptions will **only reach the controller if the advice re-throws** them.
If the advice **handles or swallows** the exceptions, they **will not propagate and will not trigger** `@ExceptionHandler` or `@ControllerAdvice`.


## 6. Aspect Oriented Programming

Spring AOP is a Spring Framework feature that **modularizes cross-cutting concerns** like logging, security, and transactions **into reusable aspects**, keeping them separate from core business logic.

### Spring AOP Under the Hood
Consider this example:
```
com.small.backend.orderservice.controller.OrderController // "Injects" OrderService
com.small.backend.orderservice.service.OrderService // Interface
com.small.backend.orderservice.service.impl.OrderServiceImpl // Implementation
```

The flow looks like:
```
Controller
   ↓
Proxy(OrderService)
   ├─ @Before advice runs (before target call)
   ├─ @Around advice (before part)
   ↓
   Target(OrderService.method())
   ↑
   ├─ @AfterReturning advice (if no exception)
   ├─ @AfterThrowing advice (if exception)
   ├─ @Around advice (after part)
   └─ @After advice (always, finally block style)
   ↓
Return to caller
```

- During application context initialization, Spring creates the actual bean instance (e.g., `OrderServiceImpl`)
and then **wraps** that it with a **proxy** —either a JDK dynamic proxy that implements the bean’s interfaces,
or a CGLIB proxy that subclasses the bean.
- The **proxy object** is what gets injected into other beans (e.g., controllers), not the raw bean itself.
- The proxy **intercepts** method calls, **delegates** them to the real bean, and **applies** any matching AOP advice.
- The **timing of advice execution** is determined by annotations like `@Before`, `@After`, etc.

### ◆ `@Aspect`
A modular unit of **a cross-cutting concern**, typically implemented as **a class** annotated with `@Aspect`.

```java
package com.small.backend.orderservice.aop;

@Aspect
@Component
public class LoggingAspect {

    // Matches join points where the **proxy object**
    // itself is an instanceof OrderService.
    // (true for JDK dynamic proxy, since proxy implements the interface)
    @Pointcut("this(com.small.backend.orderservice.service.OrderService)")
    public void proxyImplementsOrderService() {}

    @After("proxyImplementsOrderService()")
    public void logAfterThis() {
        System.out.println("[this] Advice: Proxy implements OrderService interface");
    }

    // Matches join points where the **actual target bean** 
    // is an instanceof OrderService.
    // This works because OrderServiceImpl implements OrderService,
    // so (targetObject instanceof OrderService) == true.
    @Pointcut("target(com.small.backend.orderservice.service.OrderService)")
    public void targetIsOrderService() {}

    @After("targetIsOrderService()")
    public void logAfterTargetInterface() {
        System.out.println("[target] Advice: Target object is an OrderService");
    }

    // Matches join points where the **actual target bean**
    // is an instanceof OrderServiceImpl.
    // This also works because the real bean is exactly of type OrderServiceImpl,
    // so (targetObject instanceof OrderServiceImpl) == true.
    @Pointcut("target(com.small.backend.orderservice.service.impl.OrderServiceImpl)")
    public void targetIsOrderServiceImpl() {}

    @After("targetIsOrderServiceImpl()")
    public void logAfterTarget() {
        System.out.println("[target] Advice: Real bean is OrderServiceImpl");
    }
}
```

### ◆ Join Point
An **implicit** point in the execution of the program, e.g., method execution, constructor call, exception handling.

### ◆ `@Pointcut`
A **predicate** that selects specific join points where an advice should be applied.

#### ◆ `execution`
Matches **method execution** join points based on method signature details (return type, method name, parameters, class, package).

```
execution(                    // designator
    [modifier-pattern]        // optional, public, *
    [return-type-pattern]     // void, *
    [package-pattern]         // may internally include .. to match subpackages
    (. | ..)                  // . = direct, .. = recursive
    [class-pattern]           // *
    .                         // separator
    [method-pattern]          // *
    ([param-pattern])         // .. (any number and type of parameters)
)
```

```java
// void methods in classes whose names start with 'Order' in com.small package or its subpackages
@Pointcut("execution(void com.small..Order*.*(..))")
public void allOrderMethodsReturningVoid() {}
```

#### ◆ `within`
Matches **method executions** join points within certain types (**classes** or **packages**).

**Package/class matching**
```
within(
    [package-pattern]         // may internally include .. to match subpackages
    (. | ..)                  // . = direct, .. = recursive
    [class-pattern]           // *
)
```

```java
// all methods in all classes under com.small.backend.orderservice and its subpackages
@Pointcut("within(com.small.backend.orderservice..*)")
public void allMethodsUnderOrderService() {}
```

**Annotation-type matching**
```
within(
    @[annotation-type]        // matches classes annotated with this annotation
    [class-pattern]           // optional, * = any class with the annotation
)
```

```java
// all methods in any class annotated with @Repository
@Pointcut("within(@org.springframework.stereotype.Repository *)")
public void repositoryClasses() {}
```

#### ◆ `this`
Matches join points where the **AOP proxy object** is an instance of a given type.

#### ◆ `target`
Matches join points where the **actual underlying bean** is an instance of a given type.

#### ◆ `args`
Matches join points based on the **runtime** types of method arguments.

```java
@Pointcut("args(Number, ..)")
public void methodsWithNumberFirstArg() {}

/* // Matches both calls
public void process(Number n) { ... }

process(new Integer(5));  // runtime type = Integer
process(new Double(3.14)); // runtime type = Double
*/
```

#### Combining Pointcut Expressions

```java
/**
 * Matches:
 * 1. All methods in OrderController
 * 2. All methods in OrderServiceImpl
 */
@Pointcut(
    "execution(* com.small.backend.orderservice.controller.OrderController.*(..)) || " +
    "execution(* com.small.backend.orderservice.service.impl.OrderServiceImpl.*(..))"
)
public void controllerAndServiceMethods() {}
```


### ◆ Advice
**Action** taken by an aspect at a particular join point.

An advice can either **reference a `@Pointcut` method** or **use the expressions directly** (delegator) in its `value` or `pointcut` attribute.

#### ◆ `@Before`
Runs before the method execution.

```java
@Pointcut(
        "execution(* com.small.backend.orderservice.controller.OrderController.*(..)) || " +
        "execution(* com.small.backend.orderservice.service.impl.OrderServiceImpl.*(..))"
)
public void controllerAndServiceMethods() {}

@Before("controllerAndServiceMethods()")
public void logBefore() {
    System.out.println("[Before] Method is about to execute");
}

@Before("execution(* com.small.backend.orderservice.controller.OrderController.*(..))")
// Equivalent to:
//@Before(value = "execution(* com.small.backend.orderservice.controller.OrderController.*(..))")
//@Before(pointcut = "execution(* com.small.backend.orderservice.controller.OrderController.*(..))")
public void logBeforeController() {
    System.out.println("[Before] Controller Method is about to execute");
}
```

#### ◆ `@After`
Runs after the method execution (**regardless of outcome**).

```java
@After("controllerAndServiceMethods()")
public void logAfter() {
    System.out.println("[After] Method has executed (finally)");
}
```

#### ◆ `@AfterReturning`
Runs after a method returns successfully.

```java
@AfterReturning(pointcut = "controllerAndServiceMethods()", returning = "result")
public void logAfterReturning(Object result) {
    System.out.println("[AfterReturning] Method returned: " + result);
}

@AfterReturning(
    value = "execution(* com.small.backend.orderservice.controller.OrderController.*(..))",
    returning = "result"
)
public void logAfterControllerReturning(Object result) {
    System.out.println("[AfterReturning] Controller method returned: " + result);
}
```

The `returning` **attribute** specifies the name of the parameter in your **advice** method that will **receive** the **return value** of the **target** method.

#### ◆ `@AfterThrowing`
Runs if a method throws an exception.

```java
@AfterThrowing(pointcut = "controllerAndServiceMethods()", throwing = "ex")
public void logAfterThrowing(Throwable ex) {
    System.out.println("[AfterThrowing] Method threw exception: " + ex.getMessage());
}
```

The `throwing` **attribute** specifies the name of the parameter in your **advice** method that will **receive** the **exception** thrown by the **target** method.

#### ◆ `@Around`
Wraps the method execution (can control whether to proceed).

```java
@Around("controllerAndServiceMethods()")
public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
    System.out.println("[Around-Before] Before method: " + joinPoint.getSignature());
    // a signature object represents the method being intercepted
    // might print
    // void com.small.backend.orderservice.service.impl.OrderServiceImpl.createOrder(String)
    try {
        Object result = joinPoint.proceed();  // target method executes here
        System.out.println("[Around-AfterReturning] Method returned: " + result);
        return result;
    } catch (Throwable ex) {
        System.out.println("[Around-AfterThrowing] Method threw exception: " + ex.getMessage());
        throw ex;
    } finally {
        System.out.println("[Around-Finally] After method (finally block)");
    }
}
```

### ◆ `@Order`

`@Order` defines the priority or precedence of an aspect when multiple aspects apply to the same join point.

```java
@Aspect
@Component
@Order(1)
public class FirstAspect {
    @Before("somePointcut()")
    public void before() {
        System.out.println("1st before advice");
    }
}
```

```java
@Aspect
@Component
@Order(2)
public class SecondAspect {
    @Before("somePointcut()")
    public void before() {
        System.out.println("2nd before advice");
    }
}
```

------------


**References**
- [Spring Boot API](https://docs.spring.io/spring-boot/api/java/index.html)
- [gindex/spring-boot-annotation-list](https://github.com/gindex/spring-boot-annotation-list)
- [CTYue/springboot-redbook](https://github.com/CTYue/springboot-redbook/tree/09_01_AOP)