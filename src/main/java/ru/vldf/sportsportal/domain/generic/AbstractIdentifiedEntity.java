package ru.vldf.sportsportal.domain.generic;

import javax.persistence.*;

@MappedSuperclass
public abstract class AbstractIdentifiedEntity implements DomainObject {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
