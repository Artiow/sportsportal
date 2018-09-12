package ru.vldf.sportsportal.dto.sectional.lease;

import ru.vldf.sportsportal.dto.generic.AbstractDictionaryDTO;

import javax.validation.constraints.*;

public class SportDTO extends AbstractDictionaryDTO {

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


    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public SportDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public SportDTO setCode(String code) {
        this.code = code;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public SportDTO setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public SportDTO setDescription(String description) {
        this.description = description;
        return this;
    }


    public interface IdCheck {

    }

    public interface CodeCheck {

    }
}
