package andrehsvictor.inscribe.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import andrehsvictor.inscribe.exception.InscribeException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(of = "id")
@ToString(exclude = "user")
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String value = UUID.randomUUID().toString().replace("-", "");

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime expiresAt;
    private LocalDateTime updatedAt = LocalDateTime.now();

    public RefreshToken() {
    }

    public RefreshToken(User user, LocalDateTime expiresAt) {
        this.user = user;
        this.expiresAt = expiresAt;
    }

    public void refresh(LocalDateTime expiresAt) {
        this.value = UUID.randomUUID().toString().replace("-", "");
        this.expiresAt = expiresAt;
        this.updatedAt = LocalDateTime.now();
    }

    public void validate() {
        if (LocalDateTime.now().isAfter(expiresAt)) {
            throw new InscribeException("Refresh token expired", 401);
        }
    }

}
