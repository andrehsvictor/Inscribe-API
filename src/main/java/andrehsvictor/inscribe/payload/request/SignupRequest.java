package andrehsvictor.inscribe.payload.request;

import andrehsvictor.inscribe.entity.User;
import lombok.Data;

@Data
public class SignupRequest {
    private String name;
    private String email;
    private String password;

    public User toUser() {
        return new User(name, email, password);
    }
}
