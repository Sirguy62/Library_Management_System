public class DynamicArrayDSA {
    public static void main(String[] args){


        Dynamicss dynamicss = new Dynamicss(5);

        dynamicss.add("A");
        dynamicss.add("B");
        dynamicss.add("C");
        dynamicss.add("D");
        dynamicss.add("E");
        dynamicss.add("F");

        dynamicss.delete("D");
        dynamicss.delete("E");
        dynamicss.delete("F");

        dynamicss.insert(0, "X");
        dynamicss.delete("A");

        System.out.println( dynamicss.search("C"));

        System.out.println(dynamicss);
        System.out.println(dynamicss.size);
        System.out.println(dynamicss.capacity);
        System.out.println("Empty: " +dynamicss.isEmpty());
    }
}
