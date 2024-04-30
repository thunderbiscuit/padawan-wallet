unittests:
  ./gradlew test --console=plain

instrumentationtests:
  ./gradlew connectedAndroidTest --console=plain

buildlocalkmplib:
  ./gradlew :padawankmp:assemblePadawanKmpXCFramework
  cp ./PadawanWalletiOS/Package.swift ./padawankmp/build/XCFrameworks/release/

prepkmplibrelease:
  ./gradlew :padawankmp:assemblePadawanKmpXCFramework
  zip -r ./padawankmp/build/XCFrameworks/release/PadawanKmp.xcframework.zip ./padawankmp/build/XCFrameworks/release/PadawanKmp.xcframework
  swift package compute-checksum ./padawankmp/build/XCFrameworks/release/PadawanKmp.xcframework.zip
