import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author M.Y.
 * @date 4/11/25
 */
public class CopyOnWriteArrayListExerciseTest {

    /**
     * e.g.
     * List<E> list = new CopyOnWriteArrayList<>();
     *
     * add(E e)
     * add(int index, E element)
     * addAll(Collection c)
     * addIfAbsent(E e)
     * addAllAbsent(Collection c)
     */
    @Test
    public void learn_Inserting_And_Retrieving() {
        List<Integer> list = new CopyOnWriteArrayList<>();

        // add
        for (int i = 4; i <= 6; i++) list.add(i);
        assertEquals(Arrays.asList(4, 5, 6), list);

        // addAll
        list.addAll(Arrays.asList(8, 9));
        assertEquals(Arrays.asList(4, 5, 6, 8, 9), list);


        // newList declared as CopyOnWriteList to use addIfAbsent and addAllAbsent
        CopyOnWriteArrayList<Integer> newList = new CopyOnWriteArrayList<>(list);

        // addIfAbsent (append)
        newList.addIfAbsent((Integer) 7);
        assertEquals(Arrays.asList(4, 5, 6, 8, 9, 7), newList);

        // addAllAbsent (append)
        newList.addAllAbsent(Arrays.asList(8, 9, 10)); // only 10 will be added
        assertEquals(Arrays.asList(4, 5, 6, 8, 9, 7, 10), newList);
    }

    /**
     * iterator()
     * hasNext()
     * next()
     * *remove() throws UnsupportedOperationException with iterator on CopyOnWriteArrayList
     */

    @Test
    public void learn_Iterator() {
        List<String> list = new CopyOnWriteArrayList<>();
        list.add("Apple");
        list.add("Banana");
        list.add("Orange");

        Iterator<String> itr = list.iterator();

        // hasNext
        assertTrue(itr.hasNext());

        // next
        assertEquals("Apple", itr.next());
    }
}
