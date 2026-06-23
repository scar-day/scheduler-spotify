package dev.scarday.telegramspotify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TelegramSpotifyApplication {

    public static void main(String[] args) {
        SpringApplication.run(TelegramSpotifyApplication.class, args);
    }

}
