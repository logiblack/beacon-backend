package com.lagranmoon.beacon.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Lagranmoon
 */
@Mapper
public interface HabitTagMapper {

    /**
     * 插入习惯所有存在的标签
     * @param habitId Habit Id
     * @param tagList Tag名称列表（只有在数据库中存在的Tag才会保存）
     */
    void insertHabitTags(@Param("id") Long habitId
            ,@Param("tagList") List<Long> tagList);

    /**
     * 根据Habit ID获取其所有的Tag
     * @param habitId Habit Id
     * @return Tag名称列表
     */
    List<String> getTagNamesByHabitId(Long habitId);

    /**
     * 根据Tag的Name列表获取其Id李彪
     * @param tagList Tag名称列表
     * @return tagList Tag　Id列表
     */
    List<Long> getTagIdsByName(List<String> tagList);


    /**
     * 根据Habit Id删除其所有标签
     * @param habitId Habit Id
     */
    void deleteHabitTagsByHabitId(Long habitId);

}
