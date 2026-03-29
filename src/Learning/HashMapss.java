import java.util.HashMap;

public class HashMapss {
    public static void main(String[] args) {

        //HashMap = A data structure that stores key-value pairs
        //          keys are unique, but values can be duplicated
        //          does not maintain any order, but is memory efficient
        //          HashMap<Key, Value>

        HashMap<String, Double> map = new HashMap<>();

        map.put("Good", 5.0);
        map.put("Good", 500.0); //HashMap can not have duplicate keys, will be over written by the recent key
        map.put("Orange", 40.0);
        map.put("Pineapple", 50.0);
        map.put("coconut", 5.0);

        map.remove("coconut");

        System.out.println(map);
        System.out.println(map.get("Good"));
        System.out.println(map.containsKey("Orange"));// returns a boolean, checks if the item exists

        if (map.containsKey("Orange")) {
            System.out.print("Item found: ");
            System.out.println(map.get("Orange"));
        } else {
            System.out.println("Item not found");
        }

        System.out.println(map.containsValue(50.0));
        System.out.println(map.size());

        for (String key : map.keySet()) {
            System.out.println(key + " : $" + map.get(key));
        }
    }
}
