package com.lagranmoon.beacon.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Lagranmoon
 */
@Data
@Builder
public class HabitDetailDto {

    private Long id;
    private String title;
    private Integer count;
    private List<String> tagList;

    private String content;
    private Integer frequency;
    private Integer duration;
    private Date createTime;

}
