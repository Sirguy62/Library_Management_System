public class Constructorsss {
    public static void main (){


        Students student1 = new Students("Gospel", 25, 4.0);
        Students student2 = new Students("Edwin", 20, 3.5);


        System.out.println(student1.age);
        System.out.println(student1.name);
        System.out.println(student1.gpa);
        System.out.println(student1.isEnrolled);
        student1.study();

        System.out.println(student2.age);
        System.out.println(student2.name);
        System.out.println(student2.gpa);
        System.out.println(student2.isEnrolled);
        student2.study();

    }
}
