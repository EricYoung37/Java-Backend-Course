import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author M.Y.
 * @date 4/11/25
 */
public class TreeMapExerciseTest {

    /**
     * e.g.
     * TreeMap<String, Integer> map = new TreeMap<>();
     *
     * put(K key, V value)
     * putIfAbsent(K key, V value)
     * putAll(Map<? extends K, ? extends V> m)
     *
     * get(Object key)
     * firstKey()
     * lastKey()
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
        Map<String, Integer> map = new TreeMap<>();

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


        // containsKey
        assertTrue(map.containsKey("c"));

        // containsValue
        assertTrue(map.containsValue(2));

        // keySet
        assertEquals(new HashSet<>(Arrays.asList("c", "d")), addMap.keySet());

        // values
        assertEquals(Arrays.asList(3, 4), addMap.values().stream().collect(Collectors.toList()));

        // isEmpty
        assertFalse(addMap.isEmpty());


        // newMap declared as TreeMap to use firstKey & lastKey
        TreeMap<String, Integer> newMap = new TreeMap<>(map);

        // firstKey
        assertEquals("a", newMap.firstKey());

        // lastKey
        assertEquals("d", newMap.lastKey());
    }

    /**
     * replace(K key, V oldValue, V newValue)
     * replace(K key, V value)
     *
     * remove(Object key)
     */
    @Test
    public void learn_Remove_Replacing_Updating() {
/*        TreeMap<String, Integer> map = Stream.of(
                        new AbstractMap.SimpleEntry<>("d", 4),
                        new AbstractMap.SimpleEntry<>("c", 3),
                        new AbstractMap.SimpleEntry<>("e", 5),
                        new AbstractMap.SimpleEntry<>("f", 6))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, TreeMap::new));
        // mergeFunction (a, b) -> a resolves collision for duplicate keys*/

        TreeMap<String, Integer> map = new TreeMap<>(Stream.of(
                        new AbstractMap.SimpleEntry<>("d", 4),
                        new AbstractMap.SimpleEntry<>("c", 3),
                        new AbstractMap.SimpleEntry<>("e", 5),
                        new AbstractMap.SimpleEntry<>("f", 6))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

        // replace old value
        assertTrue(map.replace("d", 4, 2)); // "d" -> 2
        assertFalse(map.replace("d", 4, 5)); // won't replace

        // replace (return old value)
        assertEquals(2, map.replace("d", 4));
        assertEquals(4, map.get("d"));

        // remove
        assertEquals(3, map.remove("c"));
    }
}
