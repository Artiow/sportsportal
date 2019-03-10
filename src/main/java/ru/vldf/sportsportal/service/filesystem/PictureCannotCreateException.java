package ru.vldf.sportsportal.service.filesystem;

public class PictureCannotCreateException extends PictureFileException {

    public PictureCannotCreateException(String message) {
        super(message);
    }

    public PictureCannotCreateException(String message, Throwable cause) {
        super(message, cause);
    }
}
