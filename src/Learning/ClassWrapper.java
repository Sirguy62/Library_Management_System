public class ClassWrapper {
    public static void main() {

        //Wrapper classes = Allow primitive values (int, char, double, boolean)
        //                  to be used as objects. Wrap them in an object.
        //                  Generally don't wrap primitives unless you need an object.
        //                  Allow use of Collections Framework and static Utility Methods.

        //Older way of using wrapper
//        Integer a = new Integer(123);
//        Double b = new Double(3.14);
//        Character c = new Character('$');
//        Boolean d = new Boolean(true);

        //Modern way of using wrapper "or" it can be called Autoboxing, wrapping a
        // primitive within an object
        Integer a = 123;
        Double b = 3.14;
        Character c = '$';
        Boolean d = true;
        String e = "Pizza";


        //Unboxing or unwrapping primitive from an object
        int f = 123;
        double g = 3.14;
        char h = '$';
        boolean j = true;
        String k = "Pizza";

        // converting primitive data type into a string
        String l = Integer.toString(123);
        String m = Double.toString(3.14);
        String n = Character.toString('@');
        String o = Boolean.toString(false);

        String p = l + m + n + o;
        System.out.println(p);

        // converting string into a primitive data type
        int q = Integer.parseInt("123");
        double r = Double.parseDouble("3.14");
        char s = "Pizza".charAt(0);
        boolean t = Boolean.parseBoolean("true");

    }


}
