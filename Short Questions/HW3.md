# Homework 3
**Author: M.Y. Yang**

## Question 2
> *Thread-safe singleton class*

See [HW2 Question9 - Singleton](HW2.md#singleton)

## Question 3
> *Ways to create threads*

### 1. Using `Thread`
#### Extend `Thread`
```java
class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("Extend Thread");
    }
}

public class Main {
    public static void main(String[] args) {
        Thread t = new MyThread();
        t.start();
    }
}
```

### 2. Using `Runnable` (usually with `Thread`)
#### Implement `Runnable`
```java
class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Implement Runnable");
    }
}

public class Main {
    public static void main(String[] args) {
        Thread t = new Thread(new MyRunnable());
        t.start();
    }
}
```

#### With lambda
```java
public class Main {
    public static void main(String[] args) {
        Thread t = new Thread(() -> System.out.println("Runnable & Lambda"));
        t.start();
    }
}
```

<details>
<summary>Anonymous Class (replaceable by lambda)</summary>

```java
public class Main {
    public static void main(String[] args) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                System.out.println("Runnable & Anonymous Class");
            }
        };
        new Thread(task).start();
    }
}
```

</details>


### 3. Using `Callable` (usually with `ExecutorService`)
#### Implement `Callable`
```java
import java.util.concurrent.*;

class MyCallable implements Callable<String> {
    @Override
    public String call() {
        return "Implement Callable";
    }
}

public class Main {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        
        Future<String> result = executor.submit(new MyCallable());
        
        System.out.println(result.get());
        
        executor.shutdown();
    }
}
```

#### With lambda
```java
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Callable<String> task = () -> "Callable & Lambda";

        Future<String> future = executor.submit(task);
        
        System.out.println(future.get());
        
        executor.shutdown();
    }
}
```

<details>
<summary>Anonymous Class (replaceable by lambda)</summary>

```java
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Callable<String> task = new Callable<>() {
            @Override
            public String call() {
                return "Callable & Anonymous Class";
            }
        };

        Future<String> future = executor.submit(task);

        System.out.println(future.get());

        executor.shutdown();
    }
}
```

</details>

### 4. Using Thread Pool (`ExecutorService`)
#### With `Runnable` & Lambda

Output is **out of order**.

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3); // 3 threads

        for (int i = 1; i <= 5; i++) {
            int taskId = i; // effectively final for lambda or anonymous class
            executor.submit(() -> {
                System.out.println("Runnable Task #" + taskId + " is running");
            });
        }

        executor.shutdown();
    }
}
```

#### With `Callable` & Lambda

Output is **in order** because of `future.get()`.

```java
import java.io.OutputStream;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(3); // 3 threads

        for (int i = 1; i <= 5; i++) {
            int taskId = i;
            Callable<String> task = () -> "Callable Task #" + taskId + " is completed";
            Future<String> future = executor.submit(task);
            System.out.println(future.get());
        }

        executor.shutdown();
    }
}
```


## Question 4
> *`Runnable` vs `Callable`*

| Feature                 | `Runnable`                                   | `Callable<V>`                      |
|-------------------------|----------------------------------------------|------------------------------------|
| **Return Value**        | Does **not return** a result                 | **Returns** a result of type `V`   |
| **Exception Handling**  | Can**not** throw checked exceptions          | Can throw checked exceptions       |
| **Method to Implement** | `void run()`                                 | `V call()`                         |
| **Used With**           | `Thread`, `ExecutorService.submit(Runnable)` | `ExecutorService.submit(Callable)` |


## Question 5
> *`start()` vs `run()`*

| Aspect                | `start()` | `run()`                     |
|-----------------------|-----------|-----------------------------|
| Creates new thread    | Yes       | No                          |
| Executes concurrently | Yes       | No (runs in current thread) |


## Question 6
> *Which is the better way to create a thread: `Thread` or `Runnable`?*

The `Runnable` interface is generally the preferred approach due to its flexibility, reusability, and compatibility with modern concurrency utilities like `ExecutorService`.

| **Aspect**                | **`Thread` Class**                                 | **`Runnable` Interface**                                                     |
|---------------------------|----------------------------------------------------|------------------------------------------------------------------------------|
| **Thread Management**     | Manages its own thread                             | Requires a `Thread` or `ExecutorService` to manage the thread                |
| **Task Logic and Thread** | Both task logic and thread management are coupled  | Task logic is separated from thread management                               |
| **Reusability**           | Can only extend `Thread`, reducing flexibility     | Can be reused with `Thread`, `ExecutorService`, etc.                         |
| **Usage**                 | Suitable for simple tasks or quick implementations | Preferred for complex tasks, scalability, and when task separation is needed |


## Question 7
> *Thread states*

| **State**         | **Description**                                                                 |
|-------------------|---------------------------------------------------------------------------------|
| **NEW**           | Thread is created but not yet started.                                          |
| **RUNNABLE**      | Thread is ready to run or is currently executing.                               |
| **BLOCKED**       | Thread is waiting for a lock to enter a synchronized block.                     |
| **WAITING**       | Thread is waiting indefinitely for another thread to perform a specific action. |
| **TIMED_WAITING** | Thread is waiting for a specified amount of time.                               |
| **TERMINATED**    | Thread has finished execution and cannot be restarted.                          |


## Question 8
> *Deadlock and possible solutions*

Deadlock describes a situation where two or more threads are blocked forever, waiting for each other.

### Example
```java
public class DeadlockDemo {

    private static final Object resource1 = new Object();
    private static final Object resource2 = new Object();

    public static void main(String[] args) {
        Thread thread1 = new Thread(new Task1());
        Thread thread2 = new Thread(new Task2());

        thread1.start();
        thread2.start();
    }

    static class Task1 implements Runnable {
        @Override
        public void run() {
            synchronized (resource1) {
                System.out.println("Thread 1: Locked resource 1");
                
                try { Thread.sleep(100); } catch (InterruptedException e) {}

                synchronized (resource2) { // stuck here
                    System.out.println("Thread 1: Locked resource 2");
                }
            }
        }
    }

    static class Task2 implements Runnable {
        @Override
        public void run() {
            synchronized (resource2) {
                System.out.println("Thread 2: Locked resource 2");
                
                try { Thread.sleep(100); } catch (InterruptedException e) {}

                synchronized (resource1) { // stuck here
                    System.out.println("Thread 2: Locked resource 1");
                }
            }
        }
    }
}
```

#### Solution 1: Lock Ordering

Ensure that all threads acquire locks in the same order. This avoids circular waiting, which is the cause of deadlock.

```java
public class DeadlockSolution {

    private static final Object resource1 = new Object();
    private static final Object resource2 = new Object();

    public static void main(String[] args) {
        Thread thread1 = new Thread(new Task1());
        Thread thread2 = new Thread(new Task2());

        thread1.start();
        thread2.start();
    }

    static class Task1 implements Runnable {
        @Override
        public void run() {
            synchronized (resource1) {
                System.out.println("Thread 1: Locked resource 1");

                try { Thread.sleep(100); } catch (InterruptedException e) {}

                synchronized (resource2) {
                    System.out.println("Thread 1: Locked resource 2");
                }
            }
        }
    }

    static class Task2 implements Runnable {
        @Override
        public void run() {
            synchronized (resource1) { // Lock resource1 first to prevent deadlock
                System.out.println("Thread 2: Locked resource 1");

                try { Thread.sleep(100); } catch (InterruptedException e) {}

                synchronized (resource2) {
                    System.out.println("Thread 2: Locked resource 2");
                }
            }
        }
    }
}
```

#### Solution 2: Using `tryLock()`

`tryLock()` attempts to acquire the lock without blocking indefinitely. If it cannot acquire the lock, the thread moves on and prints a message, preventing the deadlock situation.

```java
import java.util.concurrent.locks.ReentrantLock;

public class DeadlockSolutionWithTryLock {

    private static final ReentrantLock lock1 = new ReentrantLock();
    private static final ReentrantLock lock2 = new ReentrantLock();

    public static void main(String[] args) {
        Thread thread1 = new Thread(new Task1());
        Thread thread2 = new Thread(new Task2());

        thread1.start();
        thread2.start();
    }

    static class Task1 implements Runnable {
        @Override
        public void run() {
            boolean gotLock1 = false;
            boolean gotLock2 = false;

            try {
                gotLock1 = lock1.tryLock();
                Thread.sleep(50); // small pause to increase contention chance
                gotLock2 = lock2.tryLock();

                if (gotLock1 && gotLock2) {
                    System.out.println("Thread 1: Locked both resources");
                } else {
                    System.out.println("Thread 1: Could not acquire both locks");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (gotLock1) lock1.unlock();
                if (gotLock2) lock2.unlock();
            }
        }
    }

    static class Task2 implements Runnable {
        @Override
        public void run() {
            boolean gotLock1 = false;
            boolean gotLock2 = false;

            try {
                gotLock2 = lock2.tryLock();
                Thread.sleep(50);
                gotLock1 = lock1.tryLock();

                if (gotLock1 && gotLock2) {
                    System.out.println("Thread 2: Locked both resources");
                } else {
                    System.out.println("Thread 2: Could not acquire both locks");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (gotLock1) lock1.unlock();
                if (gotLock2) lock2.unlock();
            }
        }
    }
}
```
Expected outputs (**no** deadlock in all cases):

| Scenario                                | Output                                                                               |
|-----------------------------------------|--------------------------------------------------------------------------------------|
| Thread 1 acquires both locks            | `Thread 1: Locked both resources`<br>`Thread 2: Could not acquire both locks`        |
| Thread 2 acquires both locks            | `Thread 2: Locked both resources`<br>`Thread 1: Could not acquire both locks`        |
| Both acquire one, fail on the other     | `Thread 1: Could not acquire both locks`<br>`Thread 2: Could not acquire both locks` |
| One gets a lock, the other gets nothing | Either both fail, or one may succeed later                                           |
| Both fail to get any locks              | `Thread 1: Could not acquire both locks`<br>`Thread 2: Could not acquire both locks` |


## Question 9
> *Communication between threads*

### Use `Synchronized` and `wait()`, `notify()`, `notifyAll()`
```java
import java.util.*;

public class ProducerConsumerExample {
    public static void main(String[] args) {
        MessageBuffer buffer = new MessageBuffer();

        Thread producerThread = new Thread(() -> {
            for (String item : new String[]{"apple", "banana", "cherry"}) {
                buffer.produce(item);
                sleep(500); // delay (simulate processing)
            }
            buffer.finish(); // signal no more items will be produced
        });

        Thread consumerThread = new Thread(() -> {
            while (true) {
                String item = buffer.consume();
                if (item == null) break; // end of stream
                sleep(300);
            }
        });

        producerThread.start();
        consumerThread.start();
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}

class MessageBuffer {
    private final List<String> buffer = new ArrayList<>();
    private boolean finished = false;

    public synchronized void produce(String item) {
        buffer.add(item);
        System.out.println("Produced: " + item);
        notify(); // wake up consumer if waiting
    }

    public synchronized String consume() {
        while (buffer.isEmpty() && !finished) {
            try {
                System.out.println("Consumer waiting...");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }

        if (buffer.isEmpty() && finished) {
            System.out.println("Consumer exiting.");
            return null;
        }

        String item = buffer.remove(0);
        System.out.println("Consumed: " + item);
        return item;
    }

    public synchronized void finish() {
        finished = true;
        notifyAll(); // wake up any consumers that are waiting
    }
}
```

### Use  `Lock` and `Condition`
```java
import java.util.*;
import java.util.concurrent.locks.*;

public class ProducerConsumerWithLock {
    public static void main(String[] args) {
        MessageBuffer buffer = new MessageBuffer();

        Thread producerThread = new Thread(() -> {
            for (String item : new String[]{"apple", "banana", "cherry"}) {
                buffer.produce(item);
                sleep(500);
            }
            buffer.finish(); // signal that no more items will be produced
        });

        Thread consumerThread = new Thread(() -> {
            while (true) {
                String item = buffer.consume();
                if (item == null) break;
                sleep(300);
            }
        });

        producerThread.start();
        consumerThread.start();
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}

class MessageBuffer {
    private final List<String> buffer = new ArrayList<>();
    private final Lock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    private boolean finished = false;

    public void produce(String item) {
        lock.lock();
        try {
            buffer.add(item);
            System.out.println("Produced: " + item);
            notEmpty.signal(); // wake one waiting consumer
        } finally {
            lock.unlock();
        }
    }

    public String consume() {
        lock.lock();
        try {
            while (buffer.isEmpty() && !finished) {
                System.out.println("Consumer waiting...");
                notEmpty.await();
            }

            if (buffer.isEmpty() && finished) {
                System.out.println("Consumer exiting.");
                return null;
            }

            String item = buffer.remove(0);
            System.out.println("Consumed: " + item);
            return item;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } finally {
            lock.unlock();
        }
    }

    public void finish() {
        lock.lock();
        try {
            finished = true;
            notEmpty.signalAll(); // wake all waiting consumers
        } finally {
            lock.unlock();
        }
    }
}
```

### Use `volatile`
```java
class VolatileExample {
    private static volatile boolean running = true;

    public static void main(String[] args) {
        Thread worker = new Thread(() -> {
            System.out.println("Thread: Started working...");
            while (running) {
                // loop until flag changes
            }
            System.out.println("Thread: Detected stop signal!");
        });

        worker.start();

        try { Thread.sleep(100); } catch (InterruptedException e) {}

        running = false; // visible to the thread immediately
        System.out.println("Main: Set running = false");
    }
}
```


## Question 10
> *Class lock vs Object lock*

| Aspect               | Object Lock (`this`)              | Class Lock (`MyClass.class`)           |
|----------------------|-----------------------------------|----------------------------------------|
| Applies To           | One object instance               | The entire class (all instances)       |
| Used In              | `synchronized` instance methods   | `synchronized` static methods          |
| Lock Target          | `this`                            | `MyClass.class`                        |
| Shared Between       | Threads accessing same object     | All threads accessing class-level lock |


## Question 11
> *`join()`*

The `join()` method in Java is used to **pause the current thread** until the thread on which `join()` is called completes its execution.


## Question 12
> *`yield()`*

| Behavior                    | Description                                                                                          |
|-----------------------------|------------------------------------------------------------------------------------------------------|
| `Thread.yield()`            | Hints to the thread scheduler to allow other threads of the same priority to run (**no guarantee**). |
| When it **might not** yield | If no other threads of equal or higher priority exist, the current thread might continue running.    |

```java
public class YieldExample {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                System.out.println("Thread 1: " + i);
                Thread.yield(); // Suggest yielding to other threads
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                System.out.println("Thread 2: " + i);
                Thread.yield(); // Suggest yielding to other threads
            }
        });

        t1.start();
        t2.start();
    }
}

// might get interleaving output or one thread after the other
```


## Question 13
> *What is a thread pool?*
> 
> *What is the task queue in a thread pool?*
> 
> *Types of thread pools.*

### What is a Thread Pool?
A thread pool in Java is a collection of worker threads that are managed by the `ExecutorService` framework.
The purpose of a thread pool is to **manage** and **reuse** a fixed number of threads to execute a large number of tasks.

### Task queue
A task queue in a thread pool refers to the queue that holds tasks that are waiting to be executed.
When a task is submitted to the `ExecutorService`, it is placed in the task queue, and worker threads take tasks from the queue and execute them.

### Types of Thread Pools
| **Type**                   | **Method**                            | **Thread Count**          | **Task Handling**                               | **Use Case**                                  |
|----------------------------|---------------------------------------|---------------------------|-------------------------------------------------|-----------------------------------------------|
| **Fixed Thread Pool**      | `Executors.newFixedThreadPool(n)`     | Fixed                     | Extra tasks are queued                          | Stable number of concurrent tasks             |
| **Cached Thread Pool**     | `Executors.newCachedThreadPool()`     | Dynamic (grows as needed) | Tasks executed immediately if threads available | Many short-lived asynchronous tasks           |
| **Single Thread Executor** | `Executors.newSingleThreadExecutor()` | 1                         | Tasks are queued and executed sequentially      | Ensures tasks execute one at a time, in order |
| **Scheduled Thread Pool**  | `Executors.newScheduledThreadPool(n)` | Fixed                     | Supports delayed and periodic execution         | Timer, scheduled tasks                        |


## Question 14
> *Which library is used to create a thread pool?*
> 
> *Which interface provides the main functionality of thread pools?*

`java.util.concurrent` is the standard library used to create and manage thread pools.

The key interface that provides the core thread pool functionality is `ExecutorService`.


## Question 15
> *How to submit a task to a thread pool?*

See [Question 3 - Using Thread Pool](#4-using-thread-pool-executorservice)


## Question 16
> *Advantages of thread pools*

1. **Improved Performance**

    Thread pools reuse threads, reducing the overhead of creating and destroying threads repeatedly.

2. **Better Resource Management**

    Thread pools can limit the number of threads to avoid system overload or spawn threads dynamically.

3. **Task Queueing**

    Tasks are queued and executed when threads are available, ensuring organized task handling.

4. **Simplified Thread Management**

    Thread lifecycle management is handled automatically, reducing manual effort in managing threads.

5. **Supports Advanced Features**

    Thread pools support advanced features like `Future` and `Callable` for asynchronous task execution and results.

6. **Graceful Shutdown**

   Thread pools can be shut down cleanly using `shutdown()` or `shutdownNow()`, ensuring no resource leaks.


## Question 17
> *`shutdown()` vs `shutdownNow()`*

| **Method**          | **Description**                                                  | **Running Tasks**          | **Waiting Tasks**                                  |
|---------------------|------------------------------------------------------------------|----------------------------|----------------------------------------------------|
| **`shutdown()`**    | Initiates graceful shutdown, no new tasks are accepted.          | Will complete execution.   | Will finish once running tasks complete.           |
| **`shutdownNow()`** | Immediately attempts to stop tasks, returns tasks still waiting. | Interrupted (if possible). | Returned as a `List<Runnable>` of remaining tasks. |


## Question 18
> *What are atomic classes?*
> 
> *Types of atomic classes.*
> 
> *Code examples with some main methods.*

### What Are Atomic Classes?
Atomic classes are part of the `java.util.concurrent.atomic` package, and they provide thread-safe operations for primitive types and object references.

### Types of Atomic Classes
| **Class**                    | **Description**                                                |
|------------------------------|----------------------------------------------------------------|
| `AtomicBoolean`              | Provides an atomic `boolean` value.                            |
| `AtomicInteger`              | Provides an atomic `int` value.                                |
| `AtomicLong`                 | Provides an atomic `long` value.                               |
| `AtomicIntegerArray`         | Provides an atomic array of `int` values.                      |
| `AtomicLongArray`            | Provides an atomic array of `long` values.                     |
| `AtomicReference<T>`         | Provides an atomic reference to an object of type `T`.         |
| `AtomicReferenceArray<T>`    | Provides an atomic array of references to objects of type `T`. |
| `AtomicMarkableReference<T>` | Provides an atomic reference with a markable boolean flag.     |
| `AtomicStampedReference<T>`  | Provides an atomic reference with a stamp (version number).    |

<details>
<summary>Code Examples with Some Main Methods</summary>

#### Primitive Type Example - `AtomicInteger`
```java
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerExample {
public static void main(String[] args) {
AtomicInteger atomicInt = new AtomicInteger(0);

        // Increment by 1
        atomicInt.incrementAndGet();
        System.out.println("After increment: " + atomicInt.get());

        // Decrement by 1
        atomicInt.decrementAndGet();
        System.out.println("After decrement: " + atomicInt.get());

        // Add a value (e.g., 10)
        atomicInt.addAndGet(10);
        System.out.println("After adding 10: " + atomicInt.get());

        // Set a new value
        atomicInt.set(50);
        System.out.println("After setting 50: " + atomicInt.get());
    }
}
```

#### Reference Type Example - `AtomicReference`
```java
import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceExample {
    public static void main(String[] args) {
        AtomicReference<String> atomicReference = new AtomicReference<>("Initial");

        // Get current value
        System.out.println("Current value: " + atomicReference.get());

        // Compare and set new value if current value matches
        boolean isUpdated = atomicReference.compareAndSet("Initial", "Updated");
        System.out.println("Was the value updated? " + isUpdated);
        System.out.println("Current value after update: " + atomicReference.get());
    }
}
```
</details>


## Question 19
> *Concurrent Collections*

| **Concurrent Data Structure** | **Description**                                                                                                                                                                                       |
|-------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `ConcurrentHashMap`           | A thread-safe variant of `HashMap`. Allows concurrent read and write operations on different segments without locking the entire map.                                                                 |
| `CopyOnWriteArrayList`        | A thread-safe version of `ArrayList` where mutative operations create a copy of the underlying array to ensure consistency.                                                                           |
| `BlockingQueue`               | Interface for thread-safe queues that support blocking operations like `take()`, `put()`, `poll()`, and `offer()`. Includes `ArrayBlockingQueue`, `LinkedBlockingQueue`, and `PriorityBlockingQueue`. |
| `LinkedBlockingQueue`         | A blocking queue with an optional capacity limit that blocks threads when the queue is full or empty.                                                                                                 |
| `ArrayBlockingQueue`          | A blocking queue with a fixed capacity. Operations block when the queue is full or empty.                                                                                                             |
| `ConcurrentLinkedQueue`       | A non-blocking, thread-safe queue that uses lock-free algorithms for basic operations like `add` and `remove`.                                                                                        |
| `ConcurrentSkipListMap`       | A thread-safe variant of `TreeMap` that uses a skip list for efficient ordered operations.                                                                                                            |
| `ConcurrentSkipListSet`       | A thread-safe variant of `TreeSet` implemented using a skip list, ensuring ordered and concurrent access.                                                                                             |


## Question 20
> *Types of locks and their advantages.*

| **Lock Type**       | **Description**                                                                                                                                                                   | **Advantages**                                                                                                                                                                                                                                                                                   |
|---------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Reentrant Lock**  | A lock that allows the thread holding it to re-enter and acquire the lock again. It provides methods like `lock()`, `unlock()`, and `tryLock()`.                                  | • Fairness (can ensure the longest waiting thread gets the lock).<br>• **Prevents deadlock when a thread needs to acquire the lock multiple times.**<br>• Non-blocking lock attempts using `tryLock()`.                                                                                          |
| **Read/Write Lock** | A lock that allows multiple threads to read concurrently but ensures only one thread can write at a time. `ReentrantReadWriteLock` is a typical implementation.                   | • Allows **concurrent reads** for high-performance read-heavy workloads.<br>• Ensures exclusive access for write operations.<br>• Increased throughput for reads.<br>• Fairness can be enabled, preventing thread starvation.                                                                    |
| **Stamped Lock**    | A lock that provides three modes: write, read, and optimistic read. Optimistic reads do not require locking unless a write occurs. It is designed for high-performance scenarios. | • **Optimistic reads** to **avoid locking** and improve **performance**.<br>• Non-blocking reads with `tryOptimisticRead()`.<br>• Better scalability for read-heavy workloads.<br>• Reduces contention by allowing optimistic reads.<br>• Provides traditional write locks for exclusive access. |


## Question 21
> *`Future` vs `CompletableFuture`*
> 
> *Main methods for `CompletableFuture`*

`Future` is an **interface** that represents the result of an asynchronous computation.
It provides methods for checking if the computation is complete, waiting for its completion, and retrieving the result.

`CompletableFuture` is an implementing **class** of `Future` that supports a wide range of asynchronous operations, error handling, and task composition.

<details>
<summary>Main Methods</summary>

| **Method**                                                        | **Description**                                                                                                                |
|-------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------|
| `supplyAsync(Supplier<U> supplier)`                               | Starts an asynchronous task that returns a result (`U`). Returns a `CompletableFuture<U>`.                                     |
| `runAsync(Runnable runnable)`                                     | Starts an asynchronous task that does not return a result. Returns a `CompletableFuture<Void>`.                                |
| `thenApply(Function<T, U> fn)`                                    | Returns a new `CompletableFuture` that applies the given function to the result of the original task.                          |
| `thenAccept(Consumer<T> action)`                                  | Returns a new `CompletableFuture` that executes the provided action on the result of the original task.                        |
| `thenRun(Runnable action)`                                        | Returns a new `CompletableFuture` that runs the provided action when the original task is completed.                           |
| `thenCombine(CompletableFuture<U> other, BiFunction<T, U, V> fn)` | Combines the results of two `CompletableFuture`s using the provided function. Returns a `CompletableFuture<V>`.                |
| `thenCompose(Function<T, CompletableFuture<U>> fn)`               | Returns a new `CompletableFuture` that applies a function to the result of the current task and flattens the result.           |
| `allOf(CompletableFuture<?>... cfs)`                              | Returns a new `CompletableFuture<Void>` that completes when all of the given `CompletableFuture`s complete.                    |
| `anyOf(CompletableFuture<?>... cfs)`                              | Returns a new `CompletableFuture<Object>` that completes when any of the given `CompletableFuture`s completes.                 |
| `exceptionally(Function<Throwable, ? extends T> fn)`              | Handles exceptions by applying a function and returning a new result if the task completes exceptionally.                      |
| `handle(BiFunction<T, Throwable, U> fn)`                          | Handles both normal and exceptional completion. Returns a new `CompletableFuture<U>` after processing the result or exception. |
</details>


## Question 23


## Question 24


## Question 25
