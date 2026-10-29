[group("Root")]
[doc("List all available commands.")]
@default:
  just --list --unsorted

[group("Root")]
[doc("Open repository on GitHub.")]
repo:
  open https://github.com/thunderbiscuit/padawan-wallet

[group("Tests")]
[doc("Run all unit tests.")]
unittests:
  cd ./Android/ && ./kotlin test

[group("Release")]
[doc("Build the signed release bundle (.aab) to upload to the Google Play Console.")]
android-release:
  #!/usr/bin/env bash
  set -euo pipefail
  cd ./Android/
  if [ ! -f keystore.properties ]; then
    echo "Error: Android/keystore.properties is missing. It points to the upload keystore used to sign the bundle." >&2
    exit 1
  fi
  if [ ! -f assets/faucet.properties ]; then
    echo "Error: Android/assets/faucet.properties is missing, so the bundle would ship with the placeholder faucet configuration." >&2
    exit 1
  fi
  version_name=$(sed -nE 's/^ +versionName: *"?([^"]*)"?.*/\1/p' module.yaml)
  version_code=$(sed -nE 's/^ +versionCode: *([0-9]+).*/\1/p' module.yaml)
  if [[ "$version_name" == *SNAPSHOT* ]]; then
    echo "Warning: versionName is $version_name. Remember to bump versionName and versionCode in Android/module.yaml for a release." >&2
  fi
  ./kotlin package --variant release --format aab
  mkdir -p release
  output="release/padawan-wallet-${version_name}-${version_code}.aab"
  cp build/tasks/_Android_bundleAndroid/gradle-project-release.aab "$output"
  echo "Bundle ready for the Google Play Console: Android/$output (versionCode $version_code)"

[group("Localizations")]
[doc("Generate Android strings.")]
@android-locales:
  cd ./localization/scripts/                                                       \
  && python sync_localizations.py                                                  \
  && python build-android-strings.py                                               \
  && mv ../android/values/strings.xml    ../../Android/res/values/    \
  && mv ../android/values-es/strings.xml ../../Android/res/values-es/ \
  && mv ../android/values-pt/strings.xml ../../Android/res/values-pt/ \
  && mv ../android/values-fr/strings.xml ../../Android/res/values-fr/ \

[group("Localizations")]
[doc("Generate iOS strings.")]
@ios-locales:
  cd ./localization/scripts/                                                           \
  && python sync_localizations.py                                                      \
  && find ../../iOS/PadawanWallet/Resources/ -name '*.lproj' -type d -exec rm -rf {} + \
  && python build-ios-strings.py                                                       \
  && mv ../ios/* ../../iOS/PadawanWallet/Resources/

[group("ProjectSetup")]
[doc("Generate iOS files to start development")]
start-ios-dev:
  just ios-locales \
  && cd ./iOS/     \
  && swiftgen      \
  && xcodegen
