package ru.vldf.sportsportal.repository;

import org.springframework.data.repository.NoRepositoryBean;
import ru.vldf.sportsportal.domain.general.AbstractIdentifiedEntity;

/**
 * @author Namednev Artem
 */
@NoRepositoryBean
public interface AbstractIdentifiedRepository<T extends AbstractIdentifiedEntity> extends JpaExecutableRepository<T, Integer> {

}
