dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
}

springBoot {
    mainClass = "org.woonyong.lotto.pos.terminal.PosTerminalApplication"
}

tasks.bootJar {
    archiveBaseName = "lotto-pos-terminal"
    archiveVersion = ""
}