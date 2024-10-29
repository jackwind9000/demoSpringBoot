package com.example.demoSpringBoot.controller;

import com.example.demoSpringBoot.entity.Score;
import com.example.demoSpringBoot.service.ScoreService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Make fields private final
@RequestMapping("/scores")
public class ScoreCotroller {
    ScoreService scoreService;

    @PostMapping
    public Score createScore(@RequestBody Score score) {
        return scoreService.createScore(score);
    }

    @GetMapping
    List<Score> getScores() {
        return scoreService.getScores();
    }
}
