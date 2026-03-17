package Library;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Library library = new Library();
        library.loadFile("books.txt");

        boolean running = true;

        while (running) {

            System.out.println("\n===== LIBRARY MENU =====");
            System.out.println("1. Add Book");
            System.out.println("2. View Books");
            System.out.println("3. Borrow Book");
            System.out.println("4. Return Book");
            System.out.println("5. Search Book");
            System.out.println("6. Search Author");
            System.out.println("7. Delete Book");
            System.out.println("8. Available Books");
//            System.out.println("9. save Book");
//            System.out.println("10. Load Book");
            System.out.println("0. exit");
            System.out.print("Choose option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    System.out.print("Enter Book ID: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Enter Title: ");
                    String title = scanner.nextLine();

                    System.out.print("Enter Author: ");
                    String author = scanner.nextLine();

                    Book book = new Book(id, title, author);
                    library.addBook(book);

                    System.out.println("Book Added!");
                    break;

                case 2:
                    library.viewBooks();
                    break;

                case 3:
                    System.out.print("Enter Book ID to borrow: ");
                    int borrowId = scanner.nextInt();
                    library.borrowBook(borrowId);
                    break;

                case 4:
                    System.out.print("Enter Book ID to return: ");
                    int returnId = scanner.nextInt();
                    library.returnBook(returnId);
                    break;

                case 5:
                    System.out.print("Enter title to search: ");
                    String titleName = scanner.nextLine();
                    library.searchBook(titleName);
                    break;

                case 6:
                    System.out.print("Enter author to search: ");
                    String authorName = scanner.nextLine();
                    library.searchAuthor(authorName);
                    break;

                case 7:
                    System.out.print("Enter Book ID to delete: ");
                    int removeBookId = scanner.nextInt();
                    library.removeBook(removeBookId);
                    break;

                case 8:
                    library.viewAvailableBooks();
                    break;

//                case 9:
//                    library.saveToFile("books.txt");
//                    break;

//                case 10:
//                    library.loadFile("books.txt");
//                    break;

                case 0:
                    running = false;
                    System.out.println("Exiting system...");
                    break;

                default:
                    System.out.println("Invalid choice");
            }
        }
        library.saveToFile("books.txt");
        scanner.close();
    }
}