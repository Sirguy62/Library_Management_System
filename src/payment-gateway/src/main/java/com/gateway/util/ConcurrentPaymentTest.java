package com.gateway.util;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentPaymentTest {

    public static void main(String[] args) throws Exception {

        String accountId = "ccda91ed-105c-40a9-9818-7f1b07fd8657";

        ExecutorService pool = Executors.newFixedThreadPool(20);

        for (int i = 0; i < 20; i++) {

            int finalI = i;

            pool.submit(() -> {
                try {

                    URL url = new URL("http://localhost:8080/payments");
                    HttpURLConnection con =
                            (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setDoOutput(true);

                    String body =
                            "{ \"accountId\":\"" + accountId + "\"," +
                                    "\"amount\":100," +
                                    "\"idempotencyKey\":\"stress-" + UUID.randomUUID() + "\" }";

                    OutputStream os = con.getOutputStream();
                    os.write(body.getBytes());
                    os.flush();
                    os.close();

//                    int code = con.getResponseCode();

                    var input = con.getInputStream();
                    String response = new String(input.readAllBytes());

                    System.out.println(
                            "Thread-" + finalI + " -> " + response
                    );

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        pool.shutdown();
    }
}