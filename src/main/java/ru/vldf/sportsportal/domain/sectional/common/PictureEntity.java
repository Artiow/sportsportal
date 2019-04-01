package ru.vldf.sportsportal.domain.sectional.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.domain.generic.AbstractIdentifiedEntity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
@Entity
@Table(name = "picture", schema = "common")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class PictureEntity extends AbstractIdentifiedEntity {

    @Basic
    @Column(name = "upload_date", nullable = false)
    private Timestamp uploaded;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploader_id", referencedColumnName = "id", nullable = false)
    private UserEntity uploader;
}
