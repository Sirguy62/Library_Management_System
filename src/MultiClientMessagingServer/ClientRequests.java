package MultiClientMessagingServer;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class ClientRequests implements Runnable {

    private Socket socket;
    private PrintWriter out;
    private String username;

    private static List<ClientRequests> clients =
            Collections.synchronizedList(new ArrayList<>());

    private static Map<String, ClientRequests> clientMap =
            Collections.synchronizedMap(new HashMap<>());

    public ClientRequests(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        System.out.println("Client thread started for "
                + socket.getInetAddress().getHostAddress());

        try {

            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream(), true);

            out.println("Enter username:");

            while(true){

                username = in.readLine();

                if(username == null) return;

                if(!UserDAO.userExists(username)){
                    UserDAO.createUser(username);
                    break;
                } else {
                    send("Welcome back " + username);
                    break;
                }
            }

//            while (true) {
//                username = in.readLine();
//
//                if (username == null) return;
//
//                synchronized (clientMap) {
//                    if (!clientMap.containsKey(username)) {
//                        clientMap.put(username, this);
//                        break;
//                    } else {
//                        out.println("Username taken. Try another:");
//                    }
//                }
//            }

            clients.add(this);
            broadcast(username + " joined the server");

            List<String> history = MessageDAO.getRecentMessages(20);

            for (String msg : history) {
                send(msg);
            }

            String message;

            while ((message = in.readLine()) != null) {

                if (message.equals("/quit")) {
                    send("Disconnecting...");
                    socket.close();
                    break;
                }

                if (message.equals("/list")) {
                    listUsers();
                    continue;
                }

                if (message.startsWith("@")) {

                    int spaceIndex = message.indexOf(" ");
                    if (spaceIndex == -1) {
                        send("Invalid private message format");
                        continue;
                    }

                    String targetUser = message.substring(1, spaceIndex);
                    String privateMsg = message.substring(spaceIndex + 1);

                    ClientRequests target = clientMap.get(targetUser);

                    if (target != null) {
                        target.send("(private) " + username + ": " + privateMsg);
                    } else {
                        send("User not found");
                    }

                    continue;
                }

                MessageDAO.saveMessage(username, message);
                broadcast(username + ": " + message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            clients.remove(this);
            clientMap.remove(username);
            broadcast(username + " left the server");

            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Client disconnected "
                    + socket.getInetAddress().getHostAddress());
        }
    }

    private void broadcast(String message) {
        synchronized (clients) {
            for (ClientRequests client : clients) {
                client.send(message);
            }
        }
    }

    private void send(String message) {
        out.println(message);
    }

    private void listUsers() {
        StringBuilder sb = new StringBuilder("Online users: ");
        synchronized (clientMap) {
            for (String user : clientMap.keySet()) {
                sb.append(user).append(" ");
            }
        }
        send(sb.toString());
    }
}