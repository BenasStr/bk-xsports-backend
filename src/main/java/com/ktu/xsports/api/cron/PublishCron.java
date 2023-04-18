package com.ktu.xsports.api.cron;

import com.ktu.xsports.api.service.PublishService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PublishCron {

    private final PublishService publishService;

    @Scheduled(cron = "0 0 1 * * ?")
    public void publish() {

    }
}