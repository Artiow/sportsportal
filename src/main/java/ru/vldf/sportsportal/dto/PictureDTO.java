package ru.vldf.sportsportal.dto;

import ru.vldf.sportsportal.dto.generic.AbstractIdentifiedDTO;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.time.LocalDateTime;

public class PictureDTO extends AbstractIdentifiedDTO {

    @NotNull(groups = IdCheck.class)
    @Min(value = 1, groups = IdCheck.class)
    private Integer id;

    private URI uri;
    private Long size;
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

    public URI getUri() {
        return uri;
    }

    public PictureDTO setUri(URI uri) {
        this.uri = uri;
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
}
