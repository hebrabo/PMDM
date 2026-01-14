plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // REQUISITO CODELAB: Plugin para convertir JSON en objetos Kotlin
    alias(libs.plugins.kotlin.serialization)
    // NOTA: Se ha eliminado el alias de kotlin.compose porque no es compatible con Kotlin 1.9.0
}

android {
    namespace = "com.example.pmdm01_17_miniproyectoconectarseinternet"
    compileSdk = 34 // Versión estable estándar

    defaultConfig {
        applicationId = "com.example.pmdm01_17_miniproyectoconectarseinternet"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }

    // CONFIGURACIÓN PARA KOTLIN 1.9.0:
    // Define la versión del compilador de Compose necesaria para esta versión de Kotlin
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
}

dependencies {
    // Librerías base de Android y UI
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation("androidx.compose.material:material-icons-extended")
    implementation(libs.androidx.compose.material3)

    // 1. Capa de Red y Análisis de Datos (Siguiendo el Codelab)
    // Retrofit permite realizar solicitudes a servicios web REST
    implementation(libs.retrofit.main)

    // Componentes de serialización para convertir el array JSON en objetos Kotlin
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit2.kotlinx.serialization.converter)

    // 2. Coil para visualización de imágenes de forma asíncrona
    implementation(libs.coil.compose)

    // 3. ViewModel y Corrutinas (Siguiendo pautas de la profesora)
    // Permite usar viewModelScope para lanzar corrutinas sin bloquear el hilo principal
    implementation(libs.lifecycle.viewmodel.compose)

    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}