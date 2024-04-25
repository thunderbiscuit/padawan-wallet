test:
  ./gradlew test

buildkmplib:
  ./gradlew :padawankmp:assemblePadawanKmpXCFramework
  cp ./PadawanWalletiOS/Package.swift ./padawankmp/build/XCFrameworks/release/
