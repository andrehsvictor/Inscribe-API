package andrehsvictor.inscribe.exception;

public class InscribeException extends RuntimeException {
    
    private int code;

    public InscribeException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
