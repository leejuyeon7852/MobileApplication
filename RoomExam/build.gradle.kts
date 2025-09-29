// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins { //프로젝트 전체에 포함시켜야할 정보
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false
}