public class Abstract {
    public static void main(String[] args) {

        //abstract = Used to define abstract classes and methods.
        //           abstract is the process of hiding implementation details
        //           and showing only the essential features
        //           abstract classes can not be instantiated directly
        //           can contain 'abstract' methods (which must be implemented)
        //           can contain 'concrete' methods (which are inherited)

        Circle circle = new Circle(6);
        Triangle triangle = new Triangle(5,4);
        Rectangle rectangle = new Rectangle(4, 7);

        circle.display();
        triangle.display();
        rectangle.display();

        System.out.println(circle.area());
        System.out.println(triangle.area());
        System.out.println(rectangle.area());
    }
}
