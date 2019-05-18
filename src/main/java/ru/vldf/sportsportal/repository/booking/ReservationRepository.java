package ru.vldf.sportsportal.repository.booking;

import ru.vldf.sportsportal.domain.sectional.booking.PlaygroundEntity;
import ru.vldf.sportsportal.domain.sectional.booking.ReservationEntity;
import ru.vldf.sportsportal.domain.sectional.booking.ReservationEntityPK;
import ru.vldf.sportsportal.repository.JpaExecutableRepository;

import java.sql.Timestamp;

/**
 * @author Namednev Artem
 */
public interface ReservationRepository extends JpaExecutableRepository<ReservationEntity, ReservationEntityPK> {

    boolean existsByPkPlaygroundAndPkDatetime(PlaygroundEntity playground, Timestamp datetime);
}
