package andrehsvictor.inscribe.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import andrehsvictor.inscribe.entity.User;
import andrehsvictor.inscribe.entity.VerificationCode;
import andrehsvictor.inscribe.exception.InscribeException;
import andrehsvictor.inscribe.repository.UserRepository;
import andrehsvictor.inscribe.repository.VerificationCodeRepository;

@Service
public class VerificationService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    private UserRepository userRepository;

    private Long expirationMillis = 600_000L;

    public void verify(String code) {
        VerificationCode verificationCode = findByCode(code);
        verificationCode.validate();
        activateUser(verificationCode);
        verificationCodeRepository.delete(verificationCode);
    }

    private void activateUser(VerificationCode verificationCode) {
        User user = verificationCode.getUser();
        user.activate();
        userRepository.save(user);
    }

    private VerificationCode findByCode(String code) {
        return verificationCodeRepository.findByCode(code)
                .orElseThrow(() -> new InscribeException("Invalid verification code", 400));
    }

    public void sendVerificationCode(User user) {
        LocalDateTime expiration = LocalDateTime.now().plus(expirationMillis, ChronoUnit.MILLIS);
        VerificationCode verificationCode = new VerificationCode(user, expiration);
        verificationCode = verificationCodeRepository.save(verificationCode);

        String subject = "Inscribe - Verify your account";
        String text = """
                <h1>Hello, %s!</h1>
                <p>Use the following code to verify your account:</p>
                <h2>%s</h2>
                """.formatted(user.getName(), verificationCode.getCode());

        emailService.send(user.getEmail(), subject, text);
    }
}
