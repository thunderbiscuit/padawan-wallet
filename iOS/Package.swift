// swift-tools-version:5.5
import PackageDescription

let package = Package(
   name: "PadawanKmp",
   platforms: [
     .iOS(.v15),
   ],
   products: [
      .library(name: "PadawanKmp", targets: ["PadawanKmp"])
   ],
   targets: [
      .binaryTarget(
         name: "PadawanKmp",
         path: "./PadawanKmp.xcframework"
      )
   ]
)
