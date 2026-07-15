package dev.scarday.telegramspotify.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorConfiguration {
    private static final int ALBUM_DOWNLOAD_PARALLELISM = 4;

    @Bean
    public ExecutorService albumDownloadExecutor() {
        return Executors.newFixedThreadPool(ALBUM_DOWNLOAD_PARALLELISM);
    }
}
