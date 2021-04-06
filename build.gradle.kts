plugins {
    val kotlinVersion = "1.4.31"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.4.0"
}

group = "top.wsure.thesaurus"
version = "0.0.1"

repositories {
    mavenLocal()
    maven("https://maven.aliyun.com/repository/public") // 阿里云国内代理仓库
    mavenCentral()
    jcenter()
}

dependencies {
    val exposedVersion = "0.29.1"
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.0")

    implementation("com.squareup.okhttp3:okhttp:4.9.0")

    implementation("com.h2database:h2:1.4.200")

    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")

    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")

    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")

    implementation("org.jetbrains.exposed:exposed-jodatime:$exposedVersion")

}
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}