package organise.creditrisk.auth.security;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncoder {

    public String hash(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    public boolean matches(String rawPassword, String hash) {
        return BCrypt.checkpw(rawPassword, hash);
    }
}