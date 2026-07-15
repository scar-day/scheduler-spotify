# scheduler-spotify

Telegram-бот на Spring Boot, который каждые 15 секунд обновляет одно закреплённое сообщение статусом трека, играющего в Spotify, и умеет по кнопке скачивать трек или весь альбом в MP3 через `yt-dlp`.

## Возможности

- Раз в 15 секунд опрашивает Spotify Web API и редактирует одно и то же Telegram-сообщение: что сейчас играет, на паузе или тишина (с анимацией ожидания).
- Под сообщением — инлайн-кнопки: скачать трек или альбом, ссылка на исходный код.
- Скачивание идёт через `yt-dlp` (поиск трека на YouTube и извлечение аудио в MP3 с обложкой).
- Альбом скачивается параллельно (пул потоков), треки отправляются в Telegram батчами (`sendMediaGroup`/`sendAudio`) с учётом лимитов Telegram на размер медиагруппы.
- Доступ к боту ограничен списком разрешённых Telegram user id.

## Структура проекта

```
src/main/java/dev/scarday/telegramspotify/
├── TelegramSpotifyApplication.java     — точка входа Spring Boot (@EnableScheduling)
├── configuration/                      — конфигурация Spring-бинов
│   ├── SpotifyConfiguration.java       — бин SpotifyApi (client-id/secret/refresh-token)
│   ├── TelegramConfiguration.java      — @ConfigurationProperties("telegram"), бин TelegramClient
│   └── ExecutorConfiguration.java      — пул потоков для параллельного скачивания альбомов
├── scheduler/
│   └── SpotifyScheduler.java           — @Scheduled задача, обновляющая статус-сообщение
├── service/
│   ├── SpotifyService.java             — обращения к Spotify Web API (текущий трек, треки альбома)
│   └── DownloadService.java            — фасад над download-платформой (трек / трек альбома)
├── download/                           — абстракция скачивания аудио
│   ├── DownloadRequest.java, impl/     — запрос на скачивание (трек / трек в составе альбома)
│   ├── DownloadResult.java             — результат (байты аудио + обложки)
│   └── platform/impl/                  — реализация через yt-dlp: сборка команды, временная
│                                           рабочая директория, запуск процесса
├── model/
│   └── SpotifyTrack.java               — модель трека (played/paused, прогресс, обложка и т.д.)
├── callback/                           — состояние callback-кнопок
│   ├── Callbacks.java                  — константы/парсинг action:data строк callback-запросов
│   ├── CallbackCache.java              — кэш "id трека → SpotifyTrack" для обработки кнопок
│   └── AlbumDownloadTracker.java       — отслеживание уже запущенных загрузок альбомов
├── telegram/
│   ├── bot/TelegramBot.java            — long-polling consumer, проверка доступа по id
│   ├── callback/                       — CallbackHandler + CallbackDispatcher (диспетчеризация
│   │   └── impl/                          по action), обработчики: выбор/трек/альбом
│   ├── message/                        — построение Telegram-методов отправки
│   │   ├── impl/                          (sendMessage/editMessageText/sendAudio/sendMediaGroup)
│   │   ├── keyboard/                    — модель клавиатуры и фабрика кнопок
│   │   └── status/                      — TrackStatusFormatter: отдельная реализация текста
│   │       └── impl/                       статуса на каждое состояние трека (играет/пауза/тишина)
│   └── platform/TelegramPlatform.java  — выполнение Telegram-методов через TelegramClient
└── utility/                            — форматирование времени, прогресс-бар, имена файлов
```

## Требования

- JDK 21
- [`yt-dlp`](https://github.com/yt-dlp/yt-dlp) и `ffmpeg` в `PATH` (используются для поиска и конвертации аудио)
- Приложение Spotify (client id/secret) и refresh token с доступом к `user-read-currently-playing`
- Telegram-бот (токен от [@BotFather](https://t.me/BotFather)) и заранее отправленное в чат сообщение, которое бот будет редактировать

## Конфигурация

Приложение читает настройки из переменных окружения (см. `src/main/resources/application.yaml`):

| Переменная               | Назначение                                                        |
|---------------------------|--------------------------------------------------------------------|
| `SPOTIFY_CLIENT_ID`       | Client ID приложения Spotify                                       |
| `SPOTIFY_CLIENT_SECRET`   | Client Secret приложения Spotify                                   |
| `SPOTIFY_REFRESH_TOKEN`   | Refresh token пользователя, чей плеер отслеживается                |
| `TELEGRAM_BOT_TOKEN`      | Токен Telegram-бота                                                 |
| `TELEGRAM_IDS`            | Список Telegram user id через запятую, которым разрешено жать кнопки |
| `TELEGRAM_CHAT_ID`        | Chat id, в котором находится статус-сообщение                      |
| `TELEGRAM_MESSAGE_ID`     | Message id статус-сообщения, которое бот будет редактировать        |
| `YTDLP_LEVEL` (опц.)      | Уровень логирования пакета `dev.scarday` (по умолчанию `INFO`)     |

Также опционально можно положить `cookies.txt` (формат Netscape) в рабочую директорию — `yt-dlp` подхватит его для авторизованных запросов к YouTube.

## Сборка

```bash
./gradlew build
```

Соберётся исполняемый jar в `build/libs/`.

## Запуск

```bash
export SPOTIFY_CLIENT_ID=...
export SPOTIFY_CLIENT_SECRET=...
export SPOTIFY_REFRESH_TOKEN=...
export TELEGRAM_BOT_TOKEN=...
export TELEGRAM_IDS=123456789,987654321
export TELEGRAM_CHAT_ID=...
export TELEGRAM_MESSAGE_ID=...

./gradlew bootRun
```

Или через собранный jar:

```bash
java -jar build/libs/scheduler-spotify-0.0.1-SNAPSHOT.jar
```

Перед первым запуском отправьте в целевой чат любое сообщение и возьмите его `chat_id`/`message_id` — именно его бот будет обновлять статусом трека каждые 15 секунд.
