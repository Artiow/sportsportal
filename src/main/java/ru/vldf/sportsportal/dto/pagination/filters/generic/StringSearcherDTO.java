package ru.vldf.sportsportal.dto.pagination.filters.generic;

public class StringSearcherDTO extends PageDividerDTO {

    private String searchString;


    public String getSearchString() {
        return searchString;
    }

    public StringSearcherDTO setSearchString(String searchString) {
        this.searchString = searchString;
        return this;
    }
}
