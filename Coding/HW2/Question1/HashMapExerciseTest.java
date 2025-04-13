import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author M.Y.
 * @date 4/11/25
 */
public class HashMapExerciseTest {

    /**
     * e.g.
     * Map<String, Integer> map = new HashMap<>();
     *
     * put(K key, V value)
     * putIfAbsent(K key, V value)
     * putAll(Map<? extends K, ? extends V> m)
     *
     * get(Object key)
     * getOrDefault(Object key, V defaultValue)
     *
     * containsKey(Object key)
     * containsValue(Object value)
     *
     * keySet()
     * values()
     * isEmpty()
     */

    @Test
    public void learn_Inserting_And_Retrieving() {
        Map<String, Integer> map = new HashMap<>();

        // put
        map.put("a", 1);
        assertEquals(1, map.get("a"));

        // putIfAbsent
        map.putIfAbsent("b", 2);
        assertEquals(2, map.get("b"));


        // putAll
        
        // map to be added
        Map<String, Integer> addMap = Stream.of(
                        new AbstractMap.SimpleEntry<>("d", 4),
                        new AbstractMap.SimpleEntry<>("c", 3))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        
        // result map
        Map<String, Integer> resultMap = Stream.of(new Object[][] {
                { "d", 4 },
                { "c", 3 },
                { "a", 1 },
                { "b", 2 },
        }).collect(Collectors.toMap(data -> (String) data[0], data -> (Integer) data[1]));

        map.putAll(addMap);
        assertEquals(resultMap, map);


        // getOrDefault
        assertEquals(-1, map.getOrDefault("e", -1));

        // containsKey
        assertTrue(map.containsKey("c"));

        // containsValue
        assertTrue(map.containsValue(2));

        // keySet
        assertEquals(new HashSet<>(Arrays.asList("c", "d")), addMap.keySet());

        // values
        assertEquals(Arrays.asList(3, 4), new ArrayList<>(addMap.values()));

        // isEmpty
        assertFalse(addMap.isEmpty());
    }

    /**
     * replace(K key, V oldValue, V newValue)
     * replace(K key, V value)
     * replaceAll(BiFunction<? super K, ? super V, ? extends V> function)
     *
     * remove(Object key)
     * remove(Object key, Object value)
     *
     * compute(Key, BiFunction)
     * computeIfAbsent(Key, Function)
     * computeIfPresent(Key, BiFunction)
     */
    @Test
    public void learn_Remove_Replacing_Updating() {
        Map<String, Integer> map = Stream.of(
                    new AbstractMap.SimpleEntry<>("d", 4),
                    new AbstractMap.SimpleEntry<>("c", 3),
                    new AbstractMap.SimpleEntry<>("e", 5),
                    new AbstractMap.SimpleEntry<>("f", 6))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // replace old value
        assertTrue(map.replace("d", 4, 2)); // "d" -> 2
        assertFalse(map.replace("d", 4, 5)); // won't replace

        // replace (return old value)
        assertEquals(2, map.replace("d", 4));
        assertEquals(4, map.get("d"));

        // remove
        assertEquals(3, map.remove("c"));

        // remove only if mapped to the specified value
        assertFalse(map.remove("d", 5)); // won't remove
        assertTrue(map.remove("d", 4));

        // compute (return new value or null)
        assertEquals(2 * 1 + 5, map.compute("e", (key, value) -> 2 * key.length() + value));
        assertNull(map.computeIfPresent("b", (key, value) -> value * 3));

        // computeIfAbsent (return current or new value or null)
        assertEquals(2, map.computeIfAbsent("c", (key) -> key.charAt(0) - 'a'));
        assertNull(map.computeIfAbsent("d", (key) -> null));
        assertEquals(7, map.computeIfAbsent("e", (key) -> 3)); // won't re-map to 3

        // computeIfPresent (return new value or null)
        assertEquals(12, map.computeIfPresent("f", (key, value) -> value * 2));
        assertNull(map.computeIfPresent("a", (key, value) -> value * 2));
    }
}
