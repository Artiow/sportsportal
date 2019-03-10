package ru.vldf.sportsportal.service.filesystem;

public class PictureNotFoundException extends PictureFileException {

    public PictureNotFoundException(String message) {
        super(message);
    }

    public PictureNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
