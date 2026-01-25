# IMS Configuration Tool
[![Build APK](https://github.com/ShamirSecret/pixel-16-ims-fix/actions/workflows/build.yml/badge.svg)](https://github.com/ShamirSecret/pixel-16-ims-fix/actions/workflows/build.yml)
Android application to enable IMS features (VoLTE, VoWiFi, VoNR) by modifying carrier configuration.

## Features

- Enable VoLTE (Voice over LTE)
- Enable VoWiFi (Voice over WiFi)
- Enable VoNR (Voice over 5G)
- Enable 5G NR support
- Enable cross-SIM IMS calling

## Requirements

- Android 13+ (API 33+)
- Shizuku framework installed and authorized
- Root access or ADB authorization for Shizuku

## Build

### Using GitHub Actions

1. Go to the "Actions" tab in this repository
2. Select "Build APK" workflow
3. Click "Run workflow" button
4. Select branch: `main`
5. Click "Run workflow" to start the build
6. Wait for the build to complete
7. Download the APK from the Artifacts section

### Local Build

```bash
./gradlew assembleRelease
```

The APK will be located at: `app/build/outputs/apk/release/app-release.apk`

## Installation

1. Install [Shizuku](https://github.com/RikkaApps/Shizuku) framework
2. Authorize Shizuku (via root or ADB)
3. Install this APK
4. Grant Shizuku permission when prompted
5. The app will automatically configure IMS settings

## How It Works

This app uses Shizuku to gain system-level permissions and modifies the carrier configuration (CarrierConfig) to enable IMS features that may be disabled by your carrier.

## Disclaimer

This tool modifies system carrier configuration. Use at your own risk. Some features may not work depending on your carrier's network infrastructure.

## Credits

Based on the original work by [vvb2060/Ims](https://github.com/vvb2060/Ims).
