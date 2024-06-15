package andrehsvictor.inscribe.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NoteResponse {

    @JsonProperty(value = "id", index = 0)
    private String publicId;

    private String title;
    private String slug;
    private String content;
    private String createdAt;
    private String updatedAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserResponse user;

    public void excludeUser() {
        this.user = null;
    }
}
