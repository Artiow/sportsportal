package ru.vldf.sportsportal.repository.tournament;

import ru.vldf.sportsportal.domain.sectional.tournament.PlayerEntity;
import ru.vldf.sportsportal.repository.AbstractIdentifiedRepository;

import java.sql.Timestamp;

/**
 * @author Namednev Artem
 */
public interface PlayerRepository extends AbstractIdentifiedRepository<PlayerEntity> {

    boolean existsByNameAndSurnameAndPatronymicAndBirthdateAndIdIsNot(String name, String surname, String patronymic, Timestamp birthdate, Integer id);

    boolean existsByNameAndSurnameAndPatronymicAndBirthdate(String name, String surname, String patronymic, Timestamp birthdate);
}
