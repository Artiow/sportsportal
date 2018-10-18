package ru.vldf.sportsportal.dto.sectional.lease.specialized;

import ru.vldf.sportsportal.dto.generic.AbstractVersionedDTO;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

public class PlaygroundLinkDTO extends AbstractVersionedDTO {

    @NotNull
    private Integer id;

    @NotNull
    private Long version;

    private String name;
    private String address;
    private String phone;
    private Integer rate;
    private Boolean halfHourAvailable;
    private Boolean fullHourRequired;
    private BigDecimal price;
    private URI playgroundURL;
    private List<URI> ownersURLs;
    private List<URI> photoURLs;


    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public PlaygroundLinkDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    @Override
    public Long getVersion() {
        return version;
    }

    @Override
    public PlaygroundLinkDTO setVersion(Long version) {
        this.version = version;
        return this;
    }

    public String getName() {
        return name;
    }

    public PlaygroundLinkDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public PlaygroundLinkDTO setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public PlaygroundLinkDTO setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Integer getRate() {
        return rate;
    }

    public PlaygroundLinkDTO setRate(Integer rate) {
        this.rate = rate;
        return this;
    }

    public Boolean getHalfHourAvailable() {
        return halfHourAvailable;
    }

    public PlaygroundLinkDTO setHalfHourAvailable(Boolean halfHourAvailable) {
        this.halfHourAvailable = halfHourAvailable;
        return this;
    }

    public Boolean getFullHourRequired() {
        return fullHourRequired;
    }

    public PlaygroundLinkDTO setFullHourRequired(Boolean fullHourRequired) {
        this.fullHourRequired = fullHourRequired;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public PlaygroundLinkDTO setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public URI getPlaygroundURL() {
        return playgroundURL;
    }

    public PlaygroundLinkDTO setPlaygroundURL(URI playgroundURL) {
        this.playgroundURL = playgroundURL;
        return this;
    }

    public List<URI> getOwnersURLs() {
        return ownersURLs;
    }

    public PlaygroundLinkDTO setOwnersURLs(List<URI> ownersURLs) {
        this.ownersURLs = ownersURLs;
        return this;
    }

    public List<URI> getPhotoURLs() {
        return photoURLs;
    }

    public PlaygroundLinkDTO setPhotoURLs(List<URI> photoURLs) {
        this.photoURLs = photoURLs;
        return this;
    }
}
