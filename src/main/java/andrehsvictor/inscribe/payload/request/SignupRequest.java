package andrehsvictor.inscribe.payload.request;

import andrehsvictor.inscribe.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = "password")
public class SignupRequest {
    
    @Size(min = 3, max = 20, message = "Name must be between 3 and 20 characters")
    private String name;

    @Size(min = 6, max = 50, message = "Password must be between 6 and 50 characters")
    @Email(message = "Invalid email")
    private String email;

    @Size(min = 6, max = 50, message = "Password must be between 6 and 50 characters")
    private String password;

    public User toUser() {
        return new User(name, email, password);
    }
}
