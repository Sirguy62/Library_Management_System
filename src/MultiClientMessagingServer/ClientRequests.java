package MultiClientMessagingServer;

import java.net.Socket;

public class ClientRequests implements Runnable {

    private Socket socket;

    public ClientRequests(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("ClientRequests thread started " + socket.getInetAddress().getHostName());

    }
}
