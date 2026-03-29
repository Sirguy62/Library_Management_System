public abstract class Shape {

    abstract double area(); // Abstract methods can not be inherited by its children

    void display() { // Concrete methods are inherited directly
        System.out.println("This ia a shape");
    }

}
