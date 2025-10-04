# Homework 4 — Multi-threading
**Author: M. Yang**

<details>
<summary>Table of Contents</summary>

1. [Thread-safe Singleton Class](#question-2)
2. [Thread Creation—4 ways](#question-3)
3. [`Runnable` vs. `Callable`](#question-4)
4. [`start()` vs. `run()`](#question-5)
5. [`Thread` vs. `Runnable` for Thread Creation](#question-6)
6. [Thread States](#question-7)
7. [Deadlock](#question-8)
8. [Communication between Threads](#question-9)
9. [Object Lock vs. Class Lock](#question-10)
10. [`join()` in `java.lang.Thread` & Fork/Join Framework](#question-11)
11. [`yield()`](#question-12)
12. [Thread Pool](#question-13)
13. [`shutdown()` vs. `shutdownNow()`](#question-17)
14. [Atomic Class](#question-18)
15. [Concurrent Collections](#question-19)
16. [Reentrant Lock vs. R/W Lock vs. Stamped Lock](#question-20)
17. [`Future` vs. `CompletableFuture`](#question-21)
18. [Thread Starvation](#question-26)
19. [Reactive Stream](#question-27)

</details>

## Question 2
> Thread-safe singleton class

See [HW2 Question9](HW2.md#singleton)

## Question 3
> Thread Creation
> - Extend `Thread`
> - Implement `Runnable` (usually with `Thread`)
> - Implement `Callable` (usually with `ExecutorService`)
> - ThreadPool (`ExecutorService` with `Runnable` or `Callable`)

### 1. Extend `Thread`
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

### 2. Implement `Runnable` (usually with `Thread`)
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


### 3. Implement `Callable` (usually with `ExecutorService`)
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

### 4. Thread Pool (`ExecutorService`)
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
> `Runnable` vs. `Callable`

| Feature                 | `Runnable`                                   | `Callable<V>`                      |
|-------------------------|----------------------------------------------|------------------------------------|
| **Return Value**        | Does **not return** a result                 | **Returns** a result of type `V`   |
| **Exception Handling**  | Can**not** throw checked exceptions          | Can throw checked exceptions       |
| **Method to Implement** | `void run()`                                 | `V call()`                         |
| **Used With**           | `Thread`, `ExecutorService.submit(Runnable)` | `ExecutorService.submit(Callable)` |


## Question 5
> `start()` vs. `run()`

| Feature            | `run()` | `start()` |
|--------------------|---------|-----------|
| Creates new thread | No      | Yes       |

```java
public class Examples {
    public static void main(String[] args) {
        // 1. run example
        Runnable runTask = () -> {
            System.out.println("Running in: " + Thread.currentThread().getName());
        };
        runTask.run(); // runs in current thread
       
       // 2. start example
       Runnable startTask = () -> {
           System.out.println("Running in: " + Thread.currentThread().getName());
       };
       Thread t = new Thread(startTask);
       t.start(); // runs in new thread
    }
}
```

## Question 6
> `Thread` vs. `Runnable` for Thread Creation

The `Runnable` interface is generally the preferred approach due to its flexibility, reusability, and compatibility with modern concurrency utilities like `ExecutorService`.

| **Aspect**                | **`Thread` Class**                                 | **`Runnable` Interface**                                                     |
|---------------------------|----------------------------------------------------|------------------------------------------------------------------------------|
| **Thread Management**     | Manages its own thread                             | Requires a `Thread` or `ExecutorService` to manage the thread                |
| **Task Logic and Thread** | Both task logic and thread management are coupled  | **Task logic is separated from thread management**                               |
| **Reusability**           | Can only extend `Thread`, reducing flexibility     | Can be reused with `Thread`, `ExecutorService`, etc.                         |
| **Usage**                 | Suitable for simple tasks or quick implementations | Preferred for complex tasks, scalability, and when task separation is needed |


## Question 7
> Thread states

| **State**         | **Description**                                                                 |
|-------------------|---------------------------------------------------------------------------------|
| **NEW**           | Thread is created but not yet started.                                          |
| **RUNNABLE**      | Thread is ready to run or is currently executing.                               |
| **BLOCKED**       | Thread is waiting for a lock to enter a synchronized block.                     |
| **WAITING**       | Thread is waiting indefinitely for another thread to perform a specific action. |
| **TIMED_WAITING** | Thread is waiting for a specified amount of time.                               |
| **TERMINATED**    | Thread has finished execution and cannot be restarted.                          |


## Question 8
> Deadlock

A deadlock occurs when **two or more** threads **waiting for each other** are **blocked forever**.

**Runtime** programmatical detection `ThreadMXBean.findDeadlockedThreads()`.

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

| Scenario                            | Output                                                                               |
|-------------------------------------|--------------------------------------------------------------------------------------|
| Thread 1 acquires both locks        | `Thread 1: Locked both resources`<br>`Thread 2: Could not acquire both locks`        |
| Thread 2 acquires both locks        | `Thread 2: Locked both resources`<br>`Thread 1: Could not acquire both locks`        |
| Both acquire one, fail on the other | `Thread 1: Could not acquire both locks`<br>`Thread 2: Could not acquire both locks` |
| Both acquire two locks              | `Thread 1: Locked both resources` <br> `Thread 2: Locked both resources`             |


## Question 9
> Communication between threads

### Use `synchronized` and `wait()`, `notify()`, `notifyAll()`

See [Question 10](#question-10) for explanation about `synchronized` instance methods (object lock).

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
        notify(); // wake up consumer if waiting (random thread)
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

| **Aspect**      | **Regular Variable**                                     | **`volatile` Variable**                                             | **`synchronized`**                               |
|-----------------|----------------------------------------------------------|---------------------------------------------------------------------|--------------------------------------------------|
| **Visibility**  | No guarantee across threads (**stale** values possible). | Guarantees visibility across threads (always **latest** value).     | Guarantees visibility.                           |
| **Atomicity**   | No guarantee.                                            | No guarantee (only single read/write is safe).                      | Guarantees atomicity for critical sections.      |
| **Ordering**    | Compiler/CPU may **reorder** operations.                 | **Prevents reordering** of reads/writes around `volatile` variable. | Prevents reordering within `synchronized` block. |
| **Performance** | Fastest (no synchronization).                            | Lightweight (cheaper than synchronized, but some overhead).         | Heavier due to locking mechanism.                |
| **Use Case**    | Thread-local or non-concurrent use.                      | Flags, state indicators, single-writer multiple-reader scenarios.   | When atomicity of compound actions is needed.    |


## Question 10
> Object lock vs. Class lock

| Aspect         | Object Lock (`this`)                | Class Lock (`MyClass.class`)           |
|----------------|-------------------------------------|----------------------------------------|
| Applies To     | One object instance                 | The entire class (all instances)       |
| Used In        | `synchronized` **instance methods** | `synchronized` **static methods**      |
| Lock Target    | `this`                              | `MyClass.class`                        |
| Shared Between | Threads accessing same object       | All threads accessing class-level lock |

### Object Lock Example

Only one thread at a time can execute a synchronized instance method on the same object.

```java
class MyClass {
   public synchronized void doSomething1() {
       // method body
   }
   
   public synchronized void doSomething2() {
      // method body
   }
   /*// equivalent to
   public void doSomething2() {
      synchronized (this) {
         // method body
      }
   }*/
}
```

### Class Lock Example

Only one thread at a time can execute any synchronized static method of that class, across all instances.

```java
class MyClass {
    public static synchronized void doStaticWork1() {
        // method body
    }
    
    public static synchronized void doStaticWork2() {
        // method body
    }
    /*// equivalent to
    public static void doStaticWork2() {
        synchronized (MyClass.class) {
            // method body
        }
    }*/
}
```

## Question 11
> `join()` in `java.lang.Thread`

The `join()` method in Java is used to **pause the current thread** until the thread on which `join()` is called completes its execution.

```java
public class JoinExample {
    public static void main(String[] args) {
       Thread t = new Thread(() -> {
          System.out.println("Child thread: Hi");
          try {
             Thread.sleep(2000);  // simulate work
          } catch (InterruptedException e) {
             e.printStackTrace();
          }
          System.out.println("Child thread: Bye");
       });

       t.start();

       System.out.println("Main thread: Hello");  // this runs while t is sleeping

       t.join();  // main thread blocks here until t is done

       System.out.println("Main thread: See ya");
    }
}
```
Output
```
Child thread: Hi
Main thread: Hello
Child thread: Bye
Main thread: See ya
```
Without `join()`, the output is likely to be
```
Child thread: Hi
Main thread: Hello
Main thread: See ya
Child thread: Bye
```

> Fork/Join Framework (since Java 7)
> - Found in `java.util.concurrent.RecursiveTask` and `RecursiveAction`.
> - Enables efficient **divide-and-conquer** parallelism.
> - `fork()` starts a subtask asynchronously.
> - `join()` waits for its completion and gets the result.

```java
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

public class ForkJoinSumExample {

    // RecursiveTask that calculates sum of part of an array
    static class SumTask extends RecursiveTask<Long> {
        private static final int THRESHOLD = 5;
        private int[] nums;
        private int start;
        private int end;

        public SumTask(int[] nums, int start, int end) {
            this.nums = nums;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            if (end - start <= THRESHOLD) {
                // Base case: do it directly
                long sum = 0;
                for (int i = start; i < end; i++) {
                    sum += nums[i];
                }
                return sum;
            } else {
                // Split task
                int mid = (start + end) / 2;
                SumTask leftTask = new SumTask(nums, start, mid);
                SumTask rightTask = new SumTask(nums, mid, end);

                leftTask.fork();                // Start a new thread
                long rightResult = rightTask.compute(); // Compute in current thread
                long leftResult = leftTask.join();      // Join left task

                return leftResult + rightResult;
            }
        }
    }

    public static void main(String[] args) {
        int[] numbers = new int[10];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = i + 1;  // 1 to 10
        }

        ForkJoinPool pool = new ForkJoinPool();
        SumTask task = new SumTask(numbers, 0, numbers.length);
        long result = pool.invoke(task);

        System.out.println("Sum = " + result); // Should print 55
    }
}
```

## Question 12
> `yield()`

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
> What is a thread pool?
> 
> What is the task queue in a thread pool?
> 
> Types of thread pools.

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
> Which library is used to create a thread pool?
> 
> Which interface provides the main functionality of thread pools?

`java.util.concurrent` is the standard library used to create and manage thread pools.

The key interface that provides the core thread pool functionality is `ExecutorService`.


## Question 15
> How to submit a task to a thread pool?

See [Question 3](#4-thread-pool-executorservice)


## Question 16
> Advantages of thread pools

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
> `shutdown()` vs. `shutdownNow()`

| **Method**          | **Description**                                                  | **Running Tasks**          | **Waiting Tasks**                                  |
|---------------------|------------------------------------------------------------------|----------------------------|----------------------------------------------------|
| **`shutdown()`**    | Initiates graceful shutdown, no new tasks are accepted.          | Will complete execution.   | Will finish once running tasks complete.           |
| **`shutdownNow()`** | Immediately attempts to stop tasks, returns tasks still waiting. | Interrupted (if possible). | Returned as a `List<Runnable>` of remaining tasks. |


## Question 18
> What are atomic classes?
> 
> Types of atomic classes.
> 
> Code examples with some main methods.

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
> Concurrent Collections

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
> Types of locks and their advantages.

| **Lock Type**       | **Description**                                                                                                                                                                                      | **Advantages**                                                                                                                                                                                                                                                                                   |
|---------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Reentrant Lock**  | A lock that allows the thread holding it to re-enter and acquire the lock again. It provides methods like `lock()`, `unlock()`, and `tryLock()`.                                                     | • Allows **fairness** via `new ReentrantLock(true)` (ensure the longest waiting thread gets the lock).<br>• **Prevents deadlock when a thread needs to acquire the lock multiple times.**<br>• Non-blocking lock attempts using `tryLock()`.                                                     |
| **Read/Write Lock** | A lock that allows multiple threads to read concurrently but ensures only one thread can write at a time. `ReentrantReadWriteLock` is a typical implementation.                                      | • Allows **concurrent reads** for high-performance read-heavy workloads.<br>• Ensures exclusive access for write operations.<br>• Increased throughput for reads.<br>• Fairness can be enabled, preventing thread starvation.                                                                    |
| **Stamped Lock**    | A lock that provides three modes: write, read, and optimistic read. Optimistic reads do not require locking unless a write occurs. It is designed for high-performance scenarios. **Not reentrant.** | • **Optimistic reads** to **avoid locking** and improve **performance**.<br>• Non-blocking reads with `tryOptimisticRead()`.<br>• Better scalability for read-heavy workloads.<br>• Reduces contention by allowing optimistic reads.<br>• Provides traditional write locks for exclusive access. |


## Question 21
> `Future` vs. `CompletableFuture`

`Future` is an **interface** that represents the result of an asynchronous computation.
It provides methods for checking if the computation is complete, waiting for its completion, and retrieving the result.

`CompletableFuture` is an implementing **class** of `Future` that supports a wide range of asynchronous operations, **error handling**, and **task composition**.

<details>
<summary>Common CompletableFuture Methods</summary>

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

<details>
<summary>Examples</summary>

### Resultful `Future`
```java
public String getFuture() {
   ExecutorService executor = Executors.newSingleThreadExecutor();
   
   try {
      Callable<String> task = () -> {
         Thread.sleep(2000); // Simulate work
         return "Task completed!";
      };

      Future<String> future = executor.submit(task);
      return future.get(); // Blocks until result is ready

   } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
   } finally {
      executor.shutdown();
   }
}
```

### Resultless `CompletableFuture`
```java
public void runCompletableFuture() {
    // Run a task specified by a Runnable Object asynchronously.
    CompletableFuture<Void> future = CompletableFuture.runAsync(new Runnable() {
        @Override
        public void run() {
            // Simulate a long-running Job
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            System.out.println("I'll run in a separate thread than the main thread.");
        }
    });

    // Block and wait for the future to complete
    future.get();
}
```

### Resultful `CompletableFuture`
```java
public String getCompletableFuture() {
   // Run a task specified by a Supplier object asynchronously
   CompletableFuture<String> future = CompletableFuture.supplyAsync(new Supplier<String>() {
      @Override
      public String get() {
         try {
            TimeUnit.SECONDS.sleep(1);
         } catch (InterruptedException e) {
            throw new IllegalStateException(e);
         }
         return "Result of the asynchronous computation";
      }
   });

   // Block and get the result of the Future
   String result = future.get();
   System.out.println(result);
}
```
</details>

## Question 23
> Create two threads. One prints 1, 3, 5, 7, 9, and the other prints 2, 4, 6, 8, 10.
> 
> One solution uses `synchronized`, `wait`, `notify`. The other uses `ReentrantLock`, `Condition`.

### Using `synchronized`, `wait`, `notify`
```java
public class OddEvenSync {
    private final Object lock = new Object();
    private boolean oddTurn = true;

    public void printOdd() {
        for (int i = 1; i <= 9; i += 2) {
            synchronized (lock) {
                while (!oddTurn) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {}
                }
                System.out.println("Odd: " + i);
                oddTurn = false;
                lock.notify();
            }
        }
    }

    public void printEven() {
        for (int i = 2; i <= 10; i += 2) {
            synchronized (lock) {
                while (oddTurn) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {}
                }
                System.out.println("Even: " + i);
                oddTurn = true;
                lock.notify();
            }
        }
    }

    public static void main(String[] args) {
        OddEvenSync obj = new OddEvenSync();
        Thread t1 = new Thread(obj::printOdd);
        Thread t2 = new Thread(obj::printEven);
        t1.start();
        t2.start();
    }
}
```

### Using `ReentrantLock`, `Condition`
```java
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OddEvenLock {
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private boolean oddTurn = true;

    public void printOdd() {
        for (int i = 1; i <= 9; i += 2) {
            lock.lock();
            try {
                while (!oddTurn) {
                    condition.await();
                }
                System.out.println("Odd: " + i);
                oddTurn = false;
                condition.signal();
            } catch (InterruptedException e) {
            } finally {
                lock.unlock();
            }
        }
    }

    public void printEven() {
        for (int i = 2; i <= 10; i += 2) {
            lock.lock();
            try {
                while (oddTurn) {
                    condition.await();
                }
                System.out.println("Even: " + i);
                oddTurn = true;
                condition.signal();
            } catch (InterruptedException e) {
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        OddEvenLock obj = new OddEvenLock();
        Thread t1 = new Thread(obj::printOdd);
        Thread t2 = new Thread(obj::printEven);
        t1.start();
        t2.start();
    }
}
```


## Question 24
> Create three threads. One outputs 1 to 10, one outputs 11 to 20, and one outputs 21 to 22.
> The threads' running sequence is random.

```java
public class ThreeThreads {
    public static void main(String[] args) {
        Thread t1 = new Thread(printRange(1, 10, "Thread 1"));
        Thread t2 = new Thread(printRange(11, 20, "Thread 2"));
        Thread t3 = new Thread(printRange(21, 22, "Thread 3"));

        t1.start();
        t2.start();
        t3.start();
    }

    private static Runnable printRange(int start, int end, String threadName) {
        return () -> {
            for (int i = start; i <= end; i++) {
                System.out.println(threadName + ": " + i);
                try {
                    Thread.sleep(500); // tiny delay
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };
    }
}
```


## Question 25
### Part 1
> Use `CompletableFuture` to asynchronously get the sum and product of two integers,
> and print the results.

```java
import java.util.concurrent.CompletableFuture;

public class AsyncSumProduct {
   public static void main(String[] args) {
      int num1 = 5;
      int num2 = 10;

      System.out.println("Using numbers: " + num1 + " and " + num2);

      CompletableFuture<Integer> sumFuture = CompletableFuture.supplyAsync(() -> sum(num1, num2));
      CompletableFuture<Integer> productFuture = CompletableFuture.supplyAsync(() -> product(num1, num2));

      sumFuture.thenAccept(sum -> {
         System.out.println("Sum: " + sum);
      });

      productFuture.thenAccept(product -> {
         System.out.println("Product: " + product);
      });

      // Wait for both computations to complete before finishing the program
      CompletableFuture.allOf(sumFuture, productFuture).join();
   }

   private static int sum(int a, int b) {
      return a + b;
   }

   private static int product(int a, int b) {
      return a * b;
   }
}
```

### Part 2
> Find a public API with at least three endpoints.
> Use `CompletableFuture` to fetch data from each endpoint and merge the fetched data.
> 
> Using [JSONPlaceholder](https://jsonplaceholder.typicode.com/).

See [Coding/HW3/Question25/src/AsyncJsonFetcher.java](../Coding/HW4/Question25/src/AsyncJsonFetcher.java)


### Part 3
> For part 2, implement exception handling. If an exception occurs during any API call,
> return a default value and log the exception information.

See [Coding/HW3/Question25/src/AsyncJsonFetcher2.java](../Coding/HW4/Question25/src/AsyncJsonFetcher2.java)


## Question 26
> Thread Starvation

Thread starvation occurs when some threads **never get CPU time** because some **other threads keep occupying the CPU**.

### Causes
1. Some threads keep re-enter a `synchronized` block, while other threads remain blocked forever trying to enter the block.
2. The `notify()` method makes **no guarantee about what thread is awakened** if multiple thread have called `wait()`, and some threads remain waiting forever.

### Solutions
1. Use `ReentrantLock` with fairness (`new ReentrantLock(true)`) to ensure the longest-waiting thread gets the lock first.
2. `Semaphore`, `BlockingQueue`, and other concurrency utilities in `java.util.concurrent` also have fair variants.


## Question 27
> Reactive Stream

Reactive stream is a **specification** and programming pattern for handling **asynchronous data streams** with **non-blocking backpressure**.

It is designed to allow a producer of data and a consumer of data to interact in a way that:
- Does not block threads unnecessarily
- Prevents the producer from overwhelming the consumer (via backpressure)
- Works well for infinite or continuous streams of data, not just fixed collections

| Interface         | Purpose                                                            |
|-------------------|--------------------------------------------------------------------|
| `Publisher<T>`    | Emits items to subscribers                                         |
| `Subscriber<T>`   | Receives items from a publisher                                    |
| `Subscription`    | Link between publisher and subscriber; used to request/cancel data |
| `Processor<T, R>` | Acts as both a subscriber and a publisher (for transforming data)  |

<details>
<summary>Example</summary>

```java
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public class ReactiveStreamExample {
   public static void main(String[] args) {
      SubmissionPublisher<String> publisher = new SubmissionPublisher<>();

      Flow.Processor<String, String> processor = new Flow.Processor<>() {
         private Flow.Subscription subscription;
         private final SubmissionPublisher<String> delegate = new SubmissionPublisher<>();

         @Override
         public void subscribe(Flow.Subscriber<? super String> subscriber) {
            delegate.subscribe(subscriber); // forward to delegate publisher
         }

         @Override
         public void onSubscribe(Flow.Subscription subscription) {
            this.subscription = subscription;
            subscription.request(1);
         }

         @Override
         public void onNext(String item) {
            delegate.submit(item.toUpperCase()); // transform & publish
            subscription.request(1);
         }

         @Override
         public void onError(Throwable throwable) {
            delegate.closeExceptionally(throwable);
         }

         @Override
         public void onComplete() {
            delegate.close();
         }
      };

      Flow.Subscriber<String> subscriber = new Flow.Subscriber<>() {
         private Flow.Subscription subscription;

         @Override
         public void onSubscribe(Flow.Subscription subscription) {
            this.subscription = subscription;
            subscription.request(1);
         }

         @Override
         public void onNext(String item) {
            System.out.println("Received: " + item);
            subscription.request(1);
         }

         @Override
         public void onError(Throwable throwable) {
            throwable.printStackTrace();
         }

         @Override
         public void onComplete() {
            System.out.println("Done");
         }
      };

      // Connect: Publisher → Processor → Subscriber
      publisher.subscribe(processor);
      processor.subscribe(subscriber);

      publisher.submit("hello");
      publisher.submit("reactive");
      publisher.submit("stream");

      publisher.close();
   }
}
```
</details>

> `CompletableFuture` vs Reactive Stream

| Feature          | `CompletableFuture`                                   | Reactive Stream                                                          |
|------------------|-------------------------------------------------------|--------------------------------------------------------------------------|
| **Purpose**      | Represents a *single* asynchronous computation result | Represents a *stream* (potentially infinite) of asynchronous data        |
| **Nature**       | One value (or exception)                              | Multiple values over time                                                |
| **Backpressure** | No backpressure (producer can complete immediately)   | Built-in backpressure support                                            |
| **Java Version** | Introduced in Java 8                                  | Standardized in Java 9 (`Flow` API)                                      |
| **Typical Use**  | Async tasks, parallel computations, API calls         | Continuous event/data processing (e.g., Kafka, WebSockets, reactive web) |
