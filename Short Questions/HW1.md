# Homework 1 - Java OOP
**Author: M.Y. Yang**

## Question 1
> 3/4 Fundamental conecpts of OOP
> * Encapsulation
> * Polymorphism
> * Inheritance

```java
// 1. Encapsulation
class Animal {
    private String name;
    private int age;

    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age >= 0) {
            this.age = age;
        }
    }

    // Method to be overridden (runtime polymorphism)
    public void makeSound() {
        System.out.println("Some generic animal sound");
    }

    // Method overloading (compile-time polymorphism)
    public void makeSound(String mood) {
        System.out.println(name + " makes a sound when it's " + mood);
    }

    public void makeSound(int volumeLevel) {
        System.out.println(name + " makes a sound at volume level " + volumeLevel);
    }
}

// 2. Inheritance
class Dog extends Animal {
    public Dog(String name, int age) {
        super(name, age);
    }

    // Overriding method
    @Override
    public void makeSound() {
        System.out.println(getName() + " says: Woof!");
    }
}

class Cat extends Animal {
    public Cat(String name, int age) {
        super(name, age);
    }

    @Override
    public void makeSound() {
        System.out.println(getName() + " says: Meow!");
    }
}

// Main class
public class Main {
    public static void main(String[] args) {
        Dog dog = new Dog("Buddy", 3);
        Cat cat = new Cat("Whiskers", 2);

        Animal[] animals = new Animal[] { dog, cat };
        
        // test overriding
        for (Animal animal : animals) {
            animal.makeSound();
        }
        
        // test overloading
        dog.makeSound("happy");
        cat.makeSound(5);
    }
}
```


## Question 2
> Wrapper classes

In Java, wrapper classes are object representations of the primitive data types.
Each wrapper class wraps a primitive value inside an object.

| Primitive Type | Wrapper Class |
|----------------|---------------|
| `int`          | `Integer`     |
| `double`       | `Double`      |
| `float`        | `Float`       |
| `char`         | `Character`   |
| `byte`         | `Byte`        |
| `short`        | `Short`       |
| `long`         | `Long`        |
| `boolean`      | `Boolean`     |

### Why Wrapper Classes:
#### 1. To bring object-oriented features to primitives
Java is an object-oriented language, but primitive types (int, double, etc.) are not objects.
Wrapper classes provide a way to treat primitive values as objects, allowing them to:
- Be stored in object-only structures (e.g., Collections)
- Have methods and behavior (like parsing, comparing, converting)

#### 2. To enable uniform treatment of data
Java treats objects and primitives differently in many contexts.
Wrappers allow you to treat primitives uniformly as objects,
simplifying APIs and tools that expect objects.

#### 3. To enable features like nullability
Primitive types can't be null, but objects can.
Wrappers allow you to represent "no value" (null) — useful in databases, APIs, or optional fields.


## Question 3
> `HashMap` **vs** `HashTable`

| Feature                    | `HashMap`                                          | `Hashtable`                                  |
|----------------------------|----------------------------------------------------|----------------------------------------------|
| **Thread Safety**          | ❌ Not synchronized (not thread-safe)               | ✅ Synchronized (thread-safe)                 |
| **Performance**            | ✅ Faster (no synchronization overhead)             | ❌ Slower due to synchronization              |
| **Null Keys/Values**       | ✅ Allows one `null` key and multiple `null` values | ❌ Does **not** allow any `null` key or value |
| **Legacy Status**          | ✅ Part of Java Collections Framework               | ❗ Legacy class (pre-Java 1.2)                |
| **Iterator Type**          | Fail-fast iterator                                 | Not fail-fast (less consistent)              |
| **Preferred for New Code** | ✅ Recommended                                      | ❌ Avoid for new code                         |


## Question 4
> String pool and string immunity

### What is String Pool in Java
The **String Pool** (also called the **String Intern Pool**) is a special memory area inside the **Java heap** that stores **unique String literals**.

Whenever you create a String like this:
```java
String s1 = "hello";
String s2 = "hello";
```
Java does **not** create two objects. Instead, both `s1` and `s2` point to the **same object** in the String pool.

### Why String Pool
#### 1. To save memory
Instead of creating multiple copies of identical strings, Java stores only one copy in the pool.

#### 2. To improve performance
Reusing immutable string objects is faster than creating new ones — especially in applications where strings are used heavily (like web servers, compilers, parsers).

### String Immunity
In Java, String is **immutable**, meaning **once created, it cannot be changed**.

**Benefits:**

#### 1. Thread safety
Immutable objects are inherently thread-safe (no synchronization needed).

#### 2. Security
Strings are often used for passwords, file paths, etc. If they were mutable, they could be altered unexpectedly.

#### 3. Hashing consistency
Since string values don’t change, they make reliable keys in hash-based collections (HashMap, HashSet).

#### 4. String pool depends on it
If strings could change, they can't be reused safely from the pool.


## Question 5
> Garbage collection

Garbage Collection is the process by which the Java Virtual Machine (**JVM**) **automatically reclaims memory** by removing objects that are **no longer reachable or needed**.

**Types of GC:**

| GC Type                            | Description                                                                       |
|------------------------------------|-----------------------------------------------------------------------------------|
| **Serial GC**                      | Single-threaded, simple, good for small apps                                      |
| **Parallel GC**                    | Multi-threaded for both minor and major collections, good throughput              |
| **CMS (Concurrent Mark Sweep) GC** | Collects in phases with minimal pause time, deprecated as of Java 9               |
| **G1 GC (Garbage First)**          | Prioritizes low-pause-time and is good for large heaps; default since Java 9      |
| **ZGC (Z Garbage Collector)**      | Scalable, low-latency GC; supports heaps from MBs to TBs with pauses <10ms        |
| **Shenandoah GC**                  | Also low-pause-time GC, developed by Red Hat; pauses are independent of heap size |


## Question 6
> Access modifiers

| Access Modifier | Scope                                                                                   |
|-----------------|-----------------------------------------------------------------------------------------|
| `public`        | Accessible from **anywhere** (same package or different package).                       |
| `protected`     | Accessible within the **same package** and **subclasses** (even in different packages). |
| `default`       | Accessible only within the **same package** (no modifier specified).                    |
| `private`       | Accessible only within the **same class**.                                              |


## Question 7
> `final` keyword

| Modifier Context     | Purpose                                                          |
|----------------------|------------------------------------------------------------------|
| `final` with Fields  | Makes the field value **constant** after initialization.         |
| `final` with Methods | **Prevents** the method from being **overridden** by subclasses. |
| `final` with Classes | **Prevents** the class from being **extended**.                  |


## Question 8
> `static` keyword

| **Usage**                     | **Description**                                                                                                                          | **Example**                                                                        |
|-------------------------------|------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------|
| **Static Fields (Variables)** | Shared across all instances of the class. Same value for all objects.                                                                    | `static int count = 0;`                                                            |
| **Static Methods**            | Belong to the class itself, not to an instance. Can be called without creating an object.                                                | Define: `public static void displayCount() {}`<br/> Invoke: `Counter.displayCount` |
| **Static Nested Classes**     | A class inside another class that does **not reply on** an instance of the outer class. It can access static members of the outer class. | `static class NestedClass {}`                                                      |


## Question 9
> Overriding **vs** Overloading

| **Feature**           | **Overloading**                                               | **Overriding**                                                                 |
|-----------------------|---------------------------------------------------------------|--------------------------------------------------------------------------------|
| **Method Signature**  | Same method name, but **different parameters (type/number)**. | Same method name, same parameters.                                             |
| **Polymorphism Type** | **Compile-time** polymorphism (resolved at compile time).     | **Runtime** polymorphism (resolved at runtime).                                |
| **Class Context**     | Happens within the **same class**.                            | Happens in **inheritance**.                                                    |
| **Purpose**           | To provide multiple ways to call a method.                    | To customize or modify inherited behavior from a superclass.                   |
| **Return Type**       | **Return type** can be the **same or different**.             | Must have the **same return type**.                                            |
| **Access Modifier**   | **No restriction**.                                           | Access modifier must be the **same or more permissive** in overridden methods. |


## Question 10
> Method signature and polymorphism

Java method signature consists of:
- method name
- parameter types (in order)

| **Overloading**                                                   | **Overriding**     |
|-------------------------------------------------------------------|--------------------|
| Same method name, different param types (**different signature**) | **Same signature** |


## Question 11
> `super` **vs** `this`

| **Keyword** | **Refers To**                  | **Used For**                                                                      | **Context of Use**               |
|-------------|--------------------------------|-----------------------------------------------------------------------------------|----------------------------------|
| `this`      | **Current** class object       | Accessing current class members, resolving naming conflicts, constructor chaining | Within the same class            |
| `super`     | **Parent** (superclass) object | Accessing parent class members or constructors                                    | In a subclass (with inheritance) |


## Question 12
> `equals()` and `hashCode()`

### `equals()`
The default implementation compares memory addresses (i.e., reference equality) of two objects.
If comparison between the object themselves is the desired behavior, then `equals()` must be overridden.

### `hashCode()`
The default implementation returns an integer value for the object as a hash code value (to be used in hash-based collections).

### Working with a `HashMap`
1. Java calls `hashCode()` on the key.
2. It uses the hash code to compute an index (e.g., `hash % array.length`) into an internal bucket array.
3. If no collision: the entry is added.
4. If collision (another key with the same hash code):
   - Java checks `equals()` to determine whether:
     - It's the same key (update the value).
     - A different key (add to the linked list or tree inside the bucket).


## Question 13
> Java load sequence

The Java load sequence (also called the **class loading and initialization sequence**) describes how the **JVM loads, links, and initializes classes** before they are used at runtime.

```text
+---------------------------------------------+
| 1. Loading                                  |
|---------------------------------------------|
| - ClassLoader loads .class file into memory |
| - Class object created                      |
+--------------------------------------------+
        ↓
+--------------------------------------------------+
| 2. Linking                                       |
|--------------------------------------------------|
| a. Verification                                  |
|    - Bytecode is checked for correctness         |
| b. Preparation                                   |
|    - Static fields allocated with default values |
| c. Resolution                                    |
|    - Symbolic references resolved to direct refs |
+--------------------------------------------------+
        ↓
+---------------------------------------+
| 3. Initialization                     |
|---------------------------------------|
| - Static blocks executed              |
| - Static fields assigned real values  |
| - <clinit>() method runs (if present) |
+---------------------------------------+
        ↓
+-----------------------------+
| 4. Instantiation (Optional) |
|-----------------------------|
| - Object is created (heap)  |
| - Constructor is called     |
+-----------------------------+
```

### Triggered When:
- A class is instantiated (`new MyClass()`)
- A static method or variable is accessed
- Reflection is used (`Class.forName("MyClass")`)


## Question 14
> Polymorphism

See questions 1 and 9.


## Question 15
> Encapsulation

Encapsulation refers to bundling data (variables) and methods that operate on that data into a single unit, known as a class.

| **Benefit**                  | **Explanation**                                                      |
|------------------------------|----------------------------------------------------------------------|
| **Data Security**            | Hides internal details, allowing controlled access.                  |
| **Improved Flexibility**     | Internal changes don’t affect external code using the class.         |
| **Better Code Organization** | Keeps related data and methods together, improving readability.      |
| **Validation Control**       | Enforces validation rules on inputs via setters, ensuring integrity. |
| **Ease of Maintenance**      | Facilitates changes and refactoring without breaking external code.  |

For example implementation, see question 1.


## Question 16
> Interface **vs** Abstract Class

| **Feature**          | **Interface**                                                   | **Abstract Class**                                  |
|----------------------|-----------------------------------------------------------------|-----------------------------------------------------|
| **Purpose**          | Defines a contract for implementing classes                     | Define a common base class for related classes      |
| **Methods**          | All methods are implicitly **abstract** (until Java 8)          | Can have both **abstract** and **concrete** methods |
| **Fields**           | Can only have `static final` fields (constants)                 | Can have instance variables (fields)                |
| **Constructor**      | Can**not** have constructors                                    | Can have constructors                               |
| **Access Modifiers** | Methods are `public` by default                                 | Methods can have any access modifiers               |
| **Inheritance**      | A class can implement multiple interfaces                       | A class can only extend one abstract class          |
| **Default Methods**  | From Java 8 onwards, can have **default methods** (with a body) | Can**not** have default methods                     |
