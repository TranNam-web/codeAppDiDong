pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven(url = "https://jitpack.io") // ✅ đây mới đúng cú pháp Kotlin
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io") // ✅ cũng thêm JitPack vào đây
        
    }
}

rootProject.name = "DoAnDiDong"
include(":app") // ✅ bỏ dấu . thừa đi
