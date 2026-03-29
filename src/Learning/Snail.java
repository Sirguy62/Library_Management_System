public class Snail implements Prey,Predator{


    @Override
    public void hunt() {
        System.out.println("The snail hunts smaller snails");
    }
    @Override
    public void flee() {
        System.out.println("The snail is running from bigger snails");
    }
}
