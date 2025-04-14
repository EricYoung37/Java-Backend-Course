import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author M.Y.
 * @date 4/11/25
 */
public class ArraysExerciseTest {

    /**
     * e.g.
     * int[] arr = { x, y, z, ... };
     * int[] arr = new int[length]
     *
     * arr.length
     *
     * numbers[idx]
     * numbers[idx] = val
     */

    @Test
    public void learn_Inserting_And_Retrieving() {
        int[] arr = { 5, 7, 9 };
        assertEquals("[5, 7, 9]", Arrays.toString(arr));

        int[] arr2 = new int[arr.length];
        for (int i = 0; i < arr2.length; i++) arr2[i] = arr[i];

        assertEquals("[5, 7, 9]", Arrays.toString(arr2));
    }

    /**
     * Arrays.binarySearch(a, key)
     *
     * Arrays.sort(a)
     * Arrays.sort(a, fromIndex, toIndex)
     *
     * Arrays.parallelSort(a);
     */
    @Test
    public void learn_search_and_sort() {
        int[] arr = { 3, 9, 5, 7 };

        /*// unsorted + binary search = result undefined
        assertFalse(2 == Arrays.binarySearch(arr, 5));*/

        // Arrays.sort(a, fromIdx, toIdx)
        Arrays.sort(arr, 1, arr.length - 1);
        assertEquals("[3, 5, 9, 7]", Arrays.toString(arr));

        // Arrays.sort(a)
        Arrays.sort(arr);
        assertEquals("[3, 5, 7, 9]", Arrays.toString(arr));

        // Arrays.binarySearch
        assertEquals(2, Arrays.binarySearch(arr, 7));
        assertEquals(-1, Arrays.binarySearch(arr, 2));

        int[] arr2 = {7, 3, 5, 9, 5, 5, 7};
        Arrays.parallelSort(arr2);
        assertEquals("[3, 5, 5, 5, 7, 7, 9]", Arrays.toString(arr2));

        /*// duplicates + binarySearch = no guaranteed result
        assertEquals(3, Arrays.binarySearch(arr2, 5));*/
    }

    /**
     * Arrays.copyOf(T[] original)
     * Arrays.copyOfRange(T[] original, int from, int to)
     */
    @Test
    public void learn_copy_of_array() {
        // copy
        int[] arr = {1, 2, 3, 4};

        // Copy first 2 elements
        int[] copy = Arrays.copyOf(arr, 2);
        assertArrayEquals(new int[]{1, 2}, copy);

        // Copy with same length
        int[] sameLengthCopy = Arrays.copyOf(arr, arr.length);
        assertArrayEquals(arr, sameLengthCopy);

        // Copy with longer length (extra elements should be 0)
        int[] longerCopy = Arrays.copyOf(arr, 6);
        assertArrayEquals(new int[]{1, 2, 3, 4, 0, 0}, longerCopy);


        // copyOfRange
        String[] arr2 = {"dog", "cat", "bird", "fish"};

        // Copy from index 1 to 3 (excluding 3)
        String[] rangeCopy = Arrays.copyOfRange(arr2, 1, 3);
        assertArrayEquals(new String[]{"cat", "bird"}, rangeCopy);

        // Copy full range
        String[] fullRange = Arrays.copyOfRange(arr2, 0, arr2.length);
        assertArrayEquals(arr2, fullRange);

        // Copy ending with index out of bounds (fills with nulls)
        String[] extendedRange = Arrays.copyOfRange(arr2, 2, 6);
        assertArrayEquals(new String[]{"bird", "fish", null, null}, extendedRange);
    }

    /**
     * Arrays.asList(T... a)
     *
     * Arrays.equals(a, a2)
     *
     * Arrays.fill(a, val)
     */

    @Test
    public void learn_common_operations() {
        // Arrays.asList
        List<String> fruits = Arrays.asList("apple", "banana", "cherry");
        assertEquals(3, fruits.size());
        assertEquals("banana", fruits.get(1));

        // Arrays.equals
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};
        int[] array3 = {3, 2, 1};
        assertTrue(Arrays.equals(array1, array2));
        assertFalse(Arrays.equals(array1, array3));

        // Arrays.fill
        int[] filledArray = new int[5];
        Arrays.fill(filledArray, 42);
        for (int value : filledArray)
            assertEquals(42, value);
    }
}
