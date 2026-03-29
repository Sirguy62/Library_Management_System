import java.util.Scanner;
public class StringMethod {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = "  Edwin Gospel  ";

        int length = name.length();
        char letter = name.charAt(0);
        int index = name.indexOf("i");
        int lastIndex = name.lastIndexOf("e");
        String upperCase;
        String email = "gospele62@gmail.com";

        if (email.contains("@")) {
            String userName = email.substring(0, email.indexOf("@"));
            String domain = email.substring(email.indexOf("@") + 1);

            System.out.println(userName);
            System.out.println(domain);

        } else {
            System.out.println("Email must contain @");
        }

        upperCase = name.toUpperCase();
        name = name.toLowerCase();
        name = name.trim();
        name = name.replace("o", "a");

        System.out.println(length);

        scanner.close();

    }
}
