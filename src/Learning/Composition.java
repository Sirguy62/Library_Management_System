public class Composition {
    public static void main() {

        // composition = Represent a part of relationship between objects.
        //                for example, an Engine is part of a car, allows complex
        //               objects to be constructed from smaller objects.

        CompCar car = new CompCar("AMG", 2025, "V8");

        System.out.println(car.model);
        System.out.println(car.year);
        System.out.println(car.engine.type);

        car.startCar();
    }
}
