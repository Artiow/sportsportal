package ru.vldf.sportsportal.dto;

import ru.vldf.sportsportal.dto.generic.AbstractDictionaryDTO;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RoleDTO extends AbstractDictionaryDTO {

    @NotNull(groups = IdCheck.class)
    @Min(value = 1, groups = IdCheck.class)
    private Integer id;

    @NotNull(groups = FieldCheck.class)
    @Size(min = 1, max = 45, groups = FieldCheck.class)
    private String code;

    @NotNull(groups = FieldCheck.class)
    @Size(min = 1, max = 45, groups = FieldCheck.class)
    private String name;

    @Size(min = 1, max = 90, groups = FieldCheck.class)
    private String description;


    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public RoleDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public RoleDTO setCode(String code) {
        this.code = code;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public RoleDTO setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public RoleDTO setDescription(String description) {
        this.description = description;
        return this;
    }


    public interface IdCheck {

    }

    public interface FieldCheck {

    }
}
