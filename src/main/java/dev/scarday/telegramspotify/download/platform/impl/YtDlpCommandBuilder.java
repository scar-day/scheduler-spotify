package dev.scarday.telegramspotify.download.platform.impl;

import dev.scarday.telegramspotify.download.DownloadRequest;
import lombok.experimental.UtilityClass;
import lombok.val;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
class YtDlpCommandBuilder {
    List<String> build(DownloadRequest request, Path output) {
        val command = new ArrayList<String>();

        command.add("yt-dlp");
        command.add("ytsearch1:" + request.query());

        val cookies = Path.of("cookies.txt");
        if (Files.exists(cookies)) {
            command.add("--cookies");
            command.add(cookies.toAbsolutePath().toString());
        }

        command.add("--extractor-args");
        command.add("youtube:player_client=android_vr,tv,web_safari,web");
        command.add("--format");
        command.add("bestaudio[ext=m4a]/bestaudio");
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

        return command;
    }
}
