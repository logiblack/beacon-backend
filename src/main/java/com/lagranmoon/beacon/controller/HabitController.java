package com.lagranmoon.beacon.controller;

import com.lagranmoon.beacon.model.HabitDetailDto;
import com.lagranmoon.beacon.model.HabitDto;
import com.lagranmoon.beacon.model.HabitRequest;
import com.lagranmoon.beacon.model.ResponseDto;
import com.lagranmoon.beacon.model.domain.Habit;
import com.lagranmoon.beacon.service.HabitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Lagranmoon
 */
@RestController
public class HabitController {

    @Resource
    private HabitService habitService;

    @GetMapping("/habit")
    public ResponseEntity getHabitList(@RequestAttribute("openId") String openId) {

        List<HabitDto> habitList = habitService.getHabitListByOpenId(openId);

        return ResponseEntity.ok(
                ResponseDto.builder()
                        .data(habitList)
                        .build()
        );
    }

    @GetMapping("/habit/{id}")
    public ResponseEntity getHabitDetail(@PathVariable Long id,
                                         @RequestAttribute("openId") String openId) {

        HabitDetailDto habitDetail = habitService.getHabitDetailById(id, openId);

        return ResponseEntity.ok(
                ResponseDto.builder()
                        .data(habitDetail)
                        .build()
        );
    }

    @DeleteMapping("/habit/{id}")
    public ResponseEntity deleteHabitById(@PathVariable Long id
            , @RequestAttribute("openId") String openId) {
        habitService.deleteHabit(id, openId);
        return ResponseEntity.ok()
                .body(ResponseDto.builder().msg("success").build());
    }

    @PostMapping("/habit")
    public ResponseEntity saveHabit(@RequestBody @Valid HabitRequest habitRequest
            , @RequestAttribute("openId") String openId) {
        habitService.saveHabit(habitRequest, openId);
        return ResponseEntity.ok(
                ResponseDto.builder()
                        .msg("success")
                        .build()
        );
    }

    @PutMapping("/habit/{id}")
    public ResponseEntity modifyHabit(@PathVariable Long id,
                                      @Valid @RequestBody Habit habit,
                                      @RequestAttribute("openId") String openId){

        return ResponseEntity.ok(null);
    }
}
