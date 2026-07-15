package dev.scarday.telegramspotify.utility;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BarUtility {
    private static final int TOTAL_BARS = 10;

    public static String progressBar(long current, long total) {
        if (total <= 0) return "`──────────`";

        int filledBars = (int) ((double) current / total * TOTAL_BARS);

        StringBuilder sb = new StringBuilder("`");
        for (int i = 0; i < TOTAL_BARS; i++) {
            if (i == filledBars) {
                sb.append("🔘");
            } else if (i < filledBars) {
                sb.append("▬");
            } else {
                sb.append("─");
            }
        }
        sb.append("`");
        return sb.toString();
    }

    public static String sleepWave(long tick) {
        int period = 2 * (TOTAL_BARS - 1);
        int phase = (int) (tick % period);
        int position = phase < TOTAL_BARS ? phase : period - phase;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < TOTAL_BARS; i++) {
            sb.append(i == position ? "💤" : "‧");
        }
        return sb.toString();
    }
}
