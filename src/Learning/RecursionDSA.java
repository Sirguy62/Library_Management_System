import java.io.File;

public class RecursionDSA {
    public static void main(String[] args) {
        // recursion = When a thing is defined in terms of itself. - Wikipedia
        //
        //             Apply the result of a procedure, to a procedure.
        //             A recursive method calls itself. Can be a substitute for iteration.
        //             Divide a problem into sub-problems of the same type as the original.
        //             Commonly used with advanced sorting algorithms and navigating trees
        //
        //
        // Advantages
        // ----------
        //
        // easier to read/write
        // easier to debug
        //
        //
        // Disadvantages
        // -------------
        //
        // sometimes slower
        // uses more memory


        File folder = new File("C:/example");
        listFiles(folder);
        System.out.println(factorial(5));

    }
            public static void listFiles(File dir) {

                if (dir == null || !dir.exists()) {
                    return;
                }

                File[] files = dir.listFiles();

                for (File file : files) {

                    if (file.isDirectory()) {
                        listFiles(file); // recursive call
                    } else {
                        System.out.println(file.getName());
                    }
                }
            }
    public static int factorial(int n) {

        if (n == 1) {
            return 1; // base case
        }

        return n * factorial(n - 1);
    }
        }



