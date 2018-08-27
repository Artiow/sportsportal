package ru.vldf.sportsportal.mapper;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.vldf.sportsportal.domain.sectional.lease.PlaygroundEntity;
import ru.vldf.sportsportal.dto.sectional.lease.specialized.PlaygroundGridDTO;
import ru.vldf.sportsportal.mapper.sectional.lease.PlaygroundMapper;

import java.sql.Timestamp;
import java.time.LocalTime;

@RunWith(SpringRunner.class)
@ContextConfiguration
public class PlaygroundMapperTests {

    private PlaygroundMapper playgroundMapper;

    @Autowired
    public void setPlaygroundMapper(PlaygroundMapper playgroundMapper) {
        this.playgroundMapper = playgroundMapper;
    }


    @Test
    public void prepareGrid_from0900to2100HHAFalse() {

        // arrange
        PlaygroundEntity playgroundEntity = new PlaygroundEntity();
        playgroundEntity.setOpening(Timestamp.valueOf("0001-01-01 09:00:00"));
        playgroundEntity.setClosing(Timestamp.valueOf("0001-01-01 21:00:00"));
        playgroundEntity.setHalfHourAvailable(false);

        // act
        PlaygroundGridDTO.ReservationGridDTO gridDTO = playgroundMapper.prepareGrid(playgroundEntity);

        // assert
        Assert.assertEquals(LocalTime.of(9, 0), gridDTO.getStartTime());
        Assert.assertEquals(LocalTime.of(20, 0), gridDTO.getEndTime());
        Assert.assertEquals(12, (int) gridDTO.getTotalTimes());
    }

    @Test
    public void prepareGrid_from0900to2100HHATrue() {

        // arrange
        PlaygroundEntity playgroundEntity = new PlaygroundEntity();
        playgroundEntity.setOpening(Timestamp.valueOf("0001-01-01 09:00:00"));
        playgroundEntity.setClosing(Timestamp.valueOf("0001-01-01 21:00:00"));
        playgroundEntity.setHalfHourAvailable(true);

        // act
        PlaygroundGridDTO.ReservationGridDTO gridDTO = playgroundMapper.prepareGrid(playgroundEntity);

        // assert
        Assert.assertEquals(LocalTime.of(9, 0), gridDTO.getStartTime());
        Assert.assertEquals(LocalTime.of(20, 30), gridDTO.getEndTime());
        Assert.assertEquals(24, (int) gridDTO.getTotalTimes());
    }

    @Test
    public void prepareGrid_from0915to2135HHAFalse() {

        // arrange
        PlaygroundEntity playgroundEntity = new PlaygroundEntity();
        playgroundEntity.setOpening(Timestamp.valueOf("0001-01-01 09:15:00"));
        playgroundEntity.setClosing(Timestamp.valueOf("0001-01-01 21:35:00"));
        playgroundEntity.setHalfHourAvailable(false);

        // act
        PlaygroundGridDTO.ReservationGridDTO gridDTO = playgroundMapper.prepareGrid(playgroundEntity);

        // assert
        Assert.assertEquals(LocalTime.of(9, 15), gridDTO.getStartTime());
        Assert.assertEquals(LocalTime.of(20, 15), gridDTO.getEndTime());
        Assert.assertEquals(12, (int) gridDTO.getTotalTimes());
    }

    @Test
    public void prepareGrid_from0915to2135HHATrue() {

        // arrange
        PlaygroundEntity playgroundEntity = new PlaygroundEntity();
        playgroundEntity.setOpening(Timestamp.valueOf("0001-01-01 09:15:00"));
        playgroundEntity.setClosing(Timestamp.valueOf("0001-01-01 21:35:00"));
        playgroundEntity.setHalfHourAvailable(true);

        // act
        PlaygroundGridDTO.ReservationGridDTO gridDTO = playgroundMapper.prepareGrid(playgroundEntity);

        // assert
        Assert.assertEquals(LocalTime.of(9, 15), gridDTO.getStartTime());
        Assert.assertEquals(LocalTime.of(20, 45), gridDTO.getEndTime());
        Assert.assertEquals(24, (int) gridDTO.getTotalTimes());
    }

    @Test
    public void prepareGrid_from2115to0935HHAFalse() {

        // arrange
        PlaygroundEntity playgroundEntity = new PlaygroundEntity();
        playgroundEntity.setOpening(Timestamp.valueOf("0001-01-01 21:15:00"));
        playgroundEntity.setClosing(Timestamp.valueOf("0001-01-01 09:35:00"));
        playgroundEntity.setHalfHourAvailable(false);

        // act
        PlaygroundGridDTO.ReservationGridDTO gridDTO = playgroundMapper.prepareGrid(playgroundEntity);

        // assert
        Assert.assertEquals(LocalTime.of(21, 15), gridDTO.getStartTime());
        Assert.assertEquals(LocalTime.of(8, 15), gridDTO.getEndTime());
        Assert.assertEquals(12, (int) gridDTO.getTotalTimes());
    }

    @Test
    public void prepareGrid_from2115to0935HHATrue() {

        // arrange
        PlaygroundEntity playgroundEntity = new PlaygroundEntity();
        playgroundEntity.setOpening(Timestamp.valueOf("0001-01-01 21:15:00"));
        playgroundEntity.setClosing(Timestamp.valueOf("0001-01-01 09:35:00"));
        playgroundEntity.setHalfHourAvailable(true);

        // act
        PlaygroundGridDTO.ReservationGridDTO gridDTO = playgroundMapper.prepareGrid(playgroundEntity);

        // assert
        Assert.assertEquals(LocalTime.of(21, 15), gridDTO.getStartTime());
        Assert.assertEquals(LocalTime.of(8, 45), gridDTO.getEndTime());
        Assert.assertEquals(24, (int) gridDTO.getTotalTimes());
    }

    @Test
    public void prepareGrid_from2135to0915HHAFalse() {

        // arrange
        PlaygroundEntity playgroundEntity = new PlaygroundEntity();
        playgroundEntity.setOpening(Timestamp.valueOf("0001-01-01 21:35:00"));
        playgroundEntity.setClosing(Timestamp.valueOf("0001-01-01 09:15:00"));
        playgroundEntity.setHalfHourAvailable(false);

        // act
        PlaygroundGridDTO.ReservationGridDTO gridDTO = playgroundMapper.prepareGrid(playgroundEntity);

        // assert
        Assert.assertEquals(LocalTime.of(21, 35), gridDTO.getStartTime());
        Assert.assertEquals(LocalTime.of(7, 35), gridDTO.getEndTime());
        Assert.assertEquals(11, (int) gridDTO.getTotalTimes());
    }

    @Test
    public void prepareGrid_from2135to0915HHATrue() {

        // arrange
        PlaygroundEntity playgroundEntity = new PlaygroundEntity();
        playgroundEntity.setOpening(Timestamp.valueOf("0001-01-01 21:35:00"));
        playgroundEntity.setClosing(Timestamp.valueOf("0001-01-01 09:15:00"));
        playgroundEntity.setHalfHourAvailable(true);

        // act
        PlaygroundGridDTO.ReservationGridDTO gridDTO = playgroundMapper.prepareGrid(playgroundEntity);

        // assert
        Assert.assertEquals(LocalTime.of(21, 35), gridDTO.getStartTime());
        Assert.assertEquals(LocalTime.of(8, 35), gridDTO.getEndTime());
        Assert.assertEquals(23, (int) gridDTO.getTotalTimes());
    }

    @Test
    public void setGrid() {

        // arrange

        // act

        // assert
    }


    @Configuration
    @ComponentScan("ru.vldf.sportsportal.mapper")
    public static class PlaygroundMapperConfig {

    }
}
