package ru.vldf.sportsportal.mapper.manual.url.common;

import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.vldf.sportsportal.domain.sectional.common.PictureEntity;
import ru.vldf.sportsportal.mapper.manual.url.AbstractURLMapper;

import java.net.URI;
import java.util.Collection;

@Component
@Named("toPictureURL")
public class PictureURLMapper extends AbstractURLMapper<PictureEntity> {

    @Value("${api.path.common.picture}")
    private String apiPath;

    @Override
    protected String getApiPath() {
        return apiPath;
    }


    @Named("fromId")
    public URI toURL(Integer id) {
        return super.toURL(id);
    }

    @Named("fromEntity")
    public URI toURL(PictureEntity entity) {
        return super.toURL(entity);
    }

    @Named("fromCollection")
    public Collection<URI> toURL(Collection<PictureEntity> entityCollection) {
        return super.toURL(entityCollection);
    }
}
