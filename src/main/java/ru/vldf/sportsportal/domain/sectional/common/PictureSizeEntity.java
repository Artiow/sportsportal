package ru.vldf.sportsportal.domain.sectional.common;

import ru.vldf.sportsportal.domain.generic.AbstractDictionaryEntity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "picture_size", schema = "common")
public class PictureSizeEntity extends AbstractDictionaryEntity {

    @Basic
    @Column(name = "width", nullable = false)
    private Short width;

    @Basic
    @Column(name = "height", nullable = false)
    private Short height;


    public Short getWidth() {
        return width;
    }

    public void setWidth(Short width) {
        this.width = width;
    }

    public Short getHeight() {
        return height;
    }

    public void setHeight(Short height) {
        this.height = height;
    }
}
