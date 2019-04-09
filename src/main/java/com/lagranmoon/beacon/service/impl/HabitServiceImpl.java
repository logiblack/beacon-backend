package com.lagranmoon.beacon.service.impl;

import com.lagranmoon.beacon.exception.ResourceNotFoundException;
import com.lagranmoon.beacon.mapper.HabitMapper;
import com.lagranmoon.beacon.model.HabitDetailDto;
import com.lagranmoon.beacon.model.HabitDto;
import com.lagranmoon.beacon.model.HabitRequest;
import com.lagranmoon.beacon.model.domain.Habit;
import com.lagranmoon.beacon.service.HabitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    @Override
    public List<HabitDto> getHabitListByOpenId(String openId) {

        List<Habit> habitList = habitMapper.getHabitsByOpenId(openId);

        log.info(String.valueOf(habitList.size()));

        return habitList.stream()
                .map(habit -> {
                    HabitDto habitDto = HabitDto.builder()
                            .id(habit.getId())
                            .title(habit.getTitle())
                            .build();
                    List<String> tagList = habitMapper.getHabitTagsById(habit.getId());
                    habitDto.setTagList(tagList);

                    Integer count = habitMapper.getCountById(habit.getId());
                    if (Objects.isNull(count)){
                        habitDto.setCount(0);
                    }else {
                        habitDto.setCount(count);
                    }

                    return habitDto;
                }).collect(Collectors.toList());
    }

    @Override
    public HabitDetailDto getHabitDetailById(Long id) {

        Habit habit = habitMapper.getHabitById(id);

        if (Objects.nonNull(habit)) {
            return HabitDetailDto.builder()
                    .content(habit.getContent())
                    .frequency(habit.getFrequency())
                    .duration(habit.getDuration())
                    .createTime(habit.getCreateTime())
                    .build();
        } else {
            throw new ResourceNotFoundException(String.format("Habit %d doesn't exists", id));
        }
    }

    @Override
    public void saveHabit(HabitRequest habitRequest, String openId) {
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
                    habitMapper.getTagIdsByName(habitRequest.getTagList());
            habitMapper.insertHabitTags(habit.getId(), tagIdList);
        }
    }
}
