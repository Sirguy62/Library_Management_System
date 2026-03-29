public class AnanymousClass {
    public static void main(String[] args) {

        //Anonymous class = A class that doesn't have a name. Cannot be reused.
        //             Add custom behavior without having to create a new class.
        //            often used for one time uses (TimerTask, Runnable, callbacks)

     Doug doug1 = new Doug();
     Doug doug2 = new Doug(){
       @Override
       void speak() {
           System.out.println("This is a way to create an Anonymous class");
       }
     };

     doug1.speak();
     doug2.speak();

    }
}
