plugins {
    kotlin("jvm")
    id("application")
}

group = "xyz.qweru"
version = "1.0-SNAPSHOT"

val lwjglVersion = "3.3.6"
val jomlVersion = "1.10.8"

val lwjglNatives = Pair(
    System.getProperty("os.name")!!,
    System.getProperty("os.arch")!!
).let { (name, arch) ->
    when {
        "FreeBSD" == name ->
            "natives-freebsd"
        arrayOf("Linux", "SunOS", "Unit").any { name.startsWith(it) } ->
            if (arrayOf("arm", "aarch64").any { arch.startsWith(it) })
                "natives-linux${if (arch.contains("64") || arch.startsWith("armv8")) "-arm64" else "-arm32"}"
            else if (arch.startsWith("ppc"))
                "natives-linux-ppc64le"
            else if (arch.startsWith("riscv"))
                "natives-linux-riscv64"
            else
                "natives-linux"
        arrayOf("Mac OS X", "Darwin").any { name.startsWith(it) }     ->
            "natives-macos${if (arch.startsWith("aarch64")) "-arm64" else ""}"
        arrayOf("Windows").any { name.startsWith(it) }                ->
            if (arch.contains("64"))
                "natives-windows${if (arch.startsWith("aarch64")) "-arm64" else ""}"
            else
                "natives-windows-x86"
        else                                                                            ->
            throw Error("Unrecognized or unsupported platform. Please set \"lwjglNatives\" manually")
    }
}

/*
* import org.gradle.internal.os.OperatingSystem

val lwjglVersion = "3.3.6"
val jomlVersion = "1.10.8"

val lwjglNatives = Pair(
	System.getProperty("os.name")!!,
	System.getProperty("os.arch")!!
).let { (name, arch) ->
	when {
		"FreeBSD".equals(name)                                    ->
			"natives-freebsd"
		arrayOf("Linux", "SunOS", "Unit").any { name.startsWith(it) } ->
			if (arrayOf("arm", "aarch64").any { arch.startsWith(it) })
				"natives-linux${if (arch.contains("64") || arch.startsWith("armv8")) "-arm64" else "-arm32"}"
			else if (arch.startsWith("ppc"))
				"natives-linux-ppc64le"
			else if (arch.startsWith("riscv"))
				"natives-linux-riscv64"
			else
				"natives-linux"
		arrayOf("Mac OS X", "Darwin").any { name.startsWith(it) }     ->
			"natives-macos${if (arch.startsWith("aarch64")) "-arm64" else ""}"
		arrayOf("Windows").any { name.startsWith(it) }                ->
			if (arch.contains("64"))
				"natives-windows${if (arch.startsWith("aarch64")) "-arm64" else ""}"
			else
				"natives-windows-x86"
		else                                                                            ->
			throw Error("Unrecognized or unsupported platform. Please set \"lwjglNatives\" manually")
	}
}


repositories {
	mavenCentral()
}

dependencies {
	implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

	implementation("org.lwjgl", "lwjgl")
	implementation("org.lwjgl", "lwjgl-assimp")
	implementation("org.lwjgl", "lwjgl-freetype")
	implementation("org.lwjgl", "lwjgl-glfw")
	implementation("org.lwjgl", "lwjgl-meshoptimizer")
	implementation("org.lwjgl", "lwjgl-msdfgen")
	implementation("org.lwjgl", "lwjgl-nanovg")
	implementation("org.lwjgl", "lwjgl-nfd")
	implementation("org.lwjgl", "lwjgl-openal")
	implementation("org.lwjgl", "lwjgl-opengl")
	implementation("org.lwjgl", "lwjgl-stb")
	implementation ("org.lwjgl", "lwjgl", classifier = lwjglNatives)
	implementation ("org.lwjgl", "lwjgl-assimp", classifier = lwjglNatives)
	implementation ("org.lwjgl", "lwjgl-freetype", classifier = lwjglNatives)
	implementation ("org.lwjgl", "lwjgl-glfw", classifier = lwjglNatives)
	implementation ("org.lwjgl", "lwjgl-meshoptimizer", classifier = lwjglNatives)
	implementation ("org.lwjgl", "lwjgl-msdfgen", classifier = lwjglNatives)
	implementation ("org.lwjgl", "lwjgl-nanovg", classifier = lwjglNatives)
	implementation ("org.lwjgl", "lwjgl-nfd", classifier = lwjglNatives)
	implementation ("org.lwjgl", "lwjgl-openal", classifier = lwjglNatives)
	implementation ("org.lwjgl", "lwjgl-opengl", classifier = lwjglNatives)
	implementation ("org.lwjgl", "lwjgl-stb", classifier = lwjglNatives)
	implementation("org.joml", "joml", jomlVersion)
}*/

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(project(":multirender-api"))

    // LWJGL
    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

    implementation("org.lwjgl", "lwjgl")
    implementation("org.lwjgl", "lwjgl-assimp")
    implementation("org.lwjgl", "lwjgl-freetype")
    implementation("org.lwjgl", "lwjgl-glfw")
    implementation("org.lwjgl", "lwjgl-meshoptimizer")
    implementation("org.lwjgl", "lwjgl-msdfgen")
    implementation("org.lwjgl", "lwjgl-nanovg")
    implementation("org.lwjgl", "lwjgl-nfd")
    implementation("org.lwjgl", "lwjgl-openal")
    implementation("org.lwjgl", "lwjgl-opengl")
    implementation("org.lwjgl", "lwjgl-stb")
    implementation("org.lwjgl", "lwjgl", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-assimp", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-freetype", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-glfw", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-meshoptimizer", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-msdfgen", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-nanovg", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-nfd", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-openal", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-opengl", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-stb", classifier = lwjglNatives)
    implementation("org.joml", "joml", jomlVersion)
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}