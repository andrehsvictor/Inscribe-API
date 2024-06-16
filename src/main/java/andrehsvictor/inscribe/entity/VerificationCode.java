package andrehsvictor.inscribe.entity;

import java.time.LocalDateTime;

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
@Table(name = "verification_codes")
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code = String.valueOf((int) (Math.random() * 900000) + 100000);

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime expiresAt;

    public VerificationCode() {
    }

    public VerificationCode(User user, LocalDateTime expiresAt) {
        this.user = user;
        this.expiresAt = expiresAt;
    }

    public void validate() {
        if (LocalDateTime.now().isAfter(expiresAt)) {
            throw new InscribeException("Verification code expired", 401);
        }
    }
}
