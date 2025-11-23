plugins {
    id("org.springframework.boot") version "3.4.0" apply false
    id("io.spring.dependency-management") version "1.1.6" apply false
}

allprojects {
    group = "org.woonyong"
    version = "0.0.1"
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    configure<JavaPluginExtension> {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }

    dependencies {
        // Common dependencies for all modules
        add("implementation", "org.springframework.boot:spring-boot-starter")
        add("implementation", "org.springframework.boot:spring-boot-starter-actuator")
        add("implementation", "org.springframework.boot:spring-boot-starter-validation")

        add("developmentOnly", "org.springframework.boot:spring-boot-devtools")
        add("compileOnly", "org.projectlombok:lombok")
        add("annotationProcessor", "org.projectlombok:lombok")

        add("testImplementation", "org.springframework.boot:spring-boot-starter-test")
        add("testRuntimeOnly", "org.junit.platform:junit-platform-launcher")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    tasks.withType<org.springframework.boot.gradle.tasks.run.BootRun> {
        workingDir = rootProject.projectDir
    }
}
