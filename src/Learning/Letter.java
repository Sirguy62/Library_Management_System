import java.lang.String;

public class Letter {
    public  static void main (String[] args) {
        String name = "Gospel";
        char FirstLetter = 'S';
        int age = 30;
        double height = 60.5;
        boolean isEmployed = true;
        double price1 = 99000.9;
        double price2 = 40000.5;
        double price3 = -47000.98;

        System.out.printf("Hello %s\n", name);
        System.out.printf("Your name starts with a %c\n", FirstLetter);
        System.out.printf("You are %d years old\n", age);
        System.out.printf("You are %f inches tall\n", height);
        System.out.printf("Employed: %b\n", isEmployed);

        System.out.printf("%s is %d years old\n", name, age);

        System.out.printf("%,+.1f\n", price1);
        System.out.printf("%,.2f\n", price2);
        System.out.printf("%,.4f\n", price3);
    }
}
