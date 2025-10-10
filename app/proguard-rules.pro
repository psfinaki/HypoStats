# Keep Room entities and DAOs
-keep class app.hypostats.data.local.** { *; }

# Keep domain models
-keep class app.hypostats.domain.model.** { *; }

# Keep Hilt generated classes
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.internal.GeneratedComponent { *; }

# Keep line numbers for crash reports
-keepattributes SourceFile,LineNumberTable
