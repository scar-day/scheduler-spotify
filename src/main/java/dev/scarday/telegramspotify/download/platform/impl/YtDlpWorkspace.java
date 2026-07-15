package dev.scarday.telegramspotify.download.platform.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

@Slf4j
final class YtDlpWorkspace implements AutoCloseable {
    private final Path directory;
    private final Path output;

    private YtDlpWorkspace(Path directory) {
        this.directory = directory;
        this.output = directory.resolve("track");
    }

    @SneakyThrows
    static YtDlpWorkspace create() {
        return new YtDlpWorkspace(Files.createTempDirectory("yt-dlp"));
    }

    Path output() {
        return output;
    }

    byte[] readAudio() throws IOException {
        return Files.readAllBytes(directory.resolve("track.mp3"));
    }

    byte[] readThumbnail() throws IOException {
        val thumbnail = directory.resolve("track.jpg");
        return Files.exists(thumbnail) ? Files.readAllBytes(thumbnail) : null;
    }

    @Override
    @SneakyThrows
    public void close() {
        try (val walk = Files.walk(directory)) {
            walk.sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                            log.warn("Failed to delete {}", path, e);
                        }
                    });
        }
    }
}
