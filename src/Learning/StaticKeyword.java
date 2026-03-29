public class StaticKeyword {
   public static void main(String[] args) {
       Friends friend1 = new Friends("Edwin");
       Friends friend2 = new Friends("Patric");
       Friends friend3 = new Friends("Gospel");
       Friends friend4 = new Friends("Sirguy");
       Friends friend5 = new Friends("Rescue");

       Friends.showFriends();
       System.out.println(Friends.numOfFriends);
   }
}
