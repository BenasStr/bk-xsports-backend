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
public class SportStatisticResponse {
    String name;
    int tricksCount;
    int learnedCount;
    int learningCount;
    List<CategoryStatisticResponse> sportCategories;
    List<LearningTimeStampsResponse> timeStamps;
}
