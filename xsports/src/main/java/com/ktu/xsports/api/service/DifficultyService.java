package com.ktu.xsports.api.service;

import com.ktu.xsports.api.domain.Difficulty;
import com.ktu.xsports.api.repository.DifficultyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DifficultyService {

    private final DifficultyRepository difficultyRepository;

    public List<Difficulty> findDifficulties(){
        return difficultyRepository.findAll();
    }
}
