package ru.vldf.sportsportal.dto.generic.specific;

import ru.vldf.sportsportal.dto.generic.DataTransferObject;

import java.time.LocalTime;

public interface WorkTimeDTO<T extends WorkTimeDTO> extends DataTransferObject {

    LocalTime getOpening();

    T setOpening(LocalTime opening);

    LocalTime getClosing();

    T setClosing(LocalTime closing);
}
