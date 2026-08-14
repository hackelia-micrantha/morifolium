#!/usr/bin/env bash
set -euo pipefail

sdk_root="${ANDROID_HOME:-${ANDROID_SDK_ROOT:-}}"
if [[ -z "$sdk_root" ]]; then
  echo "ANDROID_HOME or ANDROID_SDK_ROOT must point to an Android SDK installation." >&2
  exit 1
fi

sdkmanager_bin="$(command -v sdkmanager || true)"
if [[ -z "$sdkmanager_bin" ]]; then
  for candidate in \
    "$sdk_root/cmdline-tools/latest/bin/sdkmanager" \
    "$sdk_root/tools/bin/sdkmanager"; do
    if [[ -x "$candidate" ]]; then
      sdkmanager_bin="$candidate"
      break
    fi
  done
fi

if [[ -z "$sdkmanager_bin" ]]; then
  echo "sdkmanager was not found under $sdk_root; install Android SDK command-line tools." >&2
  exit 1
fi

"$sdkmanager_bin" \
  "platform-tools" \
  "platforms;android-36" \
  "build-tools;36.0.0"

escaped_sdk_root="${sdk_root//\\/\\\\}"
printf 'sdk.dir=%s\n' "$escaped_sdk_root" > local.properties

echo "Android SDK ready: $sdk_root"
