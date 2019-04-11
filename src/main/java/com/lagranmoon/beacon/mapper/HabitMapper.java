package com.lagranmoon.beacon.mapper;

import com.lagranmoon.beacon.model.domain.Habit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Lagranmoon
 */
@Mapper
public interface HabitMapper {


    void insertHabit(Habit habit);

    /**
     * 根据用户的OpenID返回Habit
     * @param openId 用户的OpenID
     * @return 返回的Habit中包含id和title
     */
    List<Habit> getHabitsByOpenId(String openId);

    Habit getHabitById(@Param("id") Long id,@Param("openId") String openId);

    void deleteHabitById(@Param("id") Long id,@Param("openId") String openId);


}
