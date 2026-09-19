# Retrofit
-keepattributes Signature
-keepattributes *Annotation*
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# Gson
-keep class com.smartcontrol.owner.data.model.** { *; }
-keepattributes Signature
-keepattributes *Annotation*

# Hilt
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }

# Socket.IO
-keep class io.socket.** { *; }

# Kotlin
-keep class kotlin.** { *; }
-dontwarn kotlin.**

# General
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn retrofit2.**