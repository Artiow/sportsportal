package ru.vldf.sportsportal.domain.generic;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
@MappedSuperclass
public abstract class AbstractWordbookEntity extends AbstractIdentifiedEntity {

    @Basic
    @Column(name = "code", nullable = false)
    private String code;

    @Basic
    @Column(name = "name", nullable = false)
    private String name;
}
