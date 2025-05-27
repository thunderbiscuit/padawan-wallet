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
