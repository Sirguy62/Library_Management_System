import java.util.Random;
import java.util.Scanner;

public class SlotMachine {
    public static void main (String[] args) {

        Scanner scanner = new Scanner(System.in);

        int balance = 100;
        int bet;
        int payout;
        String[] row;
        String playAgain;

        System.out.println("Welcome to java slot");
        System.out.println("Symbols: 🍒 🍉 🍋 🍎 🥭");


        while (balance > 0) {
            System.out.println("Current balance: $" + balance);
            System.out.println("Place your amount: ");
            bet = scanner.nextInt();
            scanner.nextLine();

            if (bet > balance) {
                System.out.println("INSUFFICIENT FUNDS");
                continue;
            } else if (bet <= 0) {
                System.out.println("Bet must be greater than 0");
            }else {
                balance = balance - bet;
                System.out.println("$" + balance);
            }

            System.out.println("Spinning....");
            row = spinRow();
            printRow(row);
           payout =  getPayout(row, bet);

           if (payout > 0) {
               System.out.println("You won $" + payout);
               balance = balance + payout;
           }else {
               System.out.println("Sorry you lost");
           }

            System.out.print("Do you want to play again? (Y/N): ");
            playAgain = scanner.nextLine().toUpperCase();

           if (!playAgain.equals("Y")) {
               break;
           }
        }

        System.out.println("GAME OVER! Your final balance is $" + balance);

        scanner.close();
    }

    static String[] spinRow() {

        String[] symbols = {"🍒", "🍉", "🍋", "🍎", "🥭"};
        String[] row = new String[3];
        Random random = new Random();


        for (int i = 0; i < 3; i++){
            row[i] = (symbols[random.nextInt(symbols.length)]);
        }
        return row;
    }
    static void printRow(String[] row) {
        System.out.println(" " + String.join(" | ", row));
    }
     static int getPayout (String[] row, int bet) {

           if(row[0].equals(row[1]) && row[1].equals(row[2])) {
             return switch(row[0]) {
                 case "🍒" -> bet *  3;
                 case "🍉" -> bet *  4;
                 case "🍋" -> bet *  5;
                 case "🍎" -> bet *  10;
                 case "🥭" -> bet *  20;
                 default -> 0;
             };
         }else if(row[0].equals(row[1])) {
            return switch(row[1]) {
                case "🍒" -> bet *  2;
                case "🍉" -> bet *  3;
                case "🍋" -> bet *  4;
                case "🍎" -> bet *  5;
                case "🥭" -> bet *  10;
                default -> 0;
            };
         }else if(row[1].equals(row[2])) {
               return switch(row[2]) {
                   case "🍒" -> bet *  2;
                   case "🍉" -> bet *  3;
                   case "🍋" -> bet *  4;
                   case "🍎" -> bet *  5;
                   case "🥭" -> bet *  10;
                   default -> 0;
               };
           }

        return 0;

     }
}
