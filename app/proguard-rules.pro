# Keep app-specific classes
-keep class app.hypostats.data.local.** { *; }
-keep class app.hypostats.domain.model.** { *; }

# Keep DI framework classes
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }

# Keep Jetpack Compose and dependencies
-keep class androidx.compose.** { *; }
-keep class androidx.lifecycle.** { *; }
-keep class androidx.navigation.** { *; }

# Keep line numbers for crash reports
-keepattributes SourceFile,LineNumberTable
