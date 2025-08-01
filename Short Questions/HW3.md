# Homework 3 — Java 8
**Author: M. Yang**

## Question 5
> Best Practices to Prevent `NullPointerException` (NPE)

## Question 6
> Main New Features in Java 8
> - `default` & `static` methods in interfaces
> - Functional Interface & Lambda Expression
> - Method Reference
> - `Optional` Class
> - Stream API

### 1. `default` and `static` methods in interfaces
Java 8 allows interfaces to have `default` methods with implementations,
and also `static` **utility** methods.

```java
interface MyInterface {
    default void greet() {
        System.out.println("Hello from default method!");
    }

    static void info() {
        System.out.println("Static method in interface");
    }
}

class MyClass implements MyInterface {}

public class Main {
    public static void main(String[] args) {
        MyClass obj = new MyClass();
        obj.greet();               // calls default method
        MyInterface.info();        // calls static method
    }
}
```

<details>
<summary>Benefits of having `default` methods in interfaces</summary>

- Adding methods to interfaces **does not require** the implementing classes to **override** them.

- Enable code reuse without forcing use of abstract classes.

    Interfaces with `default` methods allow a class to inherit behavior from multiple sources,
which abstract classes cannot provide.
    ```java
    interface A {
        default void hello() {
            System.out.println("Hello from A");
        }
    }
    
    interface B {
        default void hello() {
            System.out.println("Hello from B");
        }
    }
    
    class C implements A, B {
        @Override
        public void hello() {
            A.super.hello(); // Explicit resolution
        }
    }
    ```
</details>

<details>
<summary>Benefits of having `static` methods in interfaces</summary>

- Group related helper methods with the interface.
    
    Previously, utility methods had to be placed in separate utility classes (like `Collections` for `Collection` interface).
    Now, utility methods can live inside the interface, making APIs more cohesive and discoverable.

</details>


### 2. Functional Interface & Lambda Expression
A functional interface is an interface with a **single abstract method** (SAM).
Lambda expressions provide **a concise way to implement** such interfaces.

```java
@FunctionalInterface
interface MathOperation {
    int operate(int a, int b);  // Single Abstract Method
}

public class Main {
    public static void main(String[] args) {
        MathOperation add = (a, b) -> a + b;  // Lambda expression
        System.out.println(add.operate(5, 3)); // Output: 8
    }
}
```

<details>
<summary>Anonymous class is less concise than lambda expression</summary>

```java
@FunctionalInterface
interface MathOperation {
    int operate(int a, int b);
}

public class Main {
    public static void main(String[] args) {
        MathOperation add = new MathOperation() {
            @Override
            public int operate(int a, int b) {
                return a + b;
            }
        };
        System.out.println(add.operate(5, 3)); // Output: 8
    }
}
```
</details>


### 3. Method Reference
A shorthand for calling a method using `::` operator — often used in place of lambda
when method already exists.

#### ◆ `ClassName::staticMethod`
**Use case:** When referring to a static method of a class.
```java
import java.util.function.Function;

public class Example {
    public static int square(int x) {
        return x * x;
    }

    public static void main(String[] args) {
        Function<Integer, Integer> func = Example::square;
        System.out.println(func.apply(5)); // Output: 25
    }
}
```

#### ◆ `object::instanceMethod`
**Use case:** When referring to a method of a specific object.
```java
import java.util.function.Supplier;

public class Example {
    public String sayHello() {
        return "Hello";
    }

    public static void main(String[] args) {
        Example ex = new Example();
        Supplier<String> supplier = ex::sayHello;
        System.out.println(supplier.get()); // Output: Hello
    }
}
```

#### ◆ `ClassName::instanceMethod`
**Use case:** When the method will be called on a parameter passed to the functional interface.
```java
import java.util.function.Function;

public class Example {
    public static void main(String[] args) {
        Function<String, Integer> func = String::length;
        System.out.println(func.apply("abcde")); // Output: 5
    }
}
```

#### ◆ `ClassName::new`
**Use case:** When referring to a constructor to create new objects.
```java
import java.util.function.Supplier;

class Example {
    public Example() {
        System.out.println("Constructor called");
    }

    public static void main(String[] args) {
        Supplier<Example> supplier = Example::new;
        supplier.get(); // Output: Constructor called
    }
}
```


### 4. `Optional` Class
Introduced to avoid `null` checks and `NullPointerException` (NPE).

```java
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        Optional<String> name = Optional.of("Alice");

        // Check and get value
        name.ifPresent(n -> System.out.println("Hello, " + n));
        System.out.println(name.orElse("Unknown"));
    }
}
```


### 5. Stream API
A functional-style approach to process collections (filtering, mapping, reducing, etc.)

```java
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("apple", "banana", "apricot");

        list.stream()
            .filter(s -> s.startsWith("a"))
            .map(String::toUpperCase)
            .forEach(System.out::println);
        
        /*output:
        APPLE
        APRICOT*/
    }
}
```

## Question 7
> Advantages of `Optional` Class

See [Question 6](#4-optional-class).

## Question 8
> Explain functional interface and lambda expression with code.

See [Question 6](#2-functional-interface--lambda-expression).

## Question 9
> Explain method reference with code.

See [Question 6](#3-method-reference).

## Question 10
> Lambda can use unchanged variable outside of lambda. Explain this with code.

In Java, a variable used inside a lambda must be **effectively final**, meaning its value (for primitives)
or reference (for objects) must not change after (not even after the lambda expression).

1. ✅ Primitive variable is effectively `final`
    ```java
    public class PrimitiveFinal {
        public static void main(String[] args) {
            int x = 5;  // effectively final
    
            Runnable r = () -> System.out.println("x = " + x);  // OK
    
            r.run();  // Output: x = 5
        }
    }
    ```
   
2. ✅ Object reference is effectively final, **internal state is mutable**
    ```java
    import java.util.*;
    
    public class ObjectStateMutable {
        public static void main(String[] args) {
            List<String> names = new ArrayList<>();  // effectively final
            names.add("Alice");
    
            Runnable r = () -> names.add("Bob");  // OK: modifying internal state
    
            r.run();
            System.out.println(names);  // Output: [Alice, Bob]
        }
    }
    ```
   
3. ❌ Primitive variable is reassigned
    ```java
    public class PrimitiveReassigned {
        public static void main(String[] args) {
            int x = 10;
    
            Runnable r = () -> System.out.println("x = " + x);  // ❌ Error
    
            x = 20;  // Reassignment → x is no longer effectively final
    
            r.run();
        }
    }
    ```
   
4. ❌ Object reference is reassigned
    ```java
    public class ReferenceReassigned {
        public static void main(String[] args) {
            String msg = "Hello";
    
            Runnable r = () -> System.out.println(msg);  // compile-time error if reassigned
    
            msg = "Hi";  // Error: msg is no longer effectively final
    
            r.run();
        }
    }
    ```


## Question 11
> Functional Interface Inheritance

A functional interface can extend **one or more** interfaces **only if**
the resulting interface has a **single abstract method**.

1. ✅ Valid Case
    ```java
    interface A {
        void execute();
    }
    
    interface B {
        default void log() {
            System.out.println("Logging from B");
        }
    }
    
    @FunctionalInterface
    interface C extends A, B {
        // Inherits only one abstract method: execute()
        // log() is default, so it doesn't count
    }
    ```

2. ❌ Invalid Case
    ```java
    interface A {
        void methodA();
    }
    
    interface B {
        void methodB();
    }
    
    @FunctionalInterface
    interface C extends A, B {
        // Compilation error: inherits 2 abstract methods
    }
    ```


## Question 12
> Intermediate Operations & Terminal Operations in Stream API

### Intermediate Operations
- **Definition:** Operations that return a new stream. They are **lazy**, meaning they are not executed until a terminal operation is invoked.
- **Purpose:** Used to transform or filter the elements of a stream.
- **Examples:** `filter`, `map`, `sorted`, `distinct`, `limit`, `skip`, etc.

### Terminal Operations
- **Definition:** Operations that produce a result (e.g., value, collection, side effect) and terminate the stream pipeline.
- **Purpose:** **Trigger** the evaluation of the stream and produce the final output.
- **Examples:** `collect`, `forEach`, `reduce`, `count`, `anyMatch`, `allMatch`, `findFirst`, etc.


## Question 13
> Common Intermediate Operations in Stream API

See [Stream-API-Practices](https://github.com/ZahidFKhan/Streams-API-Practices).


## Question 14
> `Collection` vs. `Stream`

| Aspect          | **Collection**     | **Stream**                                    |
|-----------------|--------------------|-----------------------------------------------|
| Storage         | Stores elements    | Does not store elements                       |
| Lifetime        | Long-lived         | Short-lived (created for pipeline processing) |
| Operation Style | External iteration | Internal (lazy) iteration                     |
| Functional API  | No                 | Yes                                           |


## Question 15
> Implementing Stream API's `filter` & `map`

```java
@FunctionalInterface
interface MyPredicate<T> {
    boolean test(T t);
}

@FunctionalInterface
interface MyFunction<T, R> {
    R apply(T t);
}
```
```java
import java.util.ArrayList;
import java.util.List;

public class MyStream<T> {

    private final List<T> data;

    private MyStream(List<T> data) {
        this.data = data;
    }

    public static <T> MyStream<T> of(List<T> data) {
        return new MyStream<>(data);
    }

    public MyStream<T> filter(MyPredicate<T> predicate) {
        List<T> result = new ArrayList<>();
        for (T element : data) {
            if (predicate.test(element)) {
                result.add(element);
            }
        }
        return new MyStream<>(result);
    }

    public <R> MyStream<R> map(MyFunction<T, R> mapper) {
        List<R> result = new ArrayList<>();
        for (T element : data) {
            result.add(mapper.apply(element));
        }
        return new MyStream<>(result);
    }

    public List<T> collect() {
        return data;
    }
}
```
```java
import java.util.Arrays;
import java.util.List;

public class StreamExample {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");

        List<String> result = MyStream.of(names)
                .filter(name -> name.length() > 3)
                .map(name -> name.toUpperCase())
                .collect();

        System.out.println(result); // Output: [ALICE, CHARLIE, DAVID]
    }
}
```