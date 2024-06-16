package andrehsvictor.inscribe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import andrehsvictor.inscribe.entity.User;
import andrehsvictor.inscribe.entity.VerificationCode;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    @Query("SELECT vc FROM VerificationCode vc WHERE vc.code = :code")
    Optional<VerificationCode> findByCode(String code);

    @Query("SELECT vc FROM VerificationCode vc WHERE vc.user = :user")
    Optional<VerificationCode> findByUser(User user);

    @Query("DELETE FROM VerificationCode vc WHERE vc.user = :user")
    void deleteByUser(User user);
}
