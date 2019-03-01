/*
 * Copyright (C) 2019 Maciej Laskowski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    id("java-library")
}

repositories {
    mavenLocal()
    maven { url = uri("https://plugins.gradle.org/m2/") }
    maven { url = uri("http://repo1.maven.org/maven2") }
    maven { url = uri("https://oss.sonatype.org/content/groups/staging/") }
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
}

// -----------------------------------------------------------------------------
// Dependencies
// -----------------------------------------------------------------------------
dependencies {
    implementation(platform("io.knotx:knotx-dependencies:2.0.0-SNAPSHOT"))
    implementation(group = "io.vertx", name = "vertx-rx-java2")
    implementation(group = "io.vertx", name = "vertx-web-api-contract")
    implementation(group = "io.vertx", name = "vertx-auth-jwt")
    implementation(group = "io.vertx", name = "vertx-auth-shiro")

    testImplementation("io.knotx:knotx-junit5")
    testImplementation("io.knotx:knotx-launcher")
    testImplementation(group = "io.knotx", name = "knotx-launcher", classifier = "tests")

    testImplementation(group = "io.vertx", name = "vertx-junit5")
    testImplementation(group = "io.vertx", name = "vertx-unit")
    testImplementation(group = "io.rest-assured", name = "rest-assured", version = "3.3.0")
//    testImplementation(group = "io.jsonwebtoken", name = "jjwt-jackson", version = "0.10.5")

}

plugins.withId("java-library") {
    tasks.withType<JavaCompile>().configureEach {
        with(options) {
            sourceCompatibility = "1.8"
            targetCompatibility = "1.8"
            compilerArgs = listOf("-parameters")
            encoding = "UTF-8"
        }
    }

    tasks.withType<Test>().configureEach {
        environment("vertx.logger-delegate-factory-class-name", "io.vertx.core.logging.SLF4JLogDelegateFactory")

        failFast = true
        useJUnitPlatform()
        testLogging {
            events = setOf(TestLogEvent.FAILED)
            exceptionFormat = TestExceptionFormat.SHORT
        }

        dependencies {
            "testImplementation"("org.junit.jupiter:junit-jupiter-api")
            "testRuntimeOnly"("org.junit.jupiter:junit-jupiter-engine")
        }
    }
}