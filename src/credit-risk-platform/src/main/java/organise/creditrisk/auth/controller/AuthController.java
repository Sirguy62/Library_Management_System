package organise.creditrisk.auth.controller;

import organise.creditrisk.auth.service.AuthService;

public class AuthController {

    private final AuthService authService = new AuthService();

    public String login(String email, String password) {
        return authService.login(email, password);
    }
}