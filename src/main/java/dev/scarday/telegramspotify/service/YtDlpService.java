package dev.scarday.telegramspotify.service;

import dev.scarday.telegramspotify.model.SpotifyTrack;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;

import org.springframework.stereotype.Service;

@Service
@Slf4j
public class YtDlpService {
    public Result downloadAudio(SpotifyTrack trackInfo) {
        return downloadAudio("%s - %s".formatted(trackInfo.artists(), trackInfo.getSongName()));
    }

    @SneakyThrows
    public Result downloadAudio(String query) {
        val tempDir = Files.createTempDirectory("yt-dlp");
        val output = tempDir.resolve("track");

        val command = new ArrayList<String>();

        command.add("yt-dlp");
        command.add("ytsearch1:" + query);

        val cookies = Path.of("cookies.txt");
        if (Files.exists(cookies)) {
            command.add("--cookies");
            command.add(cookies.toAbsolutePath().toString());
        }

        command.add("--extractor-args");
        command.add("youtube:player_client=web");

        command.add("--extract-audio");
        command.add("--audio-format");
        command.add("mp3");
        command.add("--audio-quality");
        command.add("0");

        command.add("--embed-metadata");
        command.add("--embed-thumbnail");
        command.add("--write-thumbnail");
        command.add("--convert-thumbnails");
        command.add("jpg");

        command.add("--ppa");
        command.add("EmbedThumbnail+ffmpeg_o:-c:v mjpeg -id3v2_version 3");

        command.add("--no-playlist");
        command.add("--verbose");

        command.add("-o");
        command.add(output + ".%(ext)s");

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

        val audio = Files.readAllBytes(tempDir.resolve("track.mp3"));

        byte[] thumbnail = null;
        val thumb = tempDir.resolve("track.jpg");
        if (Files.exists(thumb)) {
            thumbnail = Files.readAllBytes(thumb);
        }

        try (val walk = Files.walk(tempDir)) {
            walk.sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                            log.warn("Failed to delete {}", path, e);
                        }
                    });
        }

        return new Result(true, thumbnail, audio);
    }


    public record Result(boolean ok, byte[] thumbnail, byte[] audio) {}
}
