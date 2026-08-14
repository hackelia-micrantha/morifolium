# Getting started

Morifolium v0.1 begins with one deliberately small reference profile: a native Android application using Android Gradle Plugin built-in Kotlin support.

## Prerequisites

- `mise`
- an Android SDK installation exposed through `ANDROID_HOME` or `ANDROID_SDK_ROOT`
- Android SDK command-line tools (`sdkmanager`)

Java and Gradle versions are owned by `mise.toml`; do not install repository-specific versions manually unless debugging the tool bootstrap itself.

## Canonical workflow

```bash
mise install
mise run setup
mise run ci
```

The CI gate composes the same repository tasks used locally:

```text
setup
  -> lint
  -> unit tests
  -> debug APK build
```

`lint`, `test`, and `build` share the same `setup` dependency and may execute concurrently after setup completes.

## Individual tasks

```bash
mise run lint
mise run test
mise run build
```

The debug application artifact is produced at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Android SDK contract

The setup task provisions the SDK packages required by this profile into the configured Android SDK installation:

- platform tools
- Android API 37 platform
- Android build tools 36.0.0

The Android SDK installation itself remains an external workstation prerequisite in v0.1. A later distribution/governance slice can decide whether Morifolium should manage command-line tools through `mise` as well.

## Scope

This profile exists to prove Morifolium's project-level delivery contract. It is not intended to establish a new application architecture. Additional KMP, React Native, or native iOS profiles should be added only when they prove a distinct integration need while retaining the same canonical SDLC contract.
