public class OverloadedConstructors {
   public static void main() {
       User user1 = new User("Gospel");
       User user2 = new User("Edwin", "test@gmail");
       User user3 = new User("Cool", "Test@gmail", 5);
       ArrayObject car1 = new ArrayObject("Mecedez", "Red");
       ArrayObject car2 = new ArrayObject("Camry", "Blue");
       ArrayObject car3 = new ArrayObject("Gwagon", "white");

       ArrayObject[] cars = {car1,car2,car3};
       // OR //
//       ArrayObject[] cars = {new ArrayObject("Gwagon", "white"),
//                             new ArrayObject("Camry", "Blue"),
//                             new ArrayObject("Mecedez", "Red")};



       for (int i = 0; i < cars.length; i++ ){
          cars[i].drive();
       }
       // OR use the enhanced for loop method
       for (ArrayObject car : cars) {
           car.drive();
       }


       // To change the color of all the cars
       for (ArrayObject car : cars) {
           car.color = "green";
       }

       for (ArrayObject car : cars) {
           car.drive();
       }


       System.out.println(user1.name);
       System.out.println(user2.name);
       System.out.println(user3.name);
   }
}
