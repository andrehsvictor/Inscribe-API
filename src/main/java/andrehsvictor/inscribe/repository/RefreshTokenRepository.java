package andrehsvictor.inscribe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import andrehsvictor.inscribe.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Query("SELECT rt FROM RefreshToken rt WHERE rt.value = :value")
    Optional<RefreshToken> findByValue(String value);

}
