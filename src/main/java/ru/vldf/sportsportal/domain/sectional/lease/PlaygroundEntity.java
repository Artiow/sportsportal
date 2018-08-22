package ru.vldf.sportsportal.domain.sectional.lease;

import ru.vldf.sportsportal.domain.generic.AbstractVersionedEntity;
import ru.vldf.sportsportal.domain.sectional.common.PictureEntity;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
@Table(name = "playground", schema = "lease")
public class PlaygroundEntity extends AbstractVersionedEntity {

    @Basic
    @Column(name = "name", nullable = false)
    private String name;

    @Basic
    @Column(name = "address", nullable = false)
    private String address;

    @Basic
    @Column(name = "phone", nullable = false)
    private String phone;

    @Basic
    @Column(name = "rate", nullable = false)
    private Integer rate;

    @Basic
    @Column(name = "opening", nullable = false)
    private Timestamp opening;

    @Basic
    @Column(name = "closing", nullable = false)
    private Timestamp closing;

    @Basic
    @Column(name = "half_hour_available", nullable = false)
    private Boolean halfHourAvailable = true;

    @Basic
    @Column(name = "full_hour_required", nullable = false)
    private Boolean fullHourRequired = false;

    @Basic
    @Column(name = "cost", nullable = false)
    private Integer cost;

    @OneToMany(mappedBy = "playground")
    private Collection<OrderEntity> orders;

    @ManyToMany
    @JoinTable(
            schema = "lease",
            name = "specialization",
            joinColumns = @JoinColumn(name = "playground_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "sport_id", referencedColumnName = "id", nullable = false)
    )
    private Collection<SportEntity> specializations;

    @ManyToMany
    @JoinTable(
            schema = "lease",
            name = "capability",
            joinColumns = @JoinColumn(name = "playground_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "feature_id", referencedColumnName = "id", nullable = false)
    )
    private Collection<FeatureEntity> capabilities;

    @ManyToMany
    @JoinTable(
            schema = "lease",
            name = "ownership",
            joinColumns = @JoinColumn(name = "playground_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    )
    private Collection<UserEntity> owners;

    @ManyToMany
    @JoinTable(
            schema = "lease",
            name = "photo",
            joinColumns = @JoinColumn(name = "playground_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "picture_id", referencedColumnName = "id", nullable = false)
    )
    private Collection<PictureEntity> photos;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public Timestamp getOpening() {
        return opening;
    }

    public void setOpening(Timestamp opening) {
        this.opening = opening;
    }

    public Timestamp getClosing() {
        return closing;
    }

    public void setClosing(Timestamp closing) {
        this.closing = closing;
    }

    public Boolean getHalfHourAvailable() {
        return halfHourAvailable;
    }

    public void setHalfHourAvailable(Boolean halfHourAvailable) {
        this.halfHourAvailable = halfHourAvailable;
    }

    public Boolean getFullHourRequired() {
        return fullHourRequired;
    }

    public void setFullHourRequired(Boolean fullHourRequired) {
        this.fullHourRequired = fullHourRequired;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Collection<OrderEntity> getOrders() {
        return orders;
    }

    public void setOrders(Collection<OrderEntity> orders) {
        this.orders = orders;
    }

    public Collection<SportEntity> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(Collection<SportEntity> specializations) {
        this.specializations = specializations;
    }

    public Collection<FeatureEntity> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(Collection<FeatureEntity> capabilities) {
        this.capabilities = capabilities;
    }

    public Collection<UserEntity> getOwners() {
        return owners;
    }

    public void setOwners(Collection<UserEntity> owners) {
        this.owners = owners;
    }

    public Collection<PictureEntity> getPhotos() {
        return photos;
    }

    public void setPhotos(Collection<PictureEntity> photos) {
        this.photos = photos;
    }
}
