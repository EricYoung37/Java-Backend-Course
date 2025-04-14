import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author M.Y.
 * @date 4/11/25
 */
public class TreeSetExerciseTest {
    /**
     * e.g.
     * Set<E> set= new TreeSet<>();
     *
     * add(E e)
     * addAll(Collection<> c)
     *
     * contains(Object o)
     *
     * first()
     * last()
     * subSet(E fromElement, E toElement)
     * headSet(E toElement)
     * tailSet(E fromElement)
     *
     * remove(Object o)
     *
     * size()
     * isEmpty()
     */

    @Test
    public void learn_Inserting_And_Retrieving_Removing() {
        Set<Integer> set = new TreeSet<>();

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

        // size
        assertEquals(3, set.size());

        // isEmpty
        assertFalse(set.isEmpty());


        // newSet declared as TreeSet to use TreeSet's exclusive methods
        TreeSet<Integer> newSet = new TreeSet<>(set);

        // first
        assertEquals(5, newSet.first());

        // last
        assertEquals(7, newSet.last());

        // headSet (<)
        assertEquals(new TreeSet<>(Arrays.asList(6, 5)), newSet.headSet(7));

        // tailSet (>=)
        assertEquals(new TreeSet<>(Arrays.asList(7, 6, 5)), newSet.tailSet(5));

        // subSet [,)
        assertEquals(new TreeSet<>(List.of(6)), newSet.subSet(6, 7));
    }
}
