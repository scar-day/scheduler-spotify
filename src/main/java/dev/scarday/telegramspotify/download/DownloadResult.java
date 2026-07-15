package dev.scarday.telegramspotify.download;

public record DownloadResult(boolean ok, byte[] thumbnail, byte[] audio) {}
