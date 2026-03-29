public class Carrrrr {

   private final String model;  // final key word is just an extra security that this variable is not to be changed
   private String color;
   private int price;

    Carrrrr(String model, String color, int price) {
        this.model = model;
        this.color = color;
        this.price = price;
    }


    //GETTERS
    String getModel(){
        return this.model;
    }
    String getColor(){
        return this.color;
    }
    String getPrice(){
        return "$" + this.price;
    }

    //SETTERS
    void setColor(String color) {
        this.color = color;
    }
    void setPrice(int price) {

        if (price <= 0){
            System.out.println("Price can't be zero or below");
        }else {
            this.price = price;
        }
    }


}
