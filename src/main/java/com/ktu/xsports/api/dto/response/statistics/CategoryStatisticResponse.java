package com.ktu.xsports.api.dto.response.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryStatisticResponse {
    String name;
    int learnedCount;
    int learningCount;
    int tricksCount;

    List<LearningTimeStampsResponse> timeStamps;
}
