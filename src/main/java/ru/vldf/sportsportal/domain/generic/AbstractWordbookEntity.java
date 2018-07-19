package ru.vldf.sportsportal.domain.generic;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractWordbookEntity extends AbstractIdentifiedEntity {

    @Basic
    @Column(name = "code", nullable = false)
    private String code;

    @Basic
    @Column(name = "name", nullable = false)
    private String name;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
