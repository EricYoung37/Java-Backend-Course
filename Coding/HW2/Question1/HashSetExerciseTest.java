import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.*;

/**
 * @author M.Y.
 * @date 4/11/25
 */
public class HashSetExerciseTest {
    /**
     * e.g.
     * Set<E> set= new HashSet<>();
     *
     * add(E e)
     * addAll(Collection<> c)
     *
     * contains()
     *
     * remove(Object o)
     * clear()
     *
     * isEmpty()
     */

    @Test
    public void learn_Inserting_And_Retrieving_Removing() {
        Set<Integer> set = new HashSet<>();

        // add
        for (int i = 4; i <= 6; i++) set.add(i);
        assertEquals(new HashSet<>(Arrays.asList(5, 6, 4)), set);

        // addAll
        set.addAll(Arrays.asList(7, 5, 4)); // only 7 will be added
        assertEquals(new HashSet<>(Arrays.asList(5, 7, 6, 4)), set);

        // contains
        assertTrue(set.contains(5));

        // remove
        assertTrue(set.remove(4));

        // clear & isEmpty
        set.clear();
        assertTrue(set.isEmpty());
    }
}
