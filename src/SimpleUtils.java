import java.util.*;
import java.util.stream.Collectors;

public class SimpleUtils {
    /**
     * Find and return the least element from a collection of given elements that are comparable.
     * If head = tail, pick one depending on from_start, otherwise pick the lowest value.
     *
     * @param items: the given collection of elements
     * @param from_start a <code>boolean</code> flag that decides how ties are broken.
     * If <code>true</code>, the element encountered earlier in the
     * iteration is returned, otherwise the later element is returned.
     * @param <T>: the type parameter of the collection (i.e., the items are all of type T).
     * @return the least element in <code>items</code>, where ties are
     * broken based on <code>from_start</code>.
     */
    public static <T extends Comparable<T>> T least(Collection<T> items, boolean from_start){
        return items.stream()
                .filter(Objects::nonNull)
                .reduce((head,tail) -> head.compareTo(tail) == 0 ? (from_start ? head : tail) : (head.compareTo(tail) < 0 ? head : tail))
                .orElse(null);
    }

    public static void leastTest(){
        //Test with a list of Basketball Objects
        Basketball ball1 = new Basketball("Spalding", 4,4);
        Basketball ball2 = new Basketball("Wilson", 4,4);
        List<Basketball> basketballList = Arrays.asList(ball1, ball2);
        System.out.println(least(basketballList, true)); // Output: Spalding
        System.out.println(least(basketballList, false)); //Output: Wilson

        // Test with a list of integers
        List<Integer> intList = Arrays.asList(4, 7, 2, 9, 4, 1, 7, 3, 2);
        System.out.println(least(intList, true)); // Output: 1
        System.out.println(least(intList, false)); // Output: 1

        // Test with a list of strings
        List<String> strList = Arrays.asList("cat", "dog", "elephant", "bear", "fish");
        System.out.println(least(strList, true)); // Output: bear
        System.out.println(least(strList, false)); // Output: bear

        // Test with an empty list
        List<Integer> emptyList = Collections.emptyList();
        System.out.println(least(emptyList, true)); // Output: null

        // Test with a list of only one element
        List<Integer> singleItemList = Collections.singletonList(5);
        System.out.println(least(singleItemList, true)); // Output: 5
        System.out.println(least(singleItemList, false)); // Output: 5

        // Test with a list of null elements
        List<Integer> nullItemList = Arrays.asList(null, null, null);
        System.out.println(least(nullItemList, true)); // Output: null

        // Test with a list of custom objects with null values
        Basketball obj1 = new Basketball("one", 1,1);
        Basketball obj2 = new Basketball(null, 1,1);
        Basketball obj3 = new Basketball("three", 1,1);
        List<Basketball> customList = Arrays.asList(obj1, obj2, obj3);
        System.out.println(least(customList, true)); // Output: CustomObject{str=null, num=2}
        System.out.println(least(customList, false)); // Output: CustomObject{str=three, num=null}

        // Test with a list of comparable objects with negative values
        List<Integer> negativeList = Arrays.asList(-5, -3, -1, -8, -10);
        System.out.println(least(negativeList, true)); // Output: -10
        System.out.println(least(negativeList, false)); // Output: -10

        // Test with a list of comparable objects with duplicate values
        List<Integer> duplicateList = Arrays.asList(5, 7, 2, 9, 4, 1, 7, 3, 2);
        System.out.println(least(duplicateList, true)); // Output: 1
        System.out.println(least(duplicateList, false)); // Output: 1
    }

    /**
     * Flattens a map to a list of <code>String</code>s, where each element in the list is formatted
     * as "key -> value" (i.e., each key-value pair is converted to a string in this specific format).
     *
     * @param aMap the specified input map.
     * @param <K> the type parameter of keys in <code>aMap</code>.
     * @param <V> the type parameter of values in <code>aMap</code>.
     * @return the flattened list representation of <code>aMap</code>.
     */
    public static <K,V> List<String> flatten(Map<K,V> aMap){
        return aMap.entrySet().stream()
                .map(entry -> entry.getKey() + " -> " + entry.getValue())
                .collect(Collectors.toList());
    }

    public static void flattenTest() {
        // Test case 1: empty map
        Map<String, Integer> emptyMap = Collections.emptyMap();
        List<String> expected1 = Collections.emptyList();
        List<String> actual1 = flatten(emptyMap);
        System.out.println("\nExpected Empty Map:" + expected1);
        System.out.println("Actual Empty Map:" + actual1);
        assert expected1.equals(actual1) : "Test case 1 failed";

        // Test case 2: map with one entry
        Map<Integer, String> map1 = new HashMap<>();
        map1.put(1, "one");
        List<String> expected2 = Collections.singletonList("1 -> one");
        List<String> actual2 = flatten(map1);
        System.out.println("\nExpected One Entry Map:" + expected2);
        System.out.println("Actual One Entry Map:" + actual2);
        assert expected2.equals(actual2) : "Test case 2 failed";

        // Test case 3: map with multiple entries
        Map<String, Double> map2 = new HashMap<>();
        map2.put("pi", 3.14);
        map2.put("e", 2.71);
        List<String> expected3 = Arrays.asList("pi -> 3.14", "e -> 2.71");
        List<String> actual3 = flatten(map2);
        System.out.println("\nExpected multiple entry Map:" + expected3);
        System.out.println("Actual multiple entry Map:" + actual3);
        assert expected3.equals(actual3) : "Test case 3 failed";

        // Test case 4: map with non-String key and non-Double value
        Map<Character, Integer> map3 = new HashMap<>();
        map3.put('a', 1);
        map3.put('b', 2);
        map3.put('c', 3);
        List<String> expected4 = Arrays.asList("a -> 1", "b -> 2", "c -> 3");
        List<String> actual4 = flatten(map3);
        System.out.println("\nExpected non-String key and non-Double value Map:" + expected4);
        System.out.println("Actual non-String key and non-Double value Map:" + actual4);
        assert expected4.equals(actual4) : "Test case 4 failed";

        // Test case 5: map with null key and null value
        Map<String, String> map4 = new HashMap<>();
        map4.put(null, null);
        List<String> expected5 = Collections.singletonList("null -> null");
        List<String> actual5 = flatten(map4);
        System.out.println("\nExpected null key and null value Map:" + expected5);
        System.out.println("Actual null key and null value Map:" + actual5);
        assert expected5.equals(actual5) : "Test case 5 failed";

        // Test case 6: map with duplicate keys
        Map<String, Integer> map5 = new HashMap<>();
        map5.put("one", 1);
        map5.put("two", 2);
        map5.put("one", 3);
        List<String> expected6 = Arrays.asList("one -> 1", "two -> 2");
        List<String> actual6 = flatten(map5);
        System.out.println("\nExpected Duplicate Key Map: " + expected6);
        System.out.println("Actual Duplicate Key Map: " + actual6);
        assert expected6.equals(actual6) : "Test case 6 failed";
    }


    /**
     * Testing method
     * least(Cases)
     * Case 1: true -> return earliest occurrence of the least element
     * Case 2: flag = false -> return latest occurrence of the least element
     *
     * map
     * Just creates a list of strings that show the mapping from key to value
     *
     * @param args
     */
    public static void main(String[] args) {
        leastTest();
        flattenTest();
    }
}
