package ru.vldf.sportsportal.domain.sectional.lease;

import ru.vldf.sportsportal.domain.generic.AbstractVersionedEntity;
import ru.vldf.sportsportal.domain.sectional.common.PictureEntity;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.mapper.manual.JavaTimeMapper;

import javax.persistence.*;
import java.math.BigDecimal;
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
    private Timestamp opening = Timestamp.valueOf(JavaTimeMapper.MIN);

    @Basic
    @Column(name = "closing", nullable = false)
    private Timestamp closing = Timestamp.valueOf(JavaTimeMapper.MIN);

    @Basic
    @Column(name = "half_hour_available", nullable = false)
    private Boolean halfHourAvailable = false;

    @Basic
    @Column(name = "full_hour_required", nullable = false)
    private Boolean fullHourRequired = false;

    @Basic
    @Column(name = "price", nullable = false)
    private BigDecimal price = BigDecimal.valueOf(0, 2);

    @OrderBy("pk.datetime")
    @OneToMany(mappedBy = "pk.playground")
    private Collection<ReservationEntity> reservations;

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Collection<ReservationEntity> getReservations() {
        return reservations;
    }

    public void setReservations(Collection<ReservationEntity> reservations) {
        this.reservations = reservations;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlaygroundEntity)) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
