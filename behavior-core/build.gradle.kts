plugins {
    id("java-library")
}

group = "org.woonyong"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("ch.qos.logback:logback-classic:1.4.11")
}

tasks.withType<JavaCompile> {
    options.annotationProcessorGeneratedSourcesDirectory =
        file("$buildDir/generated/sources/annotationProcessor/java/main")
}