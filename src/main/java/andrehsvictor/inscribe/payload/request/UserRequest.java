package andrehsvictor.inscribe.payload.request;

import andrehsvictor.inscribe.entity.User;
import lombok.Data;

@Data
public class UserRequest {
    
    private String name;
    private String email;
    private String password;

    public User toEntity() {
        return new User(name, email, password);
    }
}
