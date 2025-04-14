import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author M.Y.
 * @date 4/11/25
 */
public class AdditionalMapExerciseTest {

    /**
     * e.g.
     * ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
     *
     * put(K key, V value)
     * putIfAbsent(K key, V value)
     * putAll(Map<? extends K, ? extends V> m)
     */
    @Test
    public void learn_ConcurrentHashMap() {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

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
    }

    /**
     * e.g.
     * Map<DayOfWeek, Integer> map = new IdentityHashMap<>();
     *
     * put(K key, V value)
     * putIfAbsent(K key, V value)
     */
    @Test
    public void learn_IdentityHashMap() {
        // Example 1
        Map<DayOfWeek, Integer> map = new IdentityHashMap<>();

        DayOfWeek monday1 = DayOfWeek.MONDAY;
        DayOfWeek monday2 = DayOfWeek.MONDAY; // Same enum value, same reference

        // put
        map.put(monday1, 1);

        // putIfAbsent
        map.putIfAbsent(monday2, 2); // Since monday1 == monday2, value should not change

        assertEquals(1, map.size());
        assertEquals(1, map.get(DayOfWeek.MONDAY));

        // Create a new DayOfWeek using valueOf to simulate a different reference (not possible with enums, just for illustration)
        DayOfWeek tuesday1 = DayOfWeek.TUESDAY;
        DayOfWeek tuesday2 = DayOfWeek.valueOf("TUESDAY"); // Still same reference due to enum caching

        map.put(tuesday1, 3);
        assertEquals(2, map.size());

        // Example 2
        // Prove that identity is used by creating a key with a different reference (not doable with enums directly)

        Map<String, Integer> idMap = new IdentityHashMap<>();
        String a = new String("hello");
        String b = new String("hello"); // Different object reference

        idMap.put(a, 1);
        idMap.putIfAbsent(b, 2); // Will insert because a != b by reference

        assertEquals(2, idMap.size());
        assertEquals(1, idMap.get(a));
        assertEquals(2, idMap.get(b));
    }

    /**
     * e.g.
     * EnumMap<DayOfWeek, Integer> enumMap = new EnumMap<>(DayOfWeek.class);
     *
     * put(K key, V value)
     * putIfAbsent(K key, V value)
     */
    @Test
    public void learn_EnumMap() {
        EnumMap<DayOfWeek, Integer> enumMap = new EnumMap<>(DayOfWeek.class);

        // put
        enumMap.put(DayOfWeek.MONDAY, 1);
        assertEquals(1, enumMap.get(DayOfWeek.MONDAY));

        // putIfAbsent
        enumMap.putIfAbsent(DayOfWeek.MONDAY, 100);
        assertEquals(1, enumMap.get(DayOfWeek.MONDAY)); // still 1

        enumMap.putIfAbsent(DayOfWeek.TUESDAY, 2);
        assertEquals(2, enumMap.get(DayOfWeek.TUESDAY));
    }
}
