package dev.scarday.telegramspotify.download;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class DownloadRequest {
    String query;

    public String query() {
        return query;
    }
}
