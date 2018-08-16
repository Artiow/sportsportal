package ru.vldf.sportsportal.dto.sectional.lease.shortcut;

import java.net.URI;
import java.util.List;

public class PlaygroundShortDTO {

    private Integer id;
    private String name;
    private String address;
    private String phone;
    private Integer rate;
    private List<String> specializations;
    private List<String> capabilities;
    private URI playgroundURI;
    private List<URI> photoURIs;

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

    public URI getPlaygroundURI() {
        return playgroundURI;
    }

    public PlaygroundShortDTO setPlaygroundURI(URI playgroundURI) {
        this.playgroundURI = playgroundURI;
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

    public List<URI> getPhotoURIs() {
        return photoURIs;
    }

    public PlaygroundShortDTO setPhotoURIs(List<URI> photoURIs) {
        this.photoURIs = photoURIs;
        return this;
    }
}
