package com.lagranmoon.beacon.controller;

import com.lagranmoon.beacon.model.ResponseDto;
import com.lagranmoon.beacon.service.HabitDailyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Lagranmoon
 */
@RestController
public class HabitDailyController {

    @Resource
    private HabitDailyService habitDailyService;

    @PostMapping(value = "/habit/count/{id}")
    public ResponseEntity finishHabit(@PathVariable Long id,
                                      @RequestAttribute("openId") String openId) {
        habitDailyService.increaseHabitCnt(id, openId);
        return ResponseEntity.ok()
                .body(ResponseDto.builder()
                        .msg("success")
                        .build());
    }

}
