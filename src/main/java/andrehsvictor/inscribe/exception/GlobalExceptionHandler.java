package andrehsvictor.inscribe.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
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

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return ResponseEntity.status(400)
                .body(Payload.<Void>builder()
                        .success(false)
                        .message(ex.getBindingResult().getFieldError().getDefaultMessage())
                        .build());
    }
}
