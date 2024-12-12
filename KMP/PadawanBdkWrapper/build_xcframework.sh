#!/bin/bash

# Define variables
#PROJECT_NAME="WalletWrapper"
#SCHEME_NAME="WalletWrapper"
#OUTPUT_DIR="./iosApp/build/"
#FRAMEWORK_NAME="${PROJECT_NAME}.framework"
#XCFRAMEWORK_NAME="${PROJECT_NAME}.xcframework"

# Clean previous builds
rm -rf "./DerivedData-device/"
rm -rf "./DerivedData-simulator/"
rm -rf "./PadawanBdkWrapper.xcframework"

xcodebuild -scheme PadawanBdkWrapper \
    -sdk iphonesimulator \
    -configuration Debug \
    -derivedDataPath ./DerivedData-simulator/ \
    clean build

xcodebuild -scheme PadawanBdkWrapper \
	-sdk iphoneos \
	-configuration Debug \
	-derivedDataPath ./DerivedData-device/ \
	clean build

xcodebuild -create-xcframework \
    -framework ./DerivedData-device/Build/Products/Debug-iphoneos/PadawanBdkWrapper.framework \
    -framework ./DerivedData-simulator/Build/Products/Debug-iphonesimulator/PadawanBdkWrapper.framework \
    -output ./PadawanBdkWrapper.xcframework

echo "XCFramework created successfully"
