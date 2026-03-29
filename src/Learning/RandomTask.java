import java.util.Random;
public class RandomTask {
    public static void main (String[] args){
        Random random = new Random();

        boolean isHeads;
        isHeads = random.nextBoolean();

        if(isHeads) {
            System.out.println("Heads");
        }else{
            System.out.println("Tails");
        }
    }
}
