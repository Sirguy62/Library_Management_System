package CRUD.Bank.HTTP;

import CRUD.Bank.HTTP.Banking.Account;
import CRUD.Bank.HTTP.Banking.Bank;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class HttpServer {

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(8080)) {

            System.out.println("Server started on port 8080...");

            Bank bank = new Bank();
            bank.loadFromFile("accounts.txt");

            ExecutorService pool = Executors.newFixedThreadPool(10);

            while (true) {
                Socket client = serverSocket.accept();
                pool.submit(() -> handleClient(client, bank));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket client, Bank bank) {

        try {

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));

            OutputStream out = client.getOutputStream();

            String requestLine = in.readLine();
            System.out.println("Request: " + requestLine);

            int statusCode = 200;
            String statusText = "OK";

            String method = "GET";
            String path = "/";
            String query = null;

            if (requestLine != null) {

                String[] firstParts = requestLine.split(" ");
                method = firstParts[0];
                path = firstParts[1];

                if (path.contains("?")) {
                    String[] split = path.split("\\?");
                    path = split[0];
                    query = split[1];
                }
            }

            boolean isPost = method.equals("POST");
            boolean isPut = method.equals("PUT");

            int contentLength = 0;
            String headerLine;

            while ((isPost || isPut) && !(headerLine = in.readLine()).isEmpty()) {

                if (headerLine.startsWith("Content-Length:")) {
                    contentLength = Integer.parseInt(headerLine.split(" ")[1]);
                }
            }

            String requestBody = "";

            if ((isPost || isPut) && contentLength > 0) {

                char[] bodyChars = new char[contentLength];
                in.read(bodyChars, 0, contentLength);
                requestBody = new String(bodyChars);

                System.out.println("BODY: " + requestBody);
            }

            String body;

            if (path.equals("/accounts") && method.equals("GET")) {

                StringBuilder builder = new StringBuilder();
                builder.append("[");

                ArrayList<Account> accounts = bank.getAccounts();

                for (int i = 0; i < accounts.size(); i++) {

                    Account acc = accounts.get(i);

                    builder.append("{")
                            .append("\"accountNumber\":").append(acc.getAccountNumber()).append(",")
                            .append("\"owner\":\"").append(acc.getOwnerName()).append("\",")
                            .append("\"balance\":").append(acc.getBalance())
                            .append("}");

                    if (i != accounts.size() - 1) {
                        builder.append(",");
                    }
                }

                builder.append("]");
                body = builder.toString();
            }

            else if (path.equals("/account") && method.equals("GET")) {

                int number = -1;

                if (query != null && query.startsWith("number=")) {
                    number = Integer.parseInt(query.split("=")[1]);
                }

                Account acc = bank.findAccount(number);

                if (acc == null) {
                    statusCode = 404;
                    statusText = "Not Found";
                    body = "{\"error\":\"Account not found\"}";
                } else {
                    body = "{"
                            + "\"accountNumber\":" + acc.getAccountNumber() + ","
                            + "\"owner\":\"" + acc.getOwnerName() + "\","
                            + "\"balance\":" + acc.getBalance()
                            + "}";
                }
            }

            else if (path.equals("/account/create") && method.equals("POST")) {

                String cleaned = requestBody
                        .replace("{", "")
                        .replace("}", "")
                        .replace("\"", "");

                String[] fields = cleaned.split(",");

                int number = Integer.parseInt(fields[0].split(":")[1]);
                String owner = fields[1].split(":")[1];

                Account existing = bank.findAccount(number);

                if (existing != null) {
                    statusCode = 400;
                    statusText = "Bad Request";
                    body = "{\"error\":\"Account already exists\"}";
                } else {
                    Account newAcc = new Account(number, owner);
                    bank.createAccount(newAcc);
                    bank.saveToFile("accounts.txt");

                    statusCode = 201;
                    statusText = "Created";
                    body = "{\"status\":\"Account Created\"}";
                }
            }

            else if (path.equals("/account/delete") && method.equals("DELETE")) {

                int number = -1;

                if (query != null && query.startsWith("number=")) {
                    number = Integer.parseInt(query.split("=")[1]);
                }

                Account acc = bank.findAccount(number);

                if (acc == null) {
                    statusCode = 404;
                    statusText = "Not Found";
                    body = "{\"error\":\"Account not found\"}";
                } else {
                    bank.removeAccount(number);
                    bank.saveToFile("accounts.txt");

                    body = "{\"status\":\"Account Deleted\"}";
                }
            }

            else if (path.equals("/account/update") && method.equals("PUT")) {

                String cleaned = requestBody
                        .replace("{", "")
                        .replace("}", "")
                        .replace("\"", "");

                String[] fields = cleaned.split(",");

                int number = Integer.parseInt(fields[0].split(":")[1]);
                String owner = fields[1].split(":")[1];

                boolean updated = bank.updateAccount(number, owner);

                if (!updated) {
                    statusCode = 404;
                    statusText = "Not Found";
                    body = "{\"error\":\"Account not found\"}";
                } else {
                    bank.saveToFile("accounts.txt");
                    body = "{\"status\":\"Account Updated\"}";
                }
            }

            else {
                statusCode = 404;
                statusText = "Not Found";
                body = "{\"error\":\"Route not found\"}";
            }

            String response =
                    "HTTP/1.1 " + statusCode + " " + statusText + "\r\n" +
                            "Content-Type: application/json\r\n\r\n" +
                            body;

            out.write(response.getBytes());
            out.flush();

            client.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

