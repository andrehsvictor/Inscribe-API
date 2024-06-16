package andrehsvictor.inscribe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import andrehsvictor.inscribe.exception.InscribeException;
import andrehsvictor.inscribe.payload.Payload;
import andrehsvictor.inscribe.payload.request.RefreshRequest;
import andrehsvictor.inscribe.payload.request.SigninRequest;
import andrehsvictor.inscribe.payload.response.TokenResponse;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private JwtService jwtService;

    public Payload<TokenResponse> signin(SigninRequest signinRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = createAuthenticationToken(signinRequest);
        Authentication authentication = tryToAuthenticate(authenticationToken);
        TokenResponse tokenResponse = jwtService.generate(authentication);
        return createPayload("Authentication successful", tokenResponse);
    }

    public Payload<TokenResponse> refresh(RefreshRequest refreshRequest) {
        TokenResponse tokenResponse = jwtService.generate(refreshRequest.getRefreshToken());
        return createPayload("Token refreshed successfully", tokenResponse);
    }

    public Payload<Void> verify(String code) {
        verificationService.verify(code);
        return createPayload("Email verified successfully", null);
    }

    private <T> Payload<T> createPayload(String message, T data) {
        return Payload.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    private UsernamePasswordAuthenticationToken createAuthenticationToken(SigninRequest signInRequest) {
        return new UsernamePasswordAuthenticationToken(signInRequest.getEmail(),
                signInRequest.getPassword());
    }

    private Authentication tryToAuthenticate(Authentication authentication) {
        try {
            return authenticationManager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new InscribeException("Invalid email or password", 401);
        } catch (DisabledException e) {
            throw new InscribeException("User is disabled", 401);
        }
    }
}
