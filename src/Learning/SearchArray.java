import java.util.Scanner;

public class SearchArray {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        int[] numbers = {1,2,3,4,5,6,7,8,};
        String[] fruits = {"apple", "orange", "banana"};
        int target;
        boolean isFound = false;
        String look;

        System.out.println("Enter fruit: ");
        look = scanner.nextLine();

        System.out.println("Enter target index: ");
        target = scanner.nextInt();


//         data structure of linear search
        for (int i = 0; i < numbers.length; i++) {
            if (target == i) {
                System.out.println("Number found at index " + i);
                isFound = true;
                break;// when number is found break or stop the loop
            }
            }
        if (!isFound) {
            System.out.println("Number not found");
        }

        for (int i = 0; i < fruits.length; i++) {
            if (fruits[i].equals(look)) {
                System.out.println("fruit found at index " + i);
                isFound = true;
                break;// when number is found break or stop the loop
            }
        }
        if (!isFound) {
            System.out.println("fruit not found");
        }

        scanner.close();
    }
}
