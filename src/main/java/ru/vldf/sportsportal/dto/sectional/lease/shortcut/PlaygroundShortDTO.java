package ru.vldf.sportsportal.dto.sectional.lease.shortcut;

import ru.vldf.sportsportal.dto.generic.AbstractIdentifiedDTO;
import ru.vldf.sportsportal.dto.generic.specific.WorkTimeDTO;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalTime;
import java.util.List;

public class PlaygroundShortDTO extends AbstractIdentifiedDTO implements WorkTimeDTO {

    private Integer id;
    private String name;
    private String address;
    private String phone;
    private Integer rate;
    private LocalTime opening;
    private LocalTime closing;
    private Boolean halfHourAvailable;
    private Boolean fullHourRequired;
    private BigDecimal price;
    private List<String> specializations;
    private List<String> capabilities;
    private URI playgroundURL;
    private List<URI> ownersURLs;
    private List<URI> photoURLs;


    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public PlaygroundShortDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public PlaygroundShortDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public PlaygroundShortDTO setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public PlaygroundShortDTO setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Integer getRate() {
        return rate;
    }

    public PlaygroundShortDTO setRate(Integer rate) {
        this.rate = rate;
        return this;
    }

    @Override
    public LocalTime getOpening() {
        return opening;
    }

    @Override
    public PlaygroundShortDTO setOpening(LocalTime opening) {
        this.opening = opening;
        return this;
    }

    @Override
    public LocalTime getClosing() {
        return closing;
    }

    @Override
    public PlaygroundShortDTO setClosing(LocalTime closing) {
        this.closing = closing;
        return this;
    }

    @Override
    public Boolean getHalfHourAvailable() {
        return halfHourAvailable;
    }

    @Override
    public PlaygroundShortDTO setHalfHourAvailable(Boolean halfHourAvailable) {
        this.halfHourAvailable = halfHourAvailable;
        return this;
    }

    @Override
    public Boolean getFullHourRequired() {
        return fullHourRequired;
    }

    @Override
    public PlaygroundShortDTO setFullHourRequired(Boolean fullHourRequired) {
        this.fullHourRequired = fullHourRequired;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public PlaygroundShortDTO setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public List<String> getSpecializations() {
        return specializations;
    }

    public PlaygroundShortDTO setSpecializations(List<String> specializations) {
        this.specializations = specializations;
        return this;
    }

    public List<String> getCapabilities() {
        return capabilities;
    }

    public PlaygroundShortDTO setCapabilities(List<String> capabilities) {
        this.capabilities = capabilities;
        return this;
    }

    public URI getPlaygroundURL() {
        return playgroundURL;
    }

    public PlaygroundShortDTO setPlaygroundURL(URI playgroundURL) {
        this.playgroundURL = playgroundURL;
        return this;
    }

    public List<URI> getOwnersURLs() {
        return ownersURLs;
    }

    public PlaygroundShortDTO setOwnersURLs(List<URI> ownersURLs) {
        this.ownersURLs = ownersURLs;
        return this;
    }

    public List<URI> getPhotoURLs() {
        return photoURLs;
    }

    public PlaygroundShortDTO setPhotoURLs(List<URI> photoURLs) {
        this.photoURLs = photoURLs;
        return this;
    }
}
