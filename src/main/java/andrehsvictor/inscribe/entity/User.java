package andrehsvictor.inscribe.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import andrehsvictor.inscribe.payload.response.UserResponse;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(of = "id")
@ToString(exclude = { "notes", "refreshToken", "password", "verificationCode" })
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String publicId = UUID.randomUUID().toString().replace("-", "");
    private String name;
    private String email;
    private String password;
    private boolean active = false;
    private boolean deleted = false;

    @OneToMany(mappedBy = "user")
    private Set<Note> notes = new HashSet<>();

    @OneToOne(mappedBy = "user")
    private RefreshToken refreshToken;

    @OneToOne(mappedBy = "user")
    private VerificationCode verificationCode;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    public User() {
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void delete() {
        this.deleted = true;
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public UserResponse toResponse() {
        return new UserResponse(publicId, name, email, active, createdAt.toString(), updatedAt.toString());
    }

}
