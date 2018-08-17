package ru.vldf.sportsportal.dto.sectional.lease;

import ru.vldf.sportsportal.dto.generic.AbstractVersionedDTO;
import ru.vldf.sportsportal.dto.sectional.common.PictureDTO;
import ru.vldf.sportsportal.dto.validation.annotations.Phone;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.net.URI;
import java.util.List;

public class PlaygroundDTO extends AbstractVersionedDTO {

    @NotNull(groups = IdCheck.class)
    @Min(value = 1, groups = IdCheck.class)
    private Integer id;

    @NotNull(groups = VersionCheck.class)
    @Min(value = 1, groups = VersionCheck.class)
    private Long version;

    @NotNull(groups = FieldCheck.class)
    @Size(min = 1, max = 45, groups = FieldCheck.class)
    private String name;

    @NotNull(groups = FieldCheck.class)
    @Size(min = 1, max = 90, groups = FieldCheck.class)
    private String address;

    @NotNull(groups = FieldCheck.class)
    @Phone(groups = FieldCheck.class)
    private String phone;

    @NotNull(groups = FieldCheck.class)
    @Min(value = 1, groups = FieldCheck.class)
    @Max(value = 10, groups = FieldCheck.class)
    private Integer rate;

    @Null(groups = FieldCheck.class)
    private URI uri;

    @Valid
    @NotNull(groups = FieldCheck.class)
    private List<SportDTO> specializations;

    @Valid
    @NotNull(groups = FieldCheck.class)
    private List<FeatureDTO> capabilities;

    @Valid
    @NotNull(groups = FieldCheck.class)
    private List<PictureDTO> photos;


    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public PlaygroundDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    @Override
    public Long getVersion() {
        return version;
    }

    @Override
    public PlaygroundDTO setVersion(Long version) {
        this.version = version;
        return this;
    }

    public String getName() {
        return name;
    }

    public PlaygroundDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public PlaygroundDTO setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public PlaygroundDTO setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Integer getRate() {
        return rate;
    }

    public PlaygroundDTO setRate(Integer rate) {
        this.rate = rate;
        return this;
    }

    public URI getUri() {
        return uri;
    }

    public PlaygroundDTO setUri(URI uri) {
        this.uri = uri;
        return this;
    }

    public List<SportDTO> getSpecializations() {
        return specializations;
    }

    public PlaygroundDTO setSpecializations(List<SportDTO> specializations) {
        this.specializations = specializations;
        return this;
    }

    public List<FeatureDTO> getCapabilities() {
        return capabilities;
    }

    public PlaygroundDTO setCapabilities(List<FeatureDTO> capabilities) {
        this.capabilities = capabilities;
        return this;
    }

    public List<PictureDTO> getPhotos() {
        return photos;
    }

    public PlaygroundDTO setPhotos(List<PictureDTO> photos) {
        this.photos = photos;
        return this;
    }


    public interface IdCheck extends VersionCheck {

    }

    public interface CreateCheck extends FieldCheck {

    }

    public interface UpdateCheck extends VersionCheck, FieldCheck {

    }

    private interface VersionCheck {

    }

    private interface FieldCheck extends
            SportDTO.CodeCheck, FeatureDTO.CodeCheck, PictureDTO.IdCheck {

    }
}
