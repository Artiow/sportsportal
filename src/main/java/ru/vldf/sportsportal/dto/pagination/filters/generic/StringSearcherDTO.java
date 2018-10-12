package ru.vldf.sportsportal.dto.pagination.filters.generic;

public class StringSearcherDTO extends PageDividerDTO {

    private String searchString;


    @Override
    public StringSearcherDTO setPageSize(Integer pageSize) {
        return (StringSearcherDTO) super.setPageSize(pageSize);
    }

    @Override
    public StringSearcherDTO setPageNum(Integer pageNum) {
        return (StringSearcherDTO) super.setPageNum(pageNum);
    }

    public String getSearchString() {
        return searchString;
    }

    public StringSearcherDTO setSearchString(String searchString) {
        this.searchString = searchString;
        return this;
    }
}
