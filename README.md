# Jaame Sokhan (جام سخن)

An Android app for reading, listening to, and studying classical Persian poetry. It is written in Kotlin with Jetpack Compose.

## Features

- Browse poets, their categories, and their poems. Download poets for offline reading.
- Search verses and keep a search history.
- Listen to recitations from Ganjoor, with verse-by-verse sync and media notifications.
- Bookmark poems, highlight verses, and add comments, all organized with labels.
- Look up words in the built-in Persian dictionary.
- Show a random poem and send daily poem notifications.
- Keep a reading history.
- Customize the theme, fonts, and poem font size.

## Tech Stack

| Area | Library |
|------|---------|
| Language | Kotlin |
| UI | Jetpack Compose, Material 3 |
| Architecture | MVVM |
| DI | Hilt |
| Local storage | Room, Paging 3 |
| Networking | Retrofit, Gson |
| Images | Coil |
| Audio | Media3 ExoPlayer |
| Analytics & crash reports | Firebase Analytics, Crashlytics, Messaging |

## Project Structure

```
app/src/main/java/ir/jaamebaade/jaamebaade_client/
├── api/           # Retrofit services (Jaame Sokhan & Ganjoor APIs)
├── audio/         # Audio playback session & notifications
├── constants/     # Navigation routes
├── dao/           # Room DAOs
├── database/      # Room database
├── model/         # Entities and data models
├── notifications/ # Scheduled poem notifications
├── repository/    # Data repositories
├── ui/theme/      # Theme, colors, fonts, dimensions
├── view/          # Compose screens and components
├── viewmodel/     # ViewModels
└── AppModule.kt   # Hilt module
```

## Getting Started

### Prerequisites

- A recent version of Android Studio
- JDK 17+
- Android SDK 37 (the minimum supported Android version is SDK 27, Android 8.1)

### Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/jaamesokhan/jaamesokhan-client.git
   cd jaamesokhan-client
   ```
2. Add a `google-services.json` file to `app/`. The app uses Firebase, and this file is not tracked in git. You can create your own Firebase project to get one.
3. Build and install the app:
   ```bash
   ./gradlew assembleDebug
   ./gradlew installDebug
   ```

The server URLs are set in `app/src/main/res/values/strings.xml`: `SERVER_BASE_URL` and `GANJOOR_BASE_URL`.

## Data Sources

- **Poems:** the free and open-source [Ganjoor](https://ganjoor.net) database.
- **Audio recitations:** the [Ganjoor API](https://api.ganjoor.net).
- **Dictionary:** [foss4/farhang-backend](https://github.com/foss4/farhang-backend/blob/master/farhang.psql).

## Contributing

To contribute, open an issue or a pull request.

## License

This project is licensed under the [GNU General Public License v3.0](LICENSE).
