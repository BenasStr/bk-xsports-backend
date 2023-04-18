package com.ktu.xsports.api.cron;

import com.ktu.xsports.api.domain.Publish;
import com.ktu.xsports.api.service.PublishService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PublishCron {

    private final PublishService publishService;

    @Scheduled(cron = "0 0 1 * * ?")
    public void publish() {
        List<Publish> publishes = publishService.getPublishes();

        publishes.forEach(publish ->{
            if (LocalDate.now().isEqual(publish.getReleaseDate())
                || LocalDate.now().isAfter(publish.getReleaseDate())) {
                publishService.publish(publish.getId());
                publishService.deletePublish(publish.getId());
            }
        });
    }
}