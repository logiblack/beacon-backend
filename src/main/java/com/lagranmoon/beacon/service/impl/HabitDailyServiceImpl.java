package com.lagranmoon.beacon.service.impl;

import com.lagranmoon.beacon.exception.UnAuthenticationException;
import com.lagranmoon.beacon.mapper.HabitDailyMapper;
import com.lagranmoon.beacon.service.HabitDailyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author Lagranmoon
 */
@Service
public class HabitDailyServiceImpl implements HabitDailyService {

    @Resource
    private HabitDailyMapper habitDailyMapper;

    @Override
    @Transactional
    public void increaseHabitCnt(Long habitId,String openId) throws RuntimeException {

        String openIdSaved = habitDailyMapper.getOpenIdByHabitId(habitId);

        if (Objects.isNull(openIdSaved)||!openIdSaved.equals(openId)){
            throw new UnAuthenticationException("Unable to increase");
        }

        habitDailyMapper.increaseCount(habitId);
    }
}
