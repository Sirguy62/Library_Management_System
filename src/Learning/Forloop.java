import java.util.Scanner;

public class Forloop {

    public static void main(String[] args) throws InterruptedException {


        Scanner scanner = new Scanner(System.in);

        int start = 10;

        for (int i = 0; i < 10; i++ ){
            System.out.println("Hello");
        }

        for (int i = 10; i > 0; i -= 2){
            System.out.println("Hello");
        }

        System.out.println("Enter how many times you want to loop: ");
        int max = scanner.nextInt();

        for (int i = 0; i < max; i++) {
            System.out.println(i);
        }

        // project to simulate a countdown
         for (int i = start; i > 0; i--){
             System.out.println(1);
             Thread.sleep(1000);
         }


        scanner.close();
    }
}
