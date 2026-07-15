package dev.scarday.telegramspotify.download.platform.impl;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@UtilityClass
@Slf4j
class YtDlpProcess {
    @SneakyThrows
    void run(List<String> command) {
        val process = new ProcessBuilder(command)
                .redirectErrorStream(true)
                .start();

        val logBuffer = new StringBuilder();
        try (val reader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                log.debug("[yt-dlp] {}", line);
                logBuffer.append(line).append('\n');
            }
        }

        val exit = process.waitFor();
        if (exit != 0) {
            throw new IllegalStateException("yt-dlp failed:\n" + logBuffer);
        }
    }
}
