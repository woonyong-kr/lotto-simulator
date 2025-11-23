dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    runtimeOnly("com.h2database:h2")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
}

springBoot {
    mainClass = "org.woonyong.lotto.bot.BotClientApplication"
}

tasks.bootJar {
    archiveBaseName = "lotto-bot-client"
    archiveVersion = ""
}