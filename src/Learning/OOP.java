public class OOP {
    public static void main () {
        CarObject car = new CarObject();
        car.isRunning= true;
        boolean running = car.isRunning;



        System.out.println(car.make + " " + running);
        car.start();
        car.stop();
    }

}
