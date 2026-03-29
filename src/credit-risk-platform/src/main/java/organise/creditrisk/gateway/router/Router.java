package organise.creditrisk.gateway.router;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import organise.creditrisk.auth.controller.AuthController;
import organise.creditrisk.gateway.filter.AuthFilter;
import organise.creditrisk.loan.controller.LoanController;
import organise.creditrisk.payment.controller.PaymentController;

import java.io.IOException;
import java.io.OutputStream;

public class Router implements HttpHandler {

    private final AuthController authController = new AuthController();
    private final AuthFilter authFilter = new AuthFilter();
    private final LoanController loanController = new LoanController();
    private final PaymentController paymentController = new PaymentController();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        try {

            if (path.equals("/auth/login") && method.equals("POST")) {

                String token = authController.login("admin@test.com", "password");

                sendResponse(exchange, token);

                return;
            }

            authFilter.doFilter(exchange);

            if (path.equals("/loans") && method.equals("GET")) {

                authFilter.doFilter(exchange);

                String response = loanController.getLoans();

                sendResponse(exchange, response);
                return;
            }

            if (path.equals("/payment") && method.equals("POST")) {

                authFilter.doFilter(exchange);

                paymentController.pay(1L, 500.0, "abc-123");

                sendResponse(exchange, "Payment done");
                return;
            }

            sendResponse(exchange, "Route not found");

        } catch (Exception e) {

            sendResponse(exchange, "ERROR: " + e.getMessage());
        }
    }

    private void sendResponse(HttpExchange exchange, String response) throws IOException {

        exchange.sendResponseHeaders(200, response.getBytes().length);

        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}