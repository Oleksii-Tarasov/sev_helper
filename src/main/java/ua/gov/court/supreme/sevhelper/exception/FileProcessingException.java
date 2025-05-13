package ua.gov.court.supreme.sevhelper.exception;

public class FileProcessingException extends BaseException {
    public FileProcessingException(String message) {
        super(message);
    }

    public FileProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
