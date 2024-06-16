package andrehsvictor.inscribe.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import andrehsvictor.inscribe.payload.Payload;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Payload<Void>> handleException(Exception e) {
        return ResponseEntity.status(500)
                .body(Payload.<Void>builder()
                        .success(false)
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(InscribeException.class)
    public ResponseEntity<Payload<Void>> handleInscribeException(InscribeException e) {
        return ResponseEntity.status(e.getCode())
                .body(Payload.<Void>builder()
                        .success(false)
                        .message(e.getMessage())
                        .build());
    }
}
