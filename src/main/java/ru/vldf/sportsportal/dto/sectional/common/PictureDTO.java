package ru.vldf.sportsportal.dto.sectional.common;

import ru.vldf.sportsportal.dto.generic.AbstractIdentifiedDTO;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.net.URI;
import java.time.LocalDateTime;

public class PictureDTO extends AbstractIdentifiedDTO {

    @NotNull(groups = IdCheck.class)
    @Min(value = 1, groups = IdCheck.class)
    private Integer id;

    @Null(groups = FieldCheck.class)
    private URI url;

    @Null(groups = FieldCheck.class)
    private Long size;

    @Null(groups = FieldCheck.class)
    private LocalDateTime uploaded;


    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public PictureDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public URI getUrl() {
        return url;
    }

    public PictureDTO setUrl(URI url) {
        this.url = url;
        return this;
    }

    public Long getSize() {
        return size;
    }

    public PictureDTO setSize(Long size) {
        this.size = size;
        return this;
    }

    public LocalDateTime getUploaded() {
        return uploaded;
    }

    public PictureDTO setUploaded(LocalDateTime uploaded) {
        this.uploaded = uploaded;
        return this;
    }


    public interface IdCheck {

    }

    public interface FieldCheck {

    }
}
