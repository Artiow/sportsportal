package ru.vldf.sportsportal.mapper.manual.url.lease;

import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.vldf.sportsportal.domain.sectional.lease.OrderEntity;
import ru.vldf.sportsportal.mapper.manual.url.AbstractURLMapper;

import java.net.URI;
import java.util.Collection;

@Component
@Named("toOrderURL")
public class OrderURLMapper extends AbstractURLMapper<OrderEntity> {

    @Value("${api.path.lease.order}")
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
    public URI toURL(OrderEntity entity) {
        return super.toURL(entity);
    }

    @Named("fromCollection")
    public Collection<URI> toURL(Collection<OrderEntity> entityCollection) {
        return super.toURL(entityCollection);
    }
}
