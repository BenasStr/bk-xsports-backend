package com.ktu.xsports.api.dto.response.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LearningTimeStampsResponse {
    LocalDate dateLearned;
    int countLearned;
}
