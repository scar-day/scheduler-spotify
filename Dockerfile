FROM eclipse-temurin:21-jre

RUN apt-get update && apt-get install -y \
    ffmpeg \
    wget \
    unzip \
    ca-certificates

# yt-dlp
RUN wget https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp \
    -O /usr/local/bin/yt-dlp \
 && chmod +x /usr/local/bin/yt-dlp

# deno (JS CHALLENGE)
RUN wget -qO- https://github.com/denoland/deno/releases/latest/download/deno-x86_64-unknown-linux-gnu.zip \
    -O deno.zip \
 && unzip deno.zip \
 && mv deno /usr/local/bin/ \
 && chmod +x /usr/local/bin/deno \
 && rm deno.zip

WORKDIR /app

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]