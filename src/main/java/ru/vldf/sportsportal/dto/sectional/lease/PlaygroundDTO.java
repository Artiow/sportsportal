package ru.vldf.sportsportal.dto.sectional.lease;

import ru.vldf.sportsportal.dto.generic.AbstractVersionedDTO;
import ru.vldf.sportsportal.dto.sectional.common.PictureDTO;
import ru.vldf.sportsportal.dto.sectional.common.specialized.UserLinkDTO;
import ru.vldf.sportsportal.dto.validation.annotations.Phone;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalTime;
import java.util.List;

public class PlaygroundDTO extends AbstractVersionedDTO {

    @NotNull(groups = IdCheck.class)
    @Min(value = 1, groups = IdCheck.class)
    private Integer id;

    @NotNull(groups = VersionCheck.class)
    @Min(value = 0, groups = VersionCheck.class)
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

    @NotNull(groups = FieldCheck.class)
    private LocalTime opening;

    @NotNull(groups = FieldCheck.class)
    private LocalTime closing;

    @NotNull(groups = FieldCheck.class)
    private Boolean halfHourAvailable;

    @NotNull(groups = FieldCheck.class)
    private Boolean fullHourRequired;

    @NotNull(groups = FieldCheck.class)
    @Min(value = 0, groups = FieldCheck.class)
    private Integer cost;

    @Valid
    @NotNull(groups = FieldCheck.class)
    private List<SportDTO> specializations;

    @Valid
    @NotNull(groups = FieldCheck.class)
    private List<FeatureDTO> capabilities;

    @Valid
    @NotNull(groups = FieldCheck.class)
    private List<PictureDTO> photos;

    @Valid
    @NotNull(groups = FieldCheck.class)
    private List<UserLinkDTO> owners;


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

    public LocalTime getOpening() {
        return opening;
    }

    public PlaygroundDTO setOpening(LocalTime opening) {
        this.opening = opening;
        return this;
    }

    public LocalTime getClosing() {
        return closing;
    }

    public PlaygroundDTO setClosing(LocalTime closing) {
        this.closing = closing;
        return this;
    }

    public Boolean getHalfHourAvailable() {
        return halfHourAvailable;
    }

    public PlaygroundDTO setHalfHourAvailable(Boolean halfHourAvailable) {
        this.halfHourAvailable = halfHourAvailable;
        return this;
    }

    public Boolean getFullHourRequired() {
        return fullHourRequired;
    }

    public PlaygroundDTO setFullHourRequired(Boolean fullHourRequired) {
        this.fullHourRequired = fullHourRequired;
        return this;
    }

    public Integer getCost() {
        return cost;
    }

    public PlaygroundDTO setCost(Integer cost) {
        this.cost = cost;
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

    public List<UserLinkDTO> getOwners() {
        return owners;
    }

    public PlaygroundDTO setOwners(List<UserLinkDTO> owners) {
        this.owners = owners;
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
            SportDTO.IdCheck, FeatureDTO.IdCheck, PictureDTO.IdCheck {

    }
}
