package ru.vldf.sportsportal.service.filesystem;

/**
 * @author Namednev Artem
 */
public abstract class PictureFileException extends RuntimeException {

    public PictureFileException(String message) {
        super(message);
    }

    public PictureFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
