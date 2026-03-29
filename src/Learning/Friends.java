public class Friends {

    static  int numOfFriends;
    String name;

    Friends(String name) {
        this.name = name;
        numOfFriends++;
    }

    static void  showFriends() {
        System.out.println("You have " + numOfFriends + " total friends");
    }
}
