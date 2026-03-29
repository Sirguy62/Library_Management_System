import javax.sound.sampled.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class AudioPlayer {
    public static void main() {
        //How to Play Audio with java (.wav, .au, .aiff)

        String filePath = "A Caring Friend.wav";
        File file = new File(filePath);

        try( Scanner scanner = new Scanner(System.in);AudioInputStream audioStream = AudioSystem.getAudioInputStream(file)) {

            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            clip.start();

            String response = "";

            while (!response.equals("Q")) {

                System.out.println("P = Play");
                System.out.println("S = Stop");
                System.out.println("R = Reset");
                System.out.println("Q = Quit");
                System.out.print("Enter an option");

                response = scanner.nextLine().toUpperCase();

                switch (response) {
                    case "P" -> clip.start();
                    case "S" -> clip.stop();
                    case "R" -> clip.setMicrosecondPosition(0);
                    case "Q" -> clip.close();
                    default -> System.out.println("Invalid input");
                }
            }

            System.out.println("No problems detected");
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        catch (UnsupportedAudioFileException | LineUnavailableException e){
            System.out.println("Audio file format or file path not supported");
        }
        catch (IOException e){
            System.out.println("Something went wrong");
        }
        finally {
            System.out.println("This will always run");
        }
    }
}
