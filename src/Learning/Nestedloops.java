import java.util.Scanner;

public class Nestedloops {

    public static void main (String[] args) {


        Scanner scanner = new Scanner(System.in);

        int rows;
        int columns;
        char symbol;

        System.out.println("Enter the number of rows: ");
        rows = scanner.nextInt();

        System.out.println("Enter the number of columns: ");
        columns = scanner.nextInt();

        System.out.println("Enter the your desired symbol: ");
        symbol = scanner.next().charAt(0);


         for (int i = 0; i < rows; i++) {
             for (int j = 0; j < columns; j++) {
                 System.out.println(symbol);
             }
             System.out.println();
         }


        for (int i = 1; i <= 3; i++ ) {
            for (int j = 1; j <= 10; j++) {
                System.out.print(j + " ");
            }
            System.out.println();
        }

        scanner.close();
    }
}
