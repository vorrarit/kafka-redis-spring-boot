import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.2.6.RELEASE"
	id("io.spring.dependency-management") version "1.0.9.RELEASE"
	kotlin("jvm") version "1.3.71"
	kotlin("plugin.spring") version "1.3.71"
	`maven-publish`
}

group = "th.co.bitfactory.testkafka"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.springframework.kafka:spring-kafka")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
	testImplementation("org.springframework.kafka:spring-kafka-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}

val sourcesJar by tasks.registering(Jar::class) {
	classifier = "sources"
	from(sourceSets.main.get().allSource)
}

publishing {
	repositories {
		maven {
			url = uri("http://172.18.0.1:8081/repository/maven-snapshots")
			credentials {
				username = "admin"
				password = "password"
			}
		}
	}

	publications {
		create<MavenPublication>("default") {
			artifact(project.file("file:dummy:class.ext"))

			//from(org.gradle.api.plugins.internal.JvmPluginsHelper.findJavaComponent(project.components))
			from(components["java"])
			//artifact(sourcesJar.get())
		}
	}
}
