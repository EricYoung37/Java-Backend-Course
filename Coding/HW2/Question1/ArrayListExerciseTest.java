import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.*;

/**
 * @author M.Y.
 * @date 4/11/25
 */
public class ArrayListExerciseTest {
    /**
     * List<E> ls = new ArrayList<>();
     * add
     * get
     * size
     * addAll(anotherList)
     */
    @Test
    public void learn_Inserting_And_Retrieving() {
        // add used in createArrayList
        int minElt = 5, maxElt = 11;
        List<Integer> list = createArrayList(minElt, maxElt);

        // size
        int oldSize = list.size();
        assertEquals(maxElt - minElt + 1, oldSize);

        // get
        assertEquals(maxElt, list.get(oldSize - 1));

        // addAll
        list.addAll(Arrays.asList(3, 2, 1));
        int newSize = list.size();
        assertEquals(Arrays.asList(3, 2, 1), list.subList(oldSize, newSize));
    }

    /**
     * Remove:
     * remove(int index)
     * remove(Object o)
     * * protected removeRange(int fromIndex, int toIndex)
     * removeAll(Collection<?> c)
     * clear()
     *
     * Update:
     * set(int index, E e)
     * replaceAll(UnaryOperator<E> operator)
     *
     * Check:
     * contains(Object o)
     * indexOf(Object o)
     * lastIndexOf(Object o)
     */
    @Test
    public void learn_Remove_Replacing_Updating() {
        int minElt = 5, maxElt = 11;
        List<Integer> list = createArrayList(minElt, maxElt);

        // remove(idx)
        int mid = list.size() / 2; // 3
        int removedElt = list.remove(mid); // 8
        assertEquals(minElt + mid, removedElt);

        // [5, 6, 7, 9, 10, 11]
        // remove(obj)
        assertTrue(list.remove((Integer) 9));
        // list.remove(9); gives IndexOutOfBoundsException
        // because 9 is treated as index instead of object

        // [5, 6, 7, 10, 11]
        // removeAll
        mid = list.size() / 2; // 2
        list.removeAll(list.subList(mid, list.size()));
        assertEquals(Arrays.asList(5, 6), list);

        // clear
        list.clear();
        assertTrue(list.isEmpty());


        for (int i = minElt; i <= maxElt; i++)
            list.add(i);

        // set
        mid = list.size() / 2; // 3
        list.set(mid, mid / 2);
        list.set(mid + mid / 2, list.get(mid)); // [5, 6, 7, 1, 1, 10, 11]
        assertEquals(mid / 2, list.get(mid));

        // replaceAll
        list.replaceAll(e -> -e * 2);
        assertEquals(Arrays.asList(-10, -12, -14, -2, -2, -20, -22), list);

        // contains
        assertTrue(list.contains(-20));

        // indexOf
        assertEquals(mid, list.indexOf(-2));

        // lastIndexOf
        assertEquals(mid + mid / 2, list.lastIndexOf(-2));
    }

    /**
     * iterator()
     * hasNext()
     * next()
     * remove()
     * forEachRemaining(Consumer<? super E> action) -- from Java8
     */

    @Test
    public void learn_Iterator() {
        int minElt = 5, maxElt = 11;
        List<Integer> list = createArrayList(minElt, maxElt);

        Iterator<Integer> iterator = list.iterator();

        // hasNext
        assertTrue(iterator.hasNext());

        // remove
        int step = 2;
        for (int i = 0; i < step; i++)
            iterator.next();
        iterator.remove();
        assertEquals(minElt + step, list.get(1));

        // forEachRemaining
        List<Integer> newList = new ArrayList<>();
        iterator.forEachRemaining(newList::add);
        assertEquals(Arrays.asList(7, 8, 9, 10, 11), newList);
    }

    /**
     * sort(Comparator<? super E> c)
     * Collections.sort(List<T> t)
     * Comparator.reverseOrder()
     */

    @Test
    public void learn_Sorting() {
        int minElt = 7, maxElt = 11;
        List<Integer> list = createArrayList(minElt, maxElt);

        list.sort((a, b) -> b - a); // decreasing order
        assertEquals(Arrays.asList(11, 10, 9, 8, 7), list);

        Collections.sort(list); // increasing order
        assertEquals(Arrays.asList(7, 8, 9, 10, 11), list);

        list.sort(Comparator.reverseOrder()); // decreasing order
        assertEquals(Arrays.asList(11, 10, 9, 8, 7), list);
    }

    private static ArrayList<Integer> createArrayList(int minElt, int maxElt) {
        ArrayList<Integer> list = new ArrayList<>();

        if (minElt <= maxElt)
            for (int i = minElt; i <= maxElt; i++)
                list.add(i);

        return list;
    }
}
