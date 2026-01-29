extra["TMDB_API_KEY"] = project.findProperty("TMDB_API_KEY") ?: ""
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}
