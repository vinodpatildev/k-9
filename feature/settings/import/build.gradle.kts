plugins {
    id(ThunderbirdPlugins.Library.androidCompose)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    // Using "importing" because "import" is not allowed in Java package names (it's fine with Kotlin, though)
    namespace = "app.k9mail.feature.settings.importing"
    resourcePrefix = "settings_import_"

    buildTypes {
        debug {
            manifestPlaceholders["appAuthRedirectScheme"] = "FIXME: override this in your app project"
        }
        release {
            manifestPlaceholders["appAuthRedirectScheme"] = "FIXME: override this in your app project"
        }
    }
}

dependencies {
    implementation(projects.app.core)
    implementation(projects.app.ui.base)

    implementation(projects.feature.account.oauth)
    implementation(libs.appauth)

    implementation(libs.androidx.constraintlayout)
    implementation(libs.fastadapter)

    implementation(libs.timber)
}
