package ru.vldf.sportsportal.repository;

import org.springframework.data.repository.NoRepositoryBean;
import ru.vldf.sportsportal.domain.generic.AbstractWordbookEntity;

import java.util.Collection;

@NoRepositoryBean
public interface AbstractWordbookRepository<T extends AbstractWordbookEntity> extends AbstractIdentifiedRepository<T> {

    Collection<T> findAllByCode(String code);

    T findByCode(String code);
}
