package ru.vldf.sportsportal.dto.sectional.common;

import ru.vldf.sportsportal.dto.generic.AbstractDictionaryDTO;

import javax.validation.constraints.*;

public class PictureSizeDTO extends AbstractDictionaryDTO {

    @Null(groups = CodeCheck.class)
    @NotNull(groups = IdCheck.class)
    @Min(value = 1, groups = IdCheck.class)
    private Integer id;

    @NotBlank(groups = CodeCheck.class)
    @Size(min = 1, max = 45, groups = CodeCheck.class)
    private String code;

    @Null(groups = CodeCheck.class)
    private String name;

    @Null(groups = CodeCheck.class)
    private String description;

    @Null(groups = CodeCheck.class)
    private Short width;

    @Null(groups = CodeCheck.class)
    private Short height;


    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public PictureSizeDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public PictureSizeDTO setCode(String code) {
        this.code = code;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public PictureSizeDTO setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public PictureSizeDTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public Short getWidth() {
        return width;
    }

    public PictureSizeDTO setWidth(Short width) {
        this.width = width;
        return this;
    }

    public Short getHeight() {
        return height;
    }

    public PictureSizeDTO setHeight(Short height) {
        this.height = height;
        return this;
    }


    public interface IdCheck {

    }

    public interface CodeCheck {

    }
}
