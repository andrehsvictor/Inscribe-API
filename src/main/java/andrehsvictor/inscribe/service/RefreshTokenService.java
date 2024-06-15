package andrehsvictor.inscribe.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import andrehsvictor.inscribe.entity.RefreshToken;
import andrehsvictor.inscribe.entity.User;
import andrehsvictor.inscribe.exception.InscribeException;
import andrehsvictor.inscribe.repository.RefreshTokenRepository;
import andrehsvictor.inscribe.repository.UserRepository;
import andrehsvictor.inscribe.security.UserDetailsImpl;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    private Long expirationMillis = 2_592_000_000L;

    public String generate(Authentication authentication) {
        User user = findUserByEmail(authentication);
        LocalDateTime expiration = LocalDateTime.now().plus(expirationMillis, ChronoUnit.MILLIS);
        if (user.getRefreshToken() != null) {
            RefreshToken refreshToken = user.getRefreshToken();
            refreshToken.validate();
            refreshToken.refresh(expiration);
            refreshToken = refreshTokenRepository.save(refreshToken);
            return refreshToken.getValue();
        }
        RefreshToken refreshToken = new RefreshToken(user, expiration);
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken.getValue();
    }

    public Authentication retrieveAuthenticationByRefreshToken(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByValue(refreshToken)
                .orElseThrow(() -> new InscribeException("Invalid refresh token", 401));
        token.validate();
        UserDetails userDetails = new UserDetailsImpl(token.getUser());
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private User findUserByEmail(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new InscribeException("User not found", 404));
    }
}
