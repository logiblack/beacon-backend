package com.lagranmoon.beacon.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

import static org.junit.Assert.*;

/**
 * @author Lagranmoon
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class HabitDailyMapperTest {

    @Resource
    private HabitDailyMapper habitDailyMapper;

    @Test
    public void testIncreaseCount() {

    }

    @Test
    public void testGetCountById() {


    }
}