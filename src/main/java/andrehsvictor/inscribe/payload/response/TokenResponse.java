package andrehsvictor.inscribe.payload.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private String createdAt;
    private String expiresAt;
}
