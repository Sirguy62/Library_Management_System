import java.util.Arrays;
import java.util.Scanner;

public class Arraysss {
    public static void main(String[] args){

        Scanner scanner = new Scanner(System.in);
//        String[] fruits = {"Mango", "Apple", "Orange", "Corn"};
        String[] foods = new String[4]; //create 4 empty arrays
        String[] fruits;
        int size;

        System.out.println("What number of food do you want");
        size = scanner.nextInt();
        scanner.nextLine();


        fruits =new String[size];

        for (int i = 0; i < fruits.length; i++){
            System.out.print("Enter a fruit: ");
            fruits[i] = scanner.nextLine();
        }

        // OR
//        Arrays.sort(fruits);
//        Arrays.fill(fruits, "Coco");

        for (String fruit : fruits) {  // for each loop
            System.out.println(fruit);
        }

        //sorting arrays


//
//        foods[0] = "pizza";
//        foods[1] = "taco";
//        foods[2] = "bread";
//        foods[3] ="pasta";
//
//        for (String food : foods) {
//            System.out.print(food + " ");
//        }

        scanner.close();
    }
}
