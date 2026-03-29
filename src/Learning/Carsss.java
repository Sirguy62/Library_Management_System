public class Carsss {

     String make;
     String model;
     int year;
     String color;

     Carsss(String make, String model, int year, String color) {
         this.make = make;
         this.model = model;
         this.year = year;
         this.color =color;
     }

     @Override
    public String toString() {
         return this.color + " " + this.year + " " + this.make + " " + this.model;
     }

}
