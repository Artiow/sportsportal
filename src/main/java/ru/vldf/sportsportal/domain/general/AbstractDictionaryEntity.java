package ru.vldf.sportsportal.domain.general;

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
public abstract class AbstractDictionaryEntity extends AbstractWordbookEntity {

    @Basic
    @Column(name = "description")
    private String description;
}
