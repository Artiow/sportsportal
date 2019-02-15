package ru.vldf.sportsportal.mapper.manual.url.common;

import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.vldf.sportsportal.domain.sectional.common.UserEntity;
import ru.vldf.sportsportal.mapper.manual.url.AbstractURLMapper;

import java.net.URI;
import java.util.Collection;

/**
 * @author Namednev Artem
 */
@Component
@Named("toUserURL")
public class UserURLMapper extends AbstractURLMapper<UserEntity> {

    public UserURLMapper(@Value("${api.path.common.user}") String apiPath) {
        super(apiPath);
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
