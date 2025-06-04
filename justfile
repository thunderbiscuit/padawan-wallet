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
  cd ./Android/ && ./gradlew test --console=plain

[group("Tests")]
[doc("Run all instrumentation tests.")]
instrumentationtests:
  cd ./Android/ && ./gradlew connectedAndroidTest --console=plain

[group("Localizations")]
[doc("Generate Android strings.")]
android-locales:
  cd ./localization/ \
  && python build-android-strings.py \
  && mv android-strings/values/strings.xml ../Android/app/src/main/res/values/ \
  && mv android-strings/values-es/strings.xml ../Android/app/src/main/res/values-es/ \
  && mv android-strings/values-pt/strings.xml ../Android/app/src/main/res/values-pt/
