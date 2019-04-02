package ru.vldf.sportsportal.service.filesystem.model;

import lombok.Getter;
import org.apache.logging.log4j.util.Strings;
import org.springframework.util.Assert;

/**
 * @author Namednev Artem
 */
@Getter
public final class PictureSize {

    private final String name;
    private final short width;
    private final short height;
    private final double factor;


    private PictureSize(short width, short height) {
        this.name = Strings.EMPTY;
        this.width = width;
        this.height = height;
        this.factor = ((double) this.width) / ((double) this.height);
    }

    private PictureSize(String name, short width, short height) {
        Assert.hasText(name, "Picture size value must not be blank");
        this.name = name.trim().toLowerCase();
        this.width = width;
        this.height = height;
        this.factor = ((double) this.width) / ((double) this.height);
    }


    public static PictureSize of(short width, short height) {
        return new PictureSize(width, height);
    }

    public static PictureSize of(String name, short width, short height) {
        return new PictureSize(name, width, height);
    }


    @Override
    public String toString() {
        return getName();
    }
}
