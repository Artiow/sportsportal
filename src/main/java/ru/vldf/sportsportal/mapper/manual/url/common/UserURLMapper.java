package ru.vldf.sportsportal.mapper.manual.url.common;

import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.mapper.manual.url.AbstractURLMapper;

import java.net.URI;
import java.util.Collection;

@Component
@Named("toUserURL")
public class UserURLMapper extends AbstractURLMapper<UserEntity> {

    @Value("${api-path.common.user}")
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
    public URI toURL(UserEntity entity) {
        return super.toURL(entity);
    }

    @Named("fromCollection")
    public Collection<URI> toURL(Collection<UserEntity> entityCollection) {
        return super.toURL(entityCollection);
    }
}
