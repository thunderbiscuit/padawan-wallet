-keep class org.bitcoindevkit.** { *; }
-keep interface org.bitcoindevkit.** { *; }
-keep enum org.bitcoindevkit.** { *; }

# bdk-android calls its native library through JNA, which R8 cannot trace
-keep class com.sun.jna.** { *; }
-keep class * implements com.sun.jna.** { *; }
-dontwarn java.awt.**
