public class Breakloop {
    public static void main(String[] args){

        for(int i = 0; i < 10; i++) {
            if(i==5) {
                break; //gets to 5 stop eg 1,2,3,4,5 ends
            }
            System.out.println(i + " ");
        }

        for(int i = 0; i < 10; i++) {
            if(i==6) {
                continue; // skips 6 eg 1,2,3,4,5,7,8,9,
            }
            System.out.println(i + " ");
        }
    }
}
