public class Interface {
    public static void main() {

           //interface = A blueprint for a class that specifies a set of abstract methods
           //            that implementing classes must define.
           //            support multiple inheritance like behavior


        Rabbit rabbit = new Rabbit();
        Hawk hawk = new Hawk();
        Snail snail = new Snail();

        hawk.hunt();
        rabbit.flee();
        snail.flee();
        snail.hunt();
    }
}
