package ru.vldf.sportsportal.service.filesystem;

public class PictureCannotDeleteException extends PictureFileException {

    public PictureCannotDeleteException(String message) {
        super(message);
    }

    public PictureCannotDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
