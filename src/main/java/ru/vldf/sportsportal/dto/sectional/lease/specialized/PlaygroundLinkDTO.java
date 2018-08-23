package ru.vldf.sportsportal.dto.sectional.lease.specialized;

import ru.vldf.sportsportal.dto.generic.AbstractIdentifiedDTO;

import java.net.URI;
import java.util.List;

public class PlaygroundLinkDTO extends AbstractIdentifiedDTO {

    private Integer id;
    private String name;
    private String address;
    private String phone;
    private Integer rate;
    private URI playgroundURL;
    private List<URI> ownersURLs;
    private List<URI> photoURLs;


    public Integer getId() {
        return id;
    }

    public PlaygroundLinkDTO setId(Integer id) {
        this.id = id;
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
