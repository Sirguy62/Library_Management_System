public class Methods {
    public static void main (String[] args) {

        // Methods or reuseable block of code or function

        String name = "Gospel";
        int age = 10;
        String completeName = fullName("Gospel", "Edwin");
        int ageOld = 17;

        happyBirthday(name, age);
        happyBirthday(name, age);
        System.out.println(square(3));
        System.out.println(cube(4));
        System.out.println(completeName);
        System.out.println(ageVerification(ageOld));


        if (ageVerification(ageOld)){
            System.out.println("You may signUp");
        }else {
            System.out.println("You are too young for this");
        }
    }

    static void happyBirthday(String name, int age) {
        System.out.println("Happy Birthday to you");
        System.out.printf("Happy Birthday dear %s!\n", name);
        System.out.printf("You are %d years old!\n", age);
        System.out.println("Happy Birthday to you!\n");
    }
    static  double square (double num){
        return num * num;
    }
    static  double cube(double num) {
        return num * num * num;
    }
    static  String fullName (String first, String second) {
        return  first + " " + second;
    }

    static  boolean ageVerification (int age) {
        if (age >= 18) {
            return true;
        } else {
            return false;
        }
    }
}


