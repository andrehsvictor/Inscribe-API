package andrehsvictor.inscribe.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import andrehsvictor.inscribe.payload.response.NoteResponse;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "notes")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String publicId = UUID.randomUUID().toString().replace("-", "");
    private String title;
    private String slug;
    private String content;
    private boolean deleted = false;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Note(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public void delete() {
        this.deleted = true;
    }

    public NoteResponse toResponse() {
        return new NoteResponse(publicId, title, slug, content, createdAt.toString(), updatedAt.toString(), user.toResponse());
    }

}
