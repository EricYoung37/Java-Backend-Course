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
> *Types of thread pools.*
> 
> *What is the task queue in a thread pool?*