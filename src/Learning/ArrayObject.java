public class ArrayObject {
    String car;
    String color;

    ArrayObject(String car, String color) {
        this.car = car;
        this.color = color;
    }

    void drive(){
        System.out.println("You drive a " + color+ " " + car);
    }
}
