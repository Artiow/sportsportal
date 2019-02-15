package ru.vldf.sportsportal.repository;

import org.springframework.data.repository.NoRepositoryBean;
import ru.vldf.sportsportal.domain.generic.AbstractWordbookEntity;

/**
 * @author Namednev Artem
 */
@NoRepositoryBean
public interface AbstractWordbookRepository<T extends AbstractWordbookEntity> extends AbstractIdentifiedRepository<T> {

    boolean existsByCode(String code);

    T findByCode(String code);
}
