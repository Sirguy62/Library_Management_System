public class Aggregation {
    public static void main() {

        //Aggregation = Represents a "has-a" relationship between objects.
        //              one object contains another object as part of its structure,
        //              but the contained objects can exist independently


        Book book1 = new Book("Lord of the rings", 433);
        Book book2 = new Book("Good bo", 400);
        Book book3 = new Book("Obi goes to school", 124);
        Book book4 = new Book("Ashes of the dead", 328);

        Book[] book = {book1,book2,book3,book4};


        Library library = new Library("NYC Public Library", 1897, book);
        library.displayInfo();


    }
}
