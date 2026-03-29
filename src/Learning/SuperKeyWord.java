public class SuperKeyWord {
    public static void main() {
        //super = Refers to the parent class (subclass <- superclass)
        //  Used in constructors and method overriding
        //calls parent constructor to initialize attributes

        Person person = new Person("Gospel", "Sirguy");
        Student student = new Student("Gospel", "Sirguy", 3.5);


        System.out.println(student.first);


        System.out.println(person.first);
        person.showName();
        student.showName();
        student.showGPA();
    }
}
