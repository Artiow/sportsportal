package ru.vldf.sportsportal.mapper.manual.uri.common;

import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.mapper.manual.uri.AbstractURIMapper;

import java.net.URI;
import java.util.Collection;

@Component
@Named("toUserURI")
public class UserURIMapper extends AbstractURIMapper<UserEntity> {

    @Value("${api-path.common.user}")
    private String apiPath;

    @Override
    protected String getApiPath() {
        return apiPath;
    }


    @Named("fromId")
    public URI toURI(Integer id) {
        return super.toURI(id);
    }

    @Named("fromEntity")
    public URI toURI(UserEntity entity) {
        return super.toURI(entity);
    }

    @Named("fromCollection")
    public Collection<URI> toURI(Collection<UserEntity> entityCollection) {
        return super.toURI(entityCollection);
    }
}
