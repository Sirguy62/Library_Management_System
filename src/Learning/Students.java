public class Students {
    String name;
    int age;
    double gpa;
    boolean isEnrolled;

    Students(String name, int age, double gpa) {

        this.name = name;
        this.age = age;
        this.gpa = gpa;
        this.isEnrolled = true;
    }

    void study() {
        System.out.println(name + "is enrolled");
    }
}
