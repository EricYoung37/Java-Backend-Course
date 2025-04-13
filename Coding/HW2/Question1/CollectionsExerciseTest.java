import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * @author M.Y.
 * @date 4/11/25
 */
public class CollectionsExerciseTest {

    /**
     * Collections.min(Collection coll, Comparator comp)
     *
     * Collections.max(Collection coll, Comparator comp)
     *
     * Collections.frequency(Collection coll, object o)
     */
    @Test
    public void learn_common_collections_operations() {
        List<Integer> numbers = Arrays.asList(5, 4, 1, 4, 9, 2, 9, 4);

        // Collections.max
        assertEquals(9, Collections.max(numbers));

        // Collections.min
        assertEquals(1, Collections.min(numbers));

        // Collections.frequency
        assertEquals(3, Collections.frequency(numbers, 4));
    }

    /**
     * Collections.synchronizedList(list)
     */
    @Test
    public void learn_thread_safe_ArrayList() throws InterruptedException {
        List<Integer> synchronizedList = Collections.synchronizedList(new ArrayList<>());

        int numberOfThreads = 5;
        int elementsPerThread = 10;
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        Runnable task = () -> {
            for (int i = 0; i < elementsPerThread; i++) {
                synchronizedList.add(i);
            }
            latch.countDown();
        };

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < numberOfThreads; i++) {
            threads.add(new Thread(task));
        }

        threads.forEach(Thread::start);
        latch.await(); // Wait for all threads to finish

        assertEquals(numberOfThreads * elementsPerThread, synchronizedList.size());
    }
}
