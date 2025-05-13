package ua.gov.court.supreme.sevhelper.exception;

public class DataAccessException extends BaseException {
    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
