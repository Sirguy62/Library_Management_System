public class CarObject {
    String make = "Ford";
    String model = "Mustang";
    int year = 2025;
    double price = 500000.99;
    boolean isRunning = false;


    void start() {
        System.out.println("Car has been started");
    }

    void stop() {
        System.out.println("Car have been stopped");
    }
}


//this is a way of creating objects in java, can be called in other
// files by "CarObject car = new CarObject();" in the required file