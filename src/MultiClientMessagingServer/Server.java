package MultiClientMessagingServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

   public static void main(String[] args) {

       try (ServerSocket serverSocket = new ServerSocket(500)) {

           while (true) {
               Socket socket = serverSocket.accept();
               System.out.println(
                       "New connection from " + socket.getInetAddress().getHostAddress()
               );

               ClientRequests clientRequests = new ClientRequests(socket);
               Thread thread = new Thread(clientRequests);

               thread.start();
           }

       } catch (IOException e) {
           e.printStackTrace();
       }
   }
}
