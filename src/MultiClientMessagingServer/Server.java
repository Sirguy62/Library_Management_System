package MultiClientMessagingServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Server {

    public static void main(String[] args) {

        ExecutorService pool = Executors.newFixedThreadPool(20);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {

            System.out.println("Shutting down server...");

            pool.shutdown();

            try {
                if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                    pool.shutdownNow();
                }
            } catch (InterruptedException e) {
                pool.shutdownNow();
            }
        }));

        try (ServerSocket serverSocket = new ServerSocket(500)) {

            System.out.println("Chat Server Started...");

            while (true) {

                Socket socket = serverSocket.accept();

                System.out.println(
                        "New connection from "
                                + socket.getInetAddress().getHostAddress()
                );

                ClientRequests handler = new ClientRequests(socket);

                pool.submit(handler);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}