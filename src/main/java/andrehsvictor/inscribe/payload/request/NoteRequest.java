package andrehsvictor.inscribe.payload.request;

import andrehsvictor.inscribe.entity.Note;
import andrehsvictor.inscribe.entity.User;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NoteRequest {
    @Size(min = 5, max = 50, message = "Title must be between 5 and 50 characters")
    private String title;

    @Size(min = 10, max = 500, message = "Content must be between 10 and 500 characters")
    private String content;

    public Note toEntity(User user) {
        return new Note(title, content, user);
    }
}
