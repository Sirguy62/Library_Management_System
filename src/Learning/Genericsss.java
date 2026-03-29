public class Genericsss {
    public static void main(String[] args) {

// Generics = A concept where you can write a class, interface, or method.
        //   that is compatible with different data types.
        // <T> type parameter (Placeholder that gets replaced with <String> type argument (specifies the type)

        Box<String> box1 = new Box<>();
        Box<Integer> box2 = new Box<>();
        Product<String, Double> product = new Product<>("apple", 50.0);

        box1.setItem("banana");
        box2.setItem(2);

        System.out.println(box1.getItem());
        System.out.println(box2.getItem());
        System.out.println(product.getItem());
        System.out.println(product.getPrice());


    }
}
