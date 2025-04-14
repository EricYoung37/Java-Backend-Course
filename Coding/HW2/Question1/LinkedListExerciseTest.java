import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.*;

/**
 * @author M.Y.
 * @date 4/11/25
 */
public class LinkedListExerciseTest {

    /**
     * e.g.
     * List<E> list = new LinkedList<>();
     *
     * Inserting:
     * add(E e) or addLast(E e)
     * addFirst(E e)
     * add(int index, E element)
     * addAll(Collection c)
     * addAll(int index, Collection c)
     *
     * Retrieving:
     * getFirst()
     * getLast()
     * get(int index)
     *
     */
    @Test
    public void learn_Inserting_And_Retrieving() {
        int minElt = 4, maxElt = 5;
        List<Integer> list = createLinkedList(minElt, maxElt);

        // add
        list.add(7);
        assertEquals(Arrays.asList(4, 5, 7), list);

        // addLast, addFirst used in method createLinkedList

        // add(idx, elt)
        list.add(2, 6);
        assertEquals(Arrays.asList(4, 5, 6, 7), list);

        // addAll(c)
        list.addAll(Arrays.asList(10, 11));
        assertEquals(Arrays.asList(4, 5, 6, 7, 10, 11), list);

        // addAll(idx, c)
        list.addAll(4, Arrays.asList(8, 9));
        assertEquals(Arrays.asList(4, 5, 6, 7, 8, 9, 10, 11), list);

        // get
        int mid = list.size() / 2;
        assertEquals(8, list.get(mid));


        // newList declared as LinkedList to use getFirst & getLast
        LinkedList<Integer> newList = createLinkedList(minElt, maxElt);

        // getFirst
        assertEquals(4, newList.getFirst());

        // getLast
        assertEquals(5, newList.getLast());
    }

    /**
     * removeFirst()
     * removeLast()
     * remove(int index)
     * remove(Object o)
     * removeLastOccurrence()
     *
     * sort()
     */

    @Test
    public void learn_Remove_Sort() {
        int minElt = 4, maxElt = 9;
        LinkedList<Integer> list = createLinkedList(minElt, maxElt);

        // removeFirst
        assertEquals(minElt, list.removeFirst());

        // removeLast
        assertEquals(maxElt, list.removeLast());

        // [5, 6, 7, 8]
        // remove(idx)
        assertEquals(7, list.remove(2));

        // [5, 6, 8]
        // remove(obj)
        assertTrue(list.remove((Integer) 6));
        // list.remove(6); gives IndexOutOfBoundsException
        // because 6 is treated as index instead of object

        // [5, 8]
        // removeLastOccurrence
        list.addFirst(5);
        assertTrue(list.removeLastOccurrence(5));
        assertEquals(0, list.lastIndexOf(5));

        // sort
        list.addLast(9);
        list.sort((a, b) -> b - a); // decreasing
        assertEquals(Arrays.asList(9, 8, 5), list);
    }

    private static LinkedList<Integer> createLinkedList(int minElt, int maxElt) {
        LinkedList<Integer> list = new LinkedList<>();

        // verbose code in order to use addFirst and addLast
        if (minElt == maxElt) {
            list.addFirst(minElt);
        }
        else if (minElt < maxElt) {
            list.addFirst(minElt);
            for (int i = minElt + 1; i <= maxElt; i++) {
                list.addLast(i);
            }
        }

        return list;
    }

    @Test
    public void testCreateLinkedList() {
        // minElt < maxElt
        int minElt = 4, maxElt = 6;
        List<Integer> list = createLinkedList(minElt, maxElt);
        assertEquals(Arrays.asList(4, 5, 6), list);

        // minElt == maxElt
        maxElt = minElt;
        list = createLinkedList(minElt, maxElt);
        assertEquals(List.of(4), list);

        // minElt > maxElt
        maxElt--;
        list = createLinkedList(minElt, maxElt);
        assertTrue(list.isEmpty());
    }
}
