package andrehsvictor.inscribe.service;

import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import andrehsvictor.inscribe.payload.response.TokenResponse;

@Service
public class JwtService {

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private RefreshTokenService refreshTokenService;

    private Long expirationMillis = 900_000L;

    public TokenResponse generate(Authentication authentication) {
        Instant expiration = Instant.now().plusMillis(expirationMillis);
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(authentication.getName())
                .expiresAt(expiration)
                .issuedAt(Instant.now())
                .build();

        String accessToken = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        String refreshToken = refreshTokenService.generate(authentication);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .createdAt(claims.getIssuedAt().toString())
                .expiresAt(claims.getExpiresAt().toString())
                .build();
    }

    public TokenResponse generate(String refreshToken) {
        Authentication authentication = refreshTokenService.retrieveAuthenticationByRefreshToken(refreshToken);
        return generate(authentication);
    }
}
