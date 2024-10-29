package com.example.demoSpringBoot.service;

import com.example.demoSpringBoot.entity.Score;
import com.example.demoSpringBoot.repository.ScoreRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScoreService {
    ScoreRepository scoreRepository;

    public Score createScore(Score score) {
        return scoreRepository.save(score);
    }

    public List<Score> getScores() {
        return scoreRepository.findAll();
    }
}
