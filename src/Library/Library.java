package Library;

import java.io.*;
import java.util.ArrayList;

public class Library {

    ArrayList<Book> books;

    public Library() {
        books = new ArrayList<>();
    }

    public void addBook(Book book) {

        for (Book b : books) {
            if (b.getId() == book.getId()) {
                System.out.println("Book with this ID already exists!");
                return;
            }
        }
        books.add(book);
        System.out.println("Book added successfully.");
    }

    public void viewBooks() {
        if (books.isEmpty()) {
            System.out.println("No books in library.");
            return;
        }
        for (Book book : books) {
            System.out.println(book);
        }
    }

    public void borrowBook(int id) {
        for (Book book : books) {
            if (book.getId() == id) {
                if (!book.isBorrowed()) {
                    book.setBorrowed(true);
                    System.out.println("Book Borrowed Successfully: " + book);
                } else {
                    System.out.println("Book already borrowed!");
                }
                return;
            }
        }
        System.out.println("Book not found!");
    }

    public void returnBook(int id) {
        for (Book book : books) {
            if (book.getId() == id) {
                if (book.isBorrowed()) {
                    book.setBorrowed(false);
                    System.out.println("Book Returned Successfully: " + book);
                } else {
                    System.out.println("This book was not borrowed.");
                }
                return;
            }
        }
        System.out.println("Book not found!");
    }

    public void searchBook(String title) {
        if (title == null || title.trim().isEmpty()) {
            System.out.println("title keyword cannot be empty.");
            return;
        }
        boolean found = false;
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                System.out.println(book);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No matching books found.");
        }
    }

    public void searchAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            System.out.println("Author keyword cannot be empty.");
            return;
        }
        boolean found = false;
        for (Book book : books) {
            if (book.getAuthor().toLowerCase().contains(author.toLowerCase())) {
                System.out.println(book);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No matching author found.");
        }
    }

    public void removeBook(int id) {
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            if (book.getId() == id) {
                books.remove(i);
                System.out.println("Book Removed Successfully: " + book);
                return;
            }
        }
        System.out.println("Book not found!");
    }

    public void viewAvailableBooks() {
        boolean found = false;
        for (Book book : books) {

            if (!book.isBorrowed()) {
                System.out.println(book);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No available books.");
        }
    }


    public void saveToFile(String path) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (Book book : books) {
                String line =
                        book.getId() + "," +
                                book.getTitle() + "," +
                                book.getAuthor() + "," +
                                book.isBorrowed();
                bw.write(line);
                bw.newLine();
            }
            System.out.println("Books saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving file.");
        }
    }

    public void loadFile(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;

         while ((line = br.readLine()) != null) {

             String[] split = line.split(",");
             int id = Integer.parseInt(split[0]);
             String title = split[1];
             String author = split[2];
             boolean borrowed = Boolean.parseBoolean(split[3]);

             Book book = new Book(id, title, author);
             book.setBorrowed(borrowed);
             books.add(book);
             System.out.println("Book Loaded Successfully: " + book);
         }
        }catch (FileNotFoundException e) {
            System.out.println("No previous data found.");
        } catch (IOException e) {
            System.out.println("Error opening file.");
        }
    }























}
