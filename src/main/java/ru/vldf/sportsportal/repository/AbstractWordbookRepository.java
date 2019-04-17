package ru.vldf.sportsportal.repository;

import org.springframework.data.repository.NoRepositoryBean;
import ru.vldf.sportsportal.domain.general.AbstractWordbookEntity;

import java.util.Optional;

/**
 * @author Namednev Artem
 */
@NoRepositoryBean
public interface AbstractWordbookRepository<T extends AbstractWordbookEntity> extends AbstractIdentifiedRepository<T> {

    boolean existsByCode(String code);

    Optional<T> findByCode(String code);
}
