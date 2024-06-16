package andrehsvictor.inscribe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import andrehsvictor.inscribe.entity.User;
import andrehsvictor.inscribe.exception.InscribeException;
import andrehsvictor.inscribe.payload.request.SignupRequest;
import andrehsvictor.inscribe.repository.UserRepository;

@Service
public class SignupService {

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void signup(SignupRequest signupRequest) {
        User user = signupRequest.toUser();
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new InscribeException("Email already in use", 400);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
        verificationService.sendVerificationEmail(user);
    }
}
