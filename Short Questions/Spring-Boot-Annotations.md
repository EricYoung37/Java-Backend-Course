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

In JPA, the side **without** `mappedBy` is the **owning side** that controls the mapping.

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
    @JoinColumn(name = "post_id", nullable = false)
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

### ◆ `@ControllerAdvice`

Declares `@ExceptionHandler`, `@InitBinder`, or `@ModelAttribute` methods to be **shared across** multiple `@Controller` classes
(**all by default**).


### ◆ `@ExceptionHandler`

Annotation for handling exceptions in specific handler **classes** and/or handler **methods**.

```java
package com.chuwa.redbook.exception;  // pay attention to the folder (package)

// imports

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                        WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(),
                webRequest.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
    
    // ...
}
```


### ◆ `@ResponseStatus`

Marks a **method** or **exception class** with the status `code()` and `reason()` that should be returned.

```java
package com.chuwa.redbook.exception;  // pay attention to the folder (package)

// @ResponseStatus can be removed if @ControllerService is used instead.
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    private String resourceName;
    private String fieldName;
    private long fieldValue;

    public ResourceNotFoundException(String resourceName, String fieldName, long fieldValue) {
        
        // note super() is called
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
        
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
    
    // getters & setters
}
```

```java
// package, imports

import com.chuwa.redbook.exception.ResourceNotFoundException;

@Service
public class PostServiceImpl implements PostService {
    @Override
    public PostDto getPostById(long id) {

        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        return modelMapper.map(post, PostDto.class);
    }
}
```

※ To use `GlobalExceptionHandler` instead, remove `@ResponseStatus`.
The exception will **bubble up** to `@ControllerAdvice`.

------------


**References**
- [Spring Boot API](https://docs.spring.io/spring-boot/api/java/index.html)
- [gindex/spring-boot-annotation-list](https://github.com/gindex/spring-boot-annotation-list)
- [CTYue/springboot-redbook](https://github.com/CTYue/springboot-redbook/tree/07_01_validation/src/main/java/com/chuwa/redbook)