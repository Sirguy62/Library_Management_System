import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class ArrayListss {
    public static void main() {

        Scanner scanner = new Scanner(System.in);

        // ArrayList = A resizeable array that stores objects (autoboxing).
        //           Array are fixed in size, but ArrayLists can change

        ArrayList<Integer> list = new ArrayList<>();
        ArrayList<Double> dou = new ArrayList<>();
        ArrayList<String> fruits = new ArrayList<>();
        ArrayList<String> foods = new ArrayList<>();

        System.out.println("Enter the number of food you would like: ");
        int numOfFood = scanner.nextInt();
        scanner.nextLine();

        for (int i = 1; i <= numOfFood; i++){
        System.out.println("Enter Food #" + i + ": ");
        String food = scanner.nextLine();
        foods.add(food);
        }

        System.out.println(foods);


        list.add(1);
        list.add(3);
        list.add(3);
        dou.add(3.6);
        dou.add(6.3);
        fruits.add("apple");
        fruits.add("mango");
        fruits.add("orange");
        fruits.add("banana");
        fruits.add("berry");

        fruits.remove(0);
        fruits.set(0, "corn");

        Collections.sort(fruits);// sort fruits


        for (String fruit : fruits) {
            System.out.println(fruit);
        }

        System.out.println(list);
        System.out.println(dou);
        System.out.println(fruits);
        System.out.println(fruits.get(1));
        System.out.println(fruits.size());


        scanner.close();

    }
}
