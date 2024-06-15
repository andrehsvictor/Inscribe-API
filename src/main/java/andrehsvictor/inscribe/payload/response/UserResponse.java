package andrehsvictor.inscribe.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {
    @JsonProperty(value = "id", index = 0)
    private String publicId;
    private String name;
    private String email;
    private boolean active;
    private String createdAt;
    private String updatedAt;
}
