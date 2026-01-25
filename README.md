# IMS Configuration Tool

Android application to enable IMS features (VoLTE, VoWiFi, VoNR) by modifying carrier configuration.

> **Note:** This project is based on the work of [vvb2060/Ims](https://github.com/vvb2060/Ims).

## Features

- Enable VoLTE (Voice over LTE)
- Enable VoWiFi (Voice over WiFi)
- Enable VoNR (Voice over 5G)
- Enable 5G NR support

## Requirements

- Android 13+ (API 33+)
- Shizuku framework installed and authorized
- Root access or ADB authorization for Shizuku

## Build

### Using GitHub Actions

1. Go to the "Actions" tab in this repository
2. Select "Build APK" workflow
3. Click "Run workflow" to manually trigger the build
4. Download the APK from the Artifacts section after build completes

### Local Build

```bash
./gradlew assembleRelease
```

The APK will be located at: `app/build/outputs/apk/release/app-release.apk`

## Installation

1. Install Shizuku framework
2. Authorize Shizuku (via root or ADB)
3. Install this APK
4. Grant Shizuku permission when prompted

## Disclaimer

This tool modifies system carrier configuration. Use at your own risk.
