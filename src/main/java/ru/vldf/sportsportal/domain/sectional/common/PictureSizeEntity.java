package ru.vldf.sportsportal.domain.sectional.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.domain.general.AbstractDictionaryEntity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
@Entity
@Table(name = "picture_size", schema = "common")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class PictureSizeEntity extends AbstractDictionaryEntity {

    @Basic
    @Column(name = "width", nullable = false)
    private Short width;

    @Basic
    @Column(name = "height", nullable = false)
    private Short height;

    @Basic
    @Column(name = "default", nullable = false)
    private Boolean isDefault;
}
