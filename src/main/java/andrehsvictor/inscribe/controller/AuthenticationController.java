package andrehsvictor.inscribe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import andrehsvictor.inscribe.payload.Payload;
import andrehsvictor.inscribe.payload.request.RefreshRequest;
import andrehsvictor.inscribe.payload.request.ResendRequest;
import andrehsvictor.inscribe.payload.request.SigninRequest;
import andrehsvictor.inscribe.payload.request.SignupRequest;
import andrehsvictor.inscribe.payload.response.TokenResponse;
import andrehsvictor.inscribe.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/signin")
    public ResponseEntity<Payload<TokenResponse>> signin(@RequestBody SigninRequest signinRequest) {
        return ResponseEntity.ok(authenticationService.signin(signinRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<Payload<TokenResponse>> refresh(@RequestBody RefreshRequest refreshToken) {
        return ResponseEntity.ok(authenticationService.refresh(refreshToken));
    }

    @PostMapping("/signup")
    public ResponseEntity<Payload<Void>> signup(@RequestBody SignupRequest signupRequest) {
        return ResponseEntity.ok(authenticationService.signup(signupRequest));
    }

    @GetMapping("/verify")
    public ResponseEntity<Payload<Void>> verify(@RequestParam(required = true) String code) {
        return ResponseEntity.ok(authenticationService.verify(code));
    }

    @PostMapping("/resend")
    public ResponseEntity<Payload<Void>> resend(@RequestBody ResendRequest code) {
        return ResponseEntity.ok(authenticationService.resend(code.getCode()));
    }
}
