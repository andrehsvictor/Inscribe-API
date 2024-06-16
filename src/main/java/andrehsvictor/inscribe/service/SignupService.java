package andrehsvictor.inscribe.service;

import org.springframework.beans.factory.annotation.Autowired;

import andrehsvictor.inscribe.entity.User;
import andrehsvictor.inscribe.payload.request.SignupRequest;
import andrehsvictor.inscribe.repository.UserRepository;

public class SignupService {
    
    @Autowired
    private VerificationService verificationService;

    @Autowired
    private UserRepository userRepository;

    public void signup(SignupRequest signupRequest) {
        User user = signupRequest.toUser();
        user = userRepository.save(user);
        verificationService.sendVerificationCode(user);
    }
}
