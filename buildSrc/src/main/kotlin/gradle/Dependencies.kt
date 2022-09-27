package gradle

object Dependencies {
    const val androidxCore = "androidx.core:core-ktx:${Versions.androidxCore}"
    const val androidxCompat = "androidx.appcompat:appcompat:${Versions.androidxCompat}"
    const val googleMaterial = "com.google.android.material:material:${Versions.googleMaterial}"
    const val androidxConstraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.androidxConstraintLayout}"
    const val firebaseAuth = "com.google.firebase:firebase-auth:${Versions.firebaseAuth}"
    const val firebaseStore = "com.google.firebase:firebase-storage:${Versions.firebaseStore}"

    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glideIntegration = "com.github.bumptech.glide:okhttp3-integration:${Versions.glideIntegration}"

    const val daggerHilt = "com.google.dagger:hilt-android:${Versions.daggerHilt}"
}