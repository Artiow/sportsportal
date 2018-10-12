package ru.vldf.sportsportal.dto.pagination.filters;

import ru.vldf.sportsportal.dto.pagination.filters.generic.StringSearcherDTO;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Collection;

public class PlaygroundFilterDTO extends StringSearcherDTO {

    private Collection<String> featureCodes;
    private Collection<String> sportCodes;
    private BigDecimal startPrice;
    private BigDecimal endPrice;
    private LocalTime opening;
    private LocalTime closing;
    private Integer minRate;


    @Override
    public PlaygroundFilterDTO setPageSize(Integer pageSize) {
        return (PlaygroundFilterDTO) super.setPageSize(pageSize);
    }

    @Override
    public PlaygroundFilterDTO setPageNum(Integer pageNum) {
        return (PlaygroundFilterDTO) super.setPageNum(pageNum);
    }

    @Override
    public PlaygroundFilterDTO setSearchString(String searchString) {
        return (PlaygroundFilterDTO) super.setSearchString(searchString);
    }

    public Collection<String> getFeatureCodes() {
        return featureCodes;
    }

    public PlaygroundFilterDTO setFeatureCodes(Collection<String> featureCodes) {
        this.featureCodes = featureCodes;
        return this;
    }

    public Collection<String> getSportCodes() {
        return sportCodes;
    }

    public PlaygroundFilterDTO setSportCodes(Collection<String> sportCodes) {
        this.sportCodes = sportCodes;
        return this;
    }

    public BigDecimal getStartPrice() {
        return startPrice;
    }

    public PlaygroundFilterDTO setStartPrice(BigDecimal startPrice) {
        this.startPrice = startPrice;
        return this;
    }

    public BigDecimal getEndPrice() {
        return endPrice;
    }

    public PlaygroundFilterDTO setEndPrice(BigDecimal endPrice) {
        this.endPrice = endPrice;
        return this;
    }

    public LocalTime getOpening() {
        return opening;
    }

    public PlaygroundFilterDTO setOpening(LocalTime opening) {
        this.opening = opening;
        return this;
    }

    public LocalTime getClosing() {
        return closing;
    }

    public PlaygroundFilterDTO setClosing(LocalTime closing) {
        this.closing = closing;
        return this;
    }

    public Integer getMinRate() {
        return minRate;
    }

    public PlaygroundFilterDTO setMinRate(Integer minRate) {
        this.minRate = minRate;
        return this;
    }
}
