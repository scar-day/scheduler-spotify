package dev.scarday.telegramspotify.download.platform.impl;

import dev.scarday.telegramspotify.download.DownloadRequest;
import dev.scarday.telegramspotify.download.DownloadResult;
import dev.scarday.telegramspotify.download.platform.Platform;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
public class YtDlpPlatform implements Platform<DownloadRequest, DownloadResult> {
    @Override
    @SneakyThrows
    public DownloadResult execute(DownloadRequest request) {
        try (val workspace = YtDlpWorkspace.create()) {
            val command = YtDlpCommandBuilder.build(request, workspace.output());
            YtDlpProcess.run(command);

            return new DownloadResult(true, workspace.readThumbnail(), workspace.readAudio());
        }
    }
}
