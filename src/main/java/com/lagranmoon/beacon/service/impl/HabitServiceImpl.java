package com.lagranmoon.beacon.service.impl;

import com.lagranmoon.beacon.exception.ResourceNotFoundException;
import com.lagranmoon.beacon.exception.UnAuthenticationException;
import com.lagranmoon.beacon.mapper.HabitDailyMapper;
import com.lagranmoon.beacon.mapper.HabitMapper;
import com.lagranmoon.beacon.mapper.HabitTagMapper;
import com.lagranmoon.beacon.model.HabitDetailDto;
import com.lagranmoon.beacon.model.HabitDto;
import com.lagranmoon.beacon.model.HabitRequest;
import com.lagranmoon.beacon.model.domain.Habit;
import com.lagranmoon.beacon.service.HabitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Lagranmoon
 */
@Slf4j
@Service
public class HabitServiceImpl implements HabitService {

    @Resource
    private HabitMapper habitMapper;

    @Resource
    private HabitDailyMapper habitDailyMapper;

    @Resource
    private HabitTagMapper habitTagMapper;

    @Override
    public List<HabitDto> getHabitListByOpenId(String openId) {

        List<Habit> habitList = habitMapper.getHabitsByOpenId(openId);

        return habitList.stream()
                .map(habit -> {
                    HabitDto habitDto = HabitDto.builder()
                            .id(habit.getId())
                            .title(habit.getTitle())
                            .build();

                    List<String> tagList = habitTagMapper.getTagNamesByHabitId(habit.getId());
                    habitDto.setTagList(tagList);

                    Integer count = habitDailyMapper.getCountById(habit.getId());

                    if (Objects.isNull(count)) {
                        habitDto.setCount(0);
                    } else {
                        habitDto.setCount(count);
                    }

                    return habitDto;
                }).collect(Collectors.toList());
    }

    @Override
    public HabitDetailDto getHabitDetailById(Long id, String openId) {

        Habit habit = habitMapper.getHabitById(id, openId);
        List<String> tagList = habitTagMapper.getTagNamesByHabitId(id);
        Integer count = habitDailyMapper.getCountById(id);

        if (Objects.nonNull(habit)) {

            if (Objects.isNull(count)) {
                count = 0;
            }

            return HabitDetailDto.builder()
                    .title(habit.getTitle())
                    .content(habit.getContent())
                    .frequency(habit.getFrequency())
                    .duration(habit.getDuration())
                    .createTime(habit.getCreateTime())
                    .tagList(tagList)
                    .count(count)
                    .build();
        } else {
            throw new ResourceNotFoundException(String.format("Habit %d doesn't exists", id));
        }
    }

    @Override
    @Transactional
    public void saveHabit(HabitRequest habitRequest, String openId) throws RuntimeException {

        String content = habitRequest.getContent();
        if (Objects.isNull(content)) {
            content = "";
        }

        Habit habit = new Habit();
        habit.setTitle(habitRequest.getTitle());
        habit.setContent(content);
        habit.setFrequency(habitRequest.getFrequency());
        habit.setDuration(habitRequest.getDuration());
        habit.setOpenId(openId);
        habit.setCreateTime(new Date());

        habitMapper.insertHabit(habit);

        if (Objects.nonNull(habitRequest.getTagList())) {
            List<Long> tagIdList =
                    habitTagMapper.getTagIdsByName(habitRequest.getTagList());
            habitTagMapper.insertHabitTags(habit.getId(), tagIdList);
        }
    }

    @Override
    @Transactional
    public void deleteHabit(Long id, String openId) throws RuntimeException {


        String openIdSaved = habitDailyMapper.getOpenIdByHabitId(id);

        if (Objects.isNull(openIdSaved) || !openIdSaved.equals(openId)) {
            throw new UnAuthenticationException("Unable to delete");
        }

        habitMapper.deleteHabitById(id, openId);
        habitTagMapper.deleteHabitTagsByHabitId(id);
        habitDailyMapper.deleteHabitDailyByHabitId(id);
    }
}
