# Homework 8 — Spring Data Pt. 2
**Author: M. Yang**

## Question 1
> Spring Boot Annotations Review

See [Spring-Boot-Annotations.md](Spring-Boot-Annotations.md).


## Question 2
> Spring vs. Spring Boot
> 
> Advantages of Spring Boot

| Feature                     | **Spring**                                                                                                                       | **Spring Boot**                                                                                                                                         |
|-----------------------------|----------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Definition/Description**  | A comprehensive framework for building Java applications with support for dependency injection, AOP, and various other features. | A **simplified extension of Spring** that focuses on reducing setup complexity and configuration to make it easier to create Spring-based applications. |
| **Configuration**           | Manual configuration (XML/annotations)                                                                                           | **Auto-configuration** based on dependencies, **opinionated defaults$^{※}$** for common use cases.                                                      |
| **Setup Complexity**        | Requires extensive setup and configuration                                                                                       | **Minimal setup**, defaults provided                                                                                                                    |
| **Deployment**              | Requires separate web server for deployment                                                                                      | **Standalone applications** with embedded web server (e.g., Tomcat)                                                                                     |
| **Deployment Speed**        | More complex to use, requires more effort                                                                                        | **Simplified development**, quick to get started                                                                                                        |
| **Use Case**                | Suitable for large, complex applications                                                                                         | **Ideal for microservices**                                                                                                                             |
| **Embedded Server Support** | Requires an external server (e.g., Tomcat)                                                                                       | **Embedded web server** (e.g., Tomcat, Jetty)                                                                                                           |

※ In software development, particularly with frameworks like Spring Boot, "opinionated" refers to the framework
having predefined configurations, conventions, and default behaviors
that guide developers toward best practices and simplify the development process.


## Question 3
> - Inversion of Control (IOC)
> - Dependency Injection (DI)

See [Spring-Boot-Annotations.md](Spring-Boot-Annotations.md#basic-concepts).


## Question 4
> `@ComponentScan`

See [Spring-Boot-Annotations.md](Spring-Boot-Annotations.md#-springbootapplication).


## Question 5
> `@SpringBootApplication`

See [Spring-Boot-Annotations.md](Spring-Boot-Annotations.md#-springbootapplication).


## Question 6
> Ways to Define a Bean

### Using `@Component` (class-level)

**Specializations:** `@Configuration`, `@Controller`, `@Service`, `@Repository`, `@ControllerAdvice`.

```java
@Component // custom bean name, e.g., via @Component("componentA")
public class MyComponent {
    public void printMessage() {
        System.out.println("This is a general-purpose component.");
    }
}

```

### Using `@Bean` (method-level) within `@Configuration` (class-level)

```java
@Configuration
public class AppConfig {
    @Bean // custom bean name, e.g., @Bean("beanA")
    public MyBean myBean() {
        return new MyBean();
    }
}
```

### Using XML (less common)
```xml
<?xml version="1.0" encoding="UTF-8"?> <!--src/main/resources/applicationContext.xml-->
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
           http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">

    <bean id="myBean" class="com.example.MyBean"/>
</beans>
```

```java
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.example.MyBean;

public class MyBean {}

public class MyApplication {
    public static void main(String[] args) {
        // Load the Spring application context from the XML configuration file
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        // Retrieve a bean from the context
        MyBean myBean = (MyBean) context.getBean("myBean");

        // Use the bean
        myBean.displayMessage();
    }
}

```


## Question 7
> Default and custom bean names with `@Component` and `@Bean`
> 
> `@Component` vs. `@Bean`

- `@Component`: class name with the first letter in lower case or `@Component("customName)`.
- `@Bean`: method name or `@Bean("customName")`.

| Feature                 | `@Component`                                         | `@Bean`                                                                          |
|-------------------------|------------------------------------------------------|----------------------------------------------------------------------------------|
| **Used On**             | Class level                                          | Method level (inside `@Configuration` class)                                     |
| **Automatic Discovery** | **Automatically discovered** via component scanning  | **Not** automatically discovered (must be declared in configuration)             |
| **Customization**       | Limited to constructor/field injection               | **Complete control** over bean creation and configuration                        |
| **Typical Use Case**    | For simple service, repository, or component classes | For complex beans requiring **explicit configuration** or **external libraries** |
| **Scope**               | Default is singleton, but configurable               | Default is singleton, but configurable                                           |

**Example of Accessing Named Beans**

`@Component` and `@Bean` definitions in [Question 6](#question-6).

```java
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {
    public static void main(String[] args) {
        // Initialize the ApplicationContext with the configuration class
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Accessing a bean defined by @Component using its name
        MyService myService = (MyService) context.getBean("componentA");
        myService.performService();

        // Accessing a bean defined by @Bean using its name
        MyBean myBean = (MyBean) context.getBean("beanA");
        myBean.doSomething();
    }
}
```


## Question 8
> `@Component` vs. `@Service`, `@Repository`, `@Controller`

- `@Component`: used for general-purpose Spring bean
- `@Service`, `@Repository`, `@Controller`: specializations of `@Component` used in specific layers of an application.


## Question 9
> - `@Autowired`
> - `@Resource`
> - `@Primary`
> - `@Qualifer`

See [Spring-Boot-Annotations.md](Spring-Boot-Annotations.md#-autowired).


## Question 10
> Annotations for Bean Injection

| Annotation                     | Used On                       | Behavior                                                                         |
|--------------------------------|-------------------------------|----------------------------------------------------------------------------------|
| **`@Autowired`**               | Fields, setters, constructors | Resolves dependencies by type. Can be combined with `@Qualifier` and `@Primary`. |
| **`@Resource`**                | Fields, setters               | Injects by field name (default). Can also specify the name using `name`.         |
| **`@Inject`**                  | Fields, setters, constructors | Similar to `@Autowired`, injects beans by type, but not as flexible.             |
| **`@Value`**                   | Fields, setters               | Inject externalized properties (simple ones).                                    |
| **`@ConfigurationProperties`** | Class, setters                | Inject externalized properties (complex structures).                             |

◾ **`@Value` Example**
```properties
# application.properties
my.property.value=Some value to inject
```

```java
@Value("${my.property.value}")
public void setMyProperty(String myProperty) {
    this.myProperty = myProperty;
}
```

◾ **`ConfigurationProperties` Example**
```properties
# application.properties
app.name=My Application
app.port=8080
```

```java
@Component
@ConfigurationProperties(prefix = "app")
public class AppConfig {
    private String name;
    private int port;

    // Getters and setters
}
```


## Question 11
> Types of Dependency Injection

See [Spring-Boot-Annotations.md](Spring-Boot-Annotations.md#-autowired).


## Question 12
> How Spring container chooses a bean from multiple candidates of the same type.

See [Spring-Boot-Annotations.md](Spring-Boot-Annotations.md#-primary).


## Question 13
> `BeanFactory` vs. `ApplicationContext`

Both are interfaces for accessing a Spring bean container.

| **Feature**                     | **BeanFactory**                                             | **ApplicationContext**                                                                              |
|---------------------------------|-------------------------------------------------------------|-----------------------------------------------------------------------------------------------------|
| **Interface**                   | `BeanFactory`                                               | `ApplicationContext` (**extends** `BeanFactory`)                                                    |
| **Initialization**              | **Lazy** initialization (beans are created on demand)       | **Eager** initialization (beans are created at startup)                                             |
| **Bean Management**             | Basic                                                       | Advanced features like event handling, AOP, i18n, etc.                                              |
| **Event Handling**              | Not supported                                               | Supports event propagation and listener mechanism                                                   |
| **AOP Support**                 | Not supported                                               | Supports Aspect-Oriented Programming (AOP)                                                          |
| **Internationalization (i18n)** | Not supported                                               | Supports `MessageSource` for internationalization                                                   |
| **Common Implementations**      | `XmlBeanFactory` (deprecated), `DefaultListableBeanFactory` | `ClassPathXmlApplicationContext`, `AnnotationConfigApplicationContext`, `GenericApplicationContext` |
| **Use Case**                    | Simple, resource-constrained applications                   | Enterprise-level applications requiring rich functionality                                          |
| **Environment Abstraction**     | Not supported                                               | Supports environment properties and profiles                                                        |

**`ApplicationContext` Examples:**
- [Question 6](#using-xml-less-common)
- [Question 7](#question-7)


## Question 14
> Bean Scopes

The scope of a bean defines the **lifecycle** and **visibility** of the bean **within the IoC container**.

| **Scope**               | **Description**                                                    | **Lifecycle**                                                    | **Use Case**                                                       |
|-------------------------|--------------------------------------------------------------------|------------------------------------------------------------------|--------------------------------------------------------------------|
| **Singleton (default)** | One instance per Spring container.                                 | Created once, shared across the entire container.                | Shared resources or state across the application.                  |
| **Prototype**           | A new instance created for each request from the Spring container. | New instance on each request, no lifecycle management.           | Beans that need to be independent and stateless per request.       |
| **Request**             | One instance per HTTP request.                                     | Created for each HTTP request, destroyed at request end.         | Request-specific data in web applications.                         |
| **Session**             | One instance per HTTP session.                                     | Created at the beginning of a session, destroyed at session end. | User-specific data or state across multiple requests in a session. |
| **Application**         | One instance per ServletContext (entire application).              | Created at application start, destroyed at application shutdown. | Application-wide beans, shared across all sessions and requests.   |
| **WebSocket**           | One instance per WebSocket session.                                | Created for the WebSocket session, destroyed when closed.        | Beans tied to the lifecycle of a WebSocket connection.             |
| **Custom**              | Defined by the user for custom lifecycle management.               | Managed according to custom logic defined by the user.           | Specialized lifecycle management for unique use cases.             |

<details>
<summary>Example of Custom scope</summary>

**◾ Create the Custom Scope Class**
```java
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import java.util.HashMap;
import java.util.Map;

public class MyCustomScope implements Scope {

    // Store the beans in a map (for example purposes)
    private Map<String, Object> beans = new HashMap<>();

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        // Check if the bean already exists, if not, create it
        if (!beans.containsKey(name)) {
            beans.put(name, objectFactory.getObject());
        }
        return beans.get(name);
    }

    @Override
    public Object remove(String name) {
        // Remove the bean from the scope
        return beans.remove(name);
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
        // You can add logic for bean destruction here if needed
    }

    @Override
    public Object resolveContextualObject(String key) {
        // Optional method, you can return some contextual objects here
        return null;
    }

    @Override
    public String getConversationId() {
        // Return a unique ID for the conversation (optional)
        return "myCustomScope";
    }
}
```

**◾ Register the Custom Scope**
```java
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericWebApplicationContext;

@Configuration
public class CustomScopeConfig {

    @Bean
    public static MyCustomScope myCustomScope() {
        return new MyCustomScope();
    }

    @Bean
    public static void registerCustomScope(GenericWebApplicationContext context) {
        context.getBeanFactory().registerScope("myCustomScope", new MyCustomScope());
    }
}
```

**◾ Use the Custom Scope in Beans**
```java
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("myCustomScope")
public class MyCustomScopedBean {

    public MyCustomScopedBean() {
        System.out.println("MyCustomScopedBean created!");
    }

    public void doSomething() {
        System.out.println("Doing something...");
    }
}
```
</details>


## Question 15
> Write a mini Spring Application to demonstrate:
> - bean registration by `@Component` and `@Bean`
> - different bean scopes
> - constructor injection, setter injection, field injection
> - dependency injection by type and by name

See [DI-demo](../Projects/DI-demo).

<details>
<summary>Demo Screenshots</summary>

### Output
![Screenshot 2025-04-27 042812](https://github.com/user-attachments/assets/0a721d96-2c4c-4e94-bfcf-bd9fd2ba2c94)

### Beans Diagram in IntelliJ IDEA
![Screenshot 2025-04-27 042905](https://github.com/user-attachments/assets/7950bdfe-4c39-4ed9-b142-a02cb217881f)

</details>

## Question 16
> Explain the builder pattern with code.

See [HW2.md Question 9](HW2.md#builder).
