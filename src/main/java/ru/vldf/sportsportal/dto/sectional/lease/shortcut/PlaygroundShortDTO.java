package ru.vldf.sportsportal.dto.sectional.lease.shortcut;

import ru.vldf.sportsportal.dto.generic.AbstractIdentifiedDTO;

import java.net.URI;
import java.time.LocalTime;
import java.util.List;

public class PlaygroundShortDTO extends AbstractIdentifiedDTO {

    private Integer id;
    private String name;
    private String address;
    private String phone;
    private Integer rate;
    private LocalTime opening;
    private LocalTime closing;
    private Boolean halfHourAvailable;
    private Boolean fullHourRequired;
    private Integer cost;
    private List<String> specializations;
    private List<String> capabilities;
    private URI playgroundURL;
    private List<URI> photoURLs;

    public Integer getId() {
        return id;
    }

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

    public LocalTime getOpening() {
        return opening;
    }

    public PlaygroundShortDTO setOpening(LocalTime opening) {
        this.opening = opening;
        return this;
    }

    public LocalTime getClosing() {
        return closing;
    }

    public PlaygroundShortDTO setClosing(LocalTime closing) {
        this.closing = closing;
        return this;
    }

    public Boolean getHalfHourAvailable() {
        return halfHourAvailable;
    }

    public PlaygroundShortDTO setHalfHourAvailable(Boolean halfHourAvailable) {
        this.halfHourAvailable = halfHourAvailable;
        return this;
    }

    public Boolean getFullHourRequired() {
        return fullHourRequired;
    }

    public PlaygroundShortDTO setFullHourRequired(Boolean fullHourRequired) {
        this.fullHourRequired = fullHourRequired;
        return this;
    }

    public Integer getCost() {
        return cost;
    }

    public PlaygroundShortDTO setCost(Integer cost) {
        this.cost = cost;
        return this;
    }

    public URI getPlaygroundURL() {
        return playgroundURL;
    }

    public PlaygroundShortDTO setPlaygroundURL(URI playgroundURL) {
        this.playgroundURL = playgroundURL;
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

    public List<URI> getPhotoURLs() {
        return photoURLs;
    }

    public PlaygroundShortDTO setPhotoURLs(List<URI> photoURLs) {
        this.photoURLs = photoURLs;
        return this;
    }
}
