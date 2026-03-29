public class GetterSetterMethod {
    public static void main() {

        //They help protect object data and add rules for accessing or modifying.
        //Getters  = Methods that makes a field READABLE.
        //Setters = Methods that makes a field WRITEABLE


        Carrrrr carrrrr = new Carrrrr("AMG", "Ash", 50000);

        carrrrr.setColor("Blue");
        carrrrr.setPrice(100000);


        System.out.println(carrrrr.getModel() + " " + carrrrr.getColor() + " " + carrrrr.getPrice());
    }
}
