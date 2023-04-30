package com.ktu.xsports.api.service;

import com.ktu.xsports.api.domain.Category;
import com.ktu.xsports.api.domain.Progress;
import com.ktu.xsports.api.domain.Sport;
import com.ktu.xsports.api.domain.User;
import com.ktu.xsports.api.dto.response.statistics.CategoryStatisticResponse;
import com.ktu.xsports.api.dto.response.statistics.LearningTimeStampsResponse;
import com.ktu.xsports.api.dto.response.statistics.SportStatisticResponse;
import com.ktu.xsports.api.dto.response.statistics.StatisticsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ktu.xsports.api.service.StatusService.DONE;
import static com.ktu.xsports.api.service.StatusService.STARTED;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final UserService userService;
    private final ProgressService progressService;

    public StatisticsResponse getStatistics(long userId) {
        User user = userService.findById(userId);
        List<Sport> sports = user.getSports();
        List<Progress> progress = progressService.findProgressByUser(userId);


        List<SportStatisticResponse> sportsResponse = sports.stream()
            .map(sport -> getSportStatistics(sport, progress))
            .toList();

        return StatisticsResponse.builder()
            .learnedCount(countLearnedInSports(sportsResponse))
            .learningCount(countLearningInSports(sportsResponse))
            .sports(sportsResponse)
            .build();
    }

    private int countLearnedInSports(List<SportStatisticResponse> sports) {
        return sports.stream()
            .mapToInt(SportStatisticResponse::getLearnedCount)
            .sum();
    }

    private int countLearningInSports(List<SportStatisticResponse> sports) {
        return sports.stream()
            .mapToInt(SportStatisticResponse::getLearnedCount)
            .sum();
    }

    private SportStatisticResponse getSportStatistics(Sport sport, List<Progress> progress) {
        List<CategoryStatisticResponse> categoryStats = sport.getCategories().stream()
            .map(category ->
                getCategoryStatistics(category, progress)
            ).toList();

        List<Progress> filteredProgress = filterSportProgress(sport, progress);

        int learned = countSportsTricksLearned(categoryStats);
        int learning = countSportsTricksLearning(categoryStats);

        return SportStatisticResponse.builder()
            .name(sport.getName())
            .sportCategories(categoryStats)
            .learnedCount(learned)
            .learningCount(learning)
            .tricksCount(learned + learning)
            .timeStamps(getTimeStamps(filteredProgress))
            .build();
    }

    private int countSportsTricksLearned(List<CategoryStatisticResponse> categoryStats) {
        return categoryStats.stream()
            .mapToInt(CategoryStatisticResponse::getLearnedCount)
            .sum();
    }

    private int countSportsTricksLearning(List<CategoryStatisticResponse> categoryStats) {
        return categoryStats.stream()
            .mapToInt(CategoryStatisticResponse::getLearningCount)
            .sum();
    }

    private List<Progress> filterSportProgress(Sport sport, List<Progress> progress) {
        return progress.stream()
            .filter(p -> p.getTrickVariant()
                .getTrick()
                .getCategory()
                .getSport()
                .getName()
                .equals(sport.getName())
            ).toList();
    }

    private CategoryStatisticResponse getCategoryStatistics(Category category, List<Progress> progress) {
        List<Progress> filteredProgress = progress.stream()
            .filter(p -> p.getTrickVariant()
                .getTrick()
                .getCategory()
                .getName()
                .equals(category.getName())
            ).toList();

        return CategoryStatisticResponse.builder()
            .name(category.getName())
            .learnedCount(countStatuses(filteredProgress, DONE))
            .learningCount(countStatuses(filteredProgress, STARTED))
            .tricksCount(filteredProgress.size())
            .timeStamps(getTimeStamps(filteredProgress))
            .build();
    }

    private int countStatuses(List<Progress> progress, String status) {
        return (int) progress.stream()
            .filter(p -> p.getStatus().getName().equals(status))
            .count();
    }

    private List<LearningTimeStampsResponse> getTimeStamps(List<Progress> progress) {
        Map<LocalDate, Long> countByDate = progress.stream()
            .collect(Collectors.groupingBy(Progress::getDateLearned, Collectors.counting()));

        return countByDate.entrySet().stream()
            .map(entity -> new LearningTimeStampsResponse(entity.getKey(), entity.getValue().intValue()))
            .toList();
    }
}
