public class OverLoaded {

    static int x = 3; //global or class  variable

    public static void main (String[] args) {
        //variable scope

        int x = 3; // local variable

        System.out.println(x);
    }

    static void function () {
        int x = 3; // local variable
    }
}
