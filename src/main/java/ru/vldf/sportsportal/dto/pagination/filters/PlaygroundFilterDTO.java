package ru.vldf.sportsportal.dto.pagination.filters;

import ru.vldf.sportsportal.dto.pagination.filters.generic.StringSearcherDTO;

import java.time.LocalTime;
import java.util.Collection;

public class PlaygroundFilterDTO extends StringSearcherDTO {

    private Collection<String> featureCodes;
    private Collection<String> sportCodes;
    private LocalTime opening;
    private LocalTime closing;
    private Integer startCost;
    private Integer endCost;
    private Integer minRate;


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

    public Integer getStartCost() {
        return startCost;
    }

    public PlaygroundFilterDTO setStartCost(Integer startCost) {
        this.startCost = startCost;
        return this;
    }

    public Integer getEndCost() {
        return endCost;
    }

    public PlaygroundFilterDTO setEndCost(Integer endCost) {
        this.endCost = endCost;
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
