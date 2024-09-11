unittests:
  cd ./Android/ && ./gradlew test --console=plain

instrumentationtests:
  cd ./Android/ && ./gradlew connectedAndroidTest --console=plain

buildlocalkmplib:
  cd ./Android/ && ./gradlew :padawankmp:assemblePadawanKmpXCFramework
  cp ./iOS/Package.swift ./Android/padawankmp/build/XCFrameworks/release/

prepkmplibrelease:
  cd ./Android/ && ./gradlew :padawankmp:assemblePadawanKmpXCFramework
  zip -r ./Android/padawankmp/build/XCFrameworks/release/PadawanKmp.xcframework.zip ./Android/padawankmp/build/XCFrameworks/release/PadawanKmp.xcframework
  swift package compute-checksum ./Android/padawankmp/build/XCFrameworks/release/PadawanKmp.xcframework.zip
