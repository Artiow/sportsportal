package ru.vldf.sportsportal.domain.sectional.common;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.domain.general.AbstractIdentifiedEntity;

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
@Table(name = "location", schema = "common")
public class LocationEntity extends AbstractIdentifiedEntity {

    @Basic
    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Basic
    @Column(name = "longitude", nullable = false)
    private Double longitude;
}
