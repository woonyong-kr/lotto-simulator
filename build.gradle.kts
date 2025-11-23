plugins {
    id("org.springframework.boot") version "3.4.0" apply false
    id("io.spring.dependency-management") version "1.1.4" apply false
}

group = "org.woonyong"
version = "1.0.0"

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    
    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.addAll(listOf(
            "-parameters",
            "-Xlint:unchecked",
            "-Xlint:deprecation"
        ))
    }
    
    tasks.withType<Test> {
        useJUnitPlatform()
    }
    
    dependencies {
        val lombokVersion = "1.18.30"
        val junitVersion = "5.10.1"
        val assertjVersion = "3.24.2"
        
        "compileOnly"("org.projectlombok:lombok:$lombokVersion")
        "annotationProcessor"("org.projectlombok:lombok:$lombokVersion")
        
        "testImplementation"("org.junit.jupiter:junit-jupiter:$junitVersion")
        "testImplementation"("org.assertj:assertj-core:$assertjVersion")
        "testCompileOnly"("org.projectlombok:lombok:$lombokVersion")
        "testAnnotationProcessor"("org.projectlombok:lombok:$lombokVersion")
    }
}

configure(subprojects.filter { it.name != "lotto-core" }) {
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    
    dependencies {
        "implementation"(project(":lotto-core"))
        
        "implementation"("org.springframework.boot:spring-boot-starter-web")
        "implementation"("org.springframework.boot:spring-boot-starter-validation")
        
        "testImplementation"("org.springframework.boot:spring-boot-starter-test")
    }
}

project(":lotto-central-server") {
    dependencies {
        "implementation"("org.springframework.boot:spring-boot-starter-data-jpa")
        "runtimeOnly"("org.postgresql:postgresql")
    }
}

