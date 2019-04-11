package com.lagranmoon.beacon.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @author Lagranmoon
 */
@Mapper
public interface HabitDailyMapper {

    String getOpenIdByHabitId(Long id);

    void increaseCount(Long habitId);

    Integer getCountById(Long habitId);

    Integer getCountByIdAndDate(@Param("habitId") Long habitId, @Param("date") Date date);

    void deleteHabitDailyByHabitId(Long habitId);
}
