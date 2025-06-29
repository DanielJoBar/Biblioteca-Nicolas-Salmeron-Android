buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.2")
    }
}

plugins {
    id("com.android.application")           version "8.1.4" apply false
    id("org.jetbrains.kotlin.android")      version "1.9.25" apply false
    id("org.jetbrains.kotlin.kapt")         version "1.9.25" apply false
    id("com.google.gms.google-services")    version "4.4.2" apply false
    id("com.google.dagger.hilt.android")    version "2.48.1" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.7.1" apply false
    id("org.jetbrains.dokka")               version "2.0.0"

}

