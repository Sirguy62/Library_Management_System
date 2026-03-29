package organise.creditrisk.auth.service;

import organise.creditrisk.auth.model.User;
import organise.creditrisk.auth.repository.UserRepository;
import organise.creditrisk.auth.security.JwtUtil;
import organise.creditrisk.auth.security.PasswordEncoder;

public class AuthService {

    private final UserRepository userRepository = new UserRepository();
    private final PasswordEncoder encoder = new PasswordEncoder();
    private final JwtUtil jwtUtil = new JwtUtil();

    public String login(String email, String password) {

        User user = userRepository.findByEmail(email);

        if (user == null)
            throw new RuntimeException("User not found");

        if (!encoder.matches(password, user.getPasswordHash()))
            throw new RuntimeException("Invalid credentials");

        return jwtUtil.generateToken(user.getEmail(), user.getRole().name());
    }
}