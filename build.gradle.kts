val JUNIT_VERSION = "5.6.2"
val LOG4J_VERSION = "2.13.0"

plugins {
    java
    application
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("javax.servlet:javax.servlet-api:4.0.1")
    implementation("org.apache.logging.log4j:log4j-api:$LOG4J_VERSION")
    implementation("org.apache.logging.log4j:log4j-core:$LOG4J_VERSION")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$JUNIT_VERSION")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$JUNIT_VERSION")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$JUNIT_VERSION")
}

application {
    mainClass.set("otus.http.server.Application")
}
