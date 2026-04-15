#!/bin/zsh

# Source this file from ~/.zshrc to expose the local Android + KMP toolchain.

export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
export ANDROID_SDK_ROOT="$HOME/Library/Android/sdk"
export ANDROID_HOME="$ANDROID_SDK_ROOT"

path_entries=(
  "$JAVA_HOME/bin"
  "$ANDROID_SDK_ROOT/platform-tools"
  "$ANDROID_SDK_ROOT/emulator"
)

if [[ -d "$ANDROID_SDK_ROOT/cmdline-tools/latest/bin" ]]; then
  path_entries+=("$ANDROID_SDK_ROOT/cmdline-tools/latest/bin")
fi

for entry in "${path_entries[@]}"; do
  if [[ -d "$entry" && ":$PATH:" != *":$entry:"* ]]; then
    export PATH="$entry:$PATH"
  fi
done
