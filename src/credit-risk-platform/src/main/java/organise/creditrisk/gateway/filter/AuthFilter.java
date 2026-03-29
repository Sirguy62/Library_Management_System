package organise.creditrisk.gateway.filter;

import com.sun.net.httpserver.HttpExchange;

import java.util.List;

public class AuthFilter {

    public void doFilter(HttpExchange exchange) {

        List<String> headers = exchange.getRequestHeaders().get("Authorization");

        if (headers == null || headers.isEmpty())
            throw new RuntimeException("Missing Authorization Header");

        String token = headers.get(0);

        if (!token.startsWith("Bearer "))
            throw new RuntimeException("Invalid Token Format");
    }
}