public class Polymorphism {
    public static void main() {

        //  Polymorphism = "POLY" = "MANY"
        //                 "MORPH" = "SHAPE"
        //                 objects can identify as other objects.
        //                 Objects can be treated as objects of a common superclass

        Carrrr car = new Carrrr();
        Bike bike = new Bike();
        Boat boat = new Boat();

        car.go();
        bike.go();
        boat.go();

        Vehicle[] vehicles = {car, bike, boat};

//        System.out.println(vehicles);

        for (Vehicle vehicle : vehicles) {
            vehicle.go();
        }
    }
}
