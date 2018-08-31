package ru.vldf.sportsportal.repository.lease;

import ru.vldf.sportsportal.domain.sectional.lease.PlaygroundEntity;
import ru.vldf.sportsportal.domain.sectional.lease.ReservationEntity;
import ru.vldf.sportsportal.domain.sectional.lease.ReservationEntityPK;
import ru.vldf.sportsportal.repository.JpaExecutableRepository;

import java.sql.Timestamp;

public interface ReservationRepository extends JpaExecutableRepository<ReservationEntity, ReservationEntityPK> {

    boolean existsByPkPlaygroundAndPkDatetime(PlaygroundEntity playground, Timestamp datetime);
}
