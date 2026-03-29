package organise.creditrisk.gateway.server;

import com.sun.net.httpserver.HttpServer;
import organise.creditrisk.gateway.router.Router;

import java.net.InetSocketAddress;

public class ApiServer {

    public void start() throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/", new Router());

        server.setExecutor(null);
        server.start();

        System.out.println("API Gateway started on port 8080");
    }
}