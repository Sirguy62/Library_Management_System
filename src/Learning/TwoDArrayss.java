public class TwoDArrayss {
    public static void main (String[] args) {

        // 2D array is an array used in storing 2 or more arrays

        String[] fruits = {"apple", "orange", "banana"};
        String[] vegetables = {"potato", "onion", "carrots"};
        String[] meats = {"chicken", "pork", "beef", "fish"};

        char[][] telephone = {{'1','2','3'},{'4','5','6'},{'7','8','9'},{'*','0','+'}};
        String[][] groceries = {fruits, vegetables, meats};
        groceries[2][2] = "pig";
        telephone[3][2] = '#';

        for (String[] foods : groceries){
            for (String food : foods) {
                System.out.print(food + " ");
            }
            System.out.println();
        }

        for (char[] numbers : telephone){
            for (char number : numbers){
                System.out.print(number + " ");
            }
            System.out.println();
        }
    }
}
