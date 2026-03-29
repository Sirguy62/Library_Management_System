public class CompCar {

    String model;
    int year;
    Engine engine;

    CompCar( String model, int year, String engineType) {
        this.model = model;
        this.year = year;
        this.engine = new Engine(engineType);
    }

    void startCar() {
        this.engine.start();
        System.out.println("The " + this.model + " is running");
    }
}
