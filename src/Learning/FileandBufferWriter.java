import java.io.*;

public class FileandBufferWriter {

    public static void main(String[] args) {

        String filePath = "text.txt";
//        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String text = """
                I follow layered architecture. I separate concerns into controller,
                service, and repository layers. I design domain models first, define
                RESTful endpoints, apply validation and business logic in the service
                layer, and isolate database access in the repository layer. I also 
                handle cross-cutting concerns like authentication and exception handling
                """;


        try (FileWriter file = new FileWriter(filePath)){
            file.write(text);
            System.out.println("File has been written");
        }
        catch ( FileNotFoundException e){
            System.out.println("Could not locate file path....");
        }
        catch (IOException e){
            System.out.println("Invalid file format...");
        }
        catch (Exception e) {
            System.out.println("Something went wrong");
        }
        finally {
            System.out.println("This always runs");
        }


        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            System.out.println("This file Exists");
            String line;
            while((line = reader.readLine()) != null){
                System.out.println(line);
            }

        }catch (FileNotFoundException e){
            System.out.println("Invalid file path or file");
        }
        catch (IOException e){
            System.out.println("Something went wrong");
        }
        finally {
            System.out.println("This will always run");
        }

    }
}
