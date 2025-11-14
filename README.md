
![CI](https://github.com/psfinaki/HypoStats/actions/workflows/ci.yml/badge.svg?branch=main)

# HypoStats

A simple Android app for tracking hypoglycemic episodes.

[<img src="https://f-droid.org/badge/get-it-on.png"
    alt="Get it on F-Droid"
    height="80">](https://f-droid.org/packages/app.hypostats)

## Features

- Record hypoglycemic treatments with sugar amounts
- View statistics and treatment history
- Backup and restore data
- Dark mode support
- Multi-language support

## Requirements

- Android 8.0 (API 26) or higher

## Tech Stack

- Kotlin
- Jetpack Compose + Material 3
- MVVM
- Hilt, Room, DataStore

## Building

```bash
./gradlew assembleDebug
./gradlew installDebug
```
