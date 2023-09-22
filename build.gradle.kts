import com.canhub.purity.Versions

plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.application").version("8.1.1").apply(false)
    id("com.android.library").version("8.1.1").apply(false)
    kotlin("android").version("1.9.10").apply(false)
    kotlin("multiplatform").version("1.8.21").apply(false)
    id("io.gitlab.arturbosch.detekt") version "1.23.1"
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}


detekt {
    // Version of detekt that will be used. When unspecified the latest detekt
    // version found will be used. Override to stay on the same version.
    toolVersion = Versions.detekt

    // The directories where detekt looks for source files.
    // Defaults to `files("src/main/java", "src/test/java", "src/main/kotlin", "src/test/kotlin")`.
    source.setFrom("src/main/java", "src/main/kotlin")

    // Builds the AST in parallel. Rules are always executed in parallel.
    // Can lead to speedups in larger projects. `false` by default.
    parallel = true

    // Define the detekt configuration(s) you want to use.
    // Defaults to the default detekt configuration.
    config.setFrom("path/to/default-detekt-config.yml")

    // Applies the config files on top of detekt's default config file. `false` by default.
    buildUponDefaultConfig = true

    // Turns on all the rules. `false` by default.
    allRules = true

    // Specifying a baseline file. All findings stored in this file in subsequent runs of detekt.
    baseline = file("path/to/baseline.xml")

    // Disables all default detekt rulesets and will only run detekt with custom rules
    // defined in plugins passed in with `detektPlugins` configuration. `false` by default.
    disableDefaultRuleSets = false

    // Adds debug output during task execution. `false` by default.
    debug = false

    // If set to `true` the build does not fail when the
    // maxIssues count was reached. Defaults to `false`.
    ignoreFailures = false

    // Android: Don't create tasks for the specified build types (e.g. "release")
    ignoredBuildTypes = listOf("release")

    // Android: Don't create tasks for the specified build flavor (e.g. "production")
    ignoredFlavors = listOf("production")

    // Android: Don't create tasks for the specified build variants (e.g. "productionRelease")
    ignoredVariants = listOf("productionRelease")

    // Specify the base path for file paths in the formatted reports.
    // If not set, all file paths reported will be absolute file path.
    basePath = projectDir.absolutePath
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    this.jvmTarget = "1.8"
    jdkHome.set(file("path/to/jdkHome"))
}
tasks.withType<io.gitlab.arturbosch.detekt.DetektCreateBaselineTask>().configureEach {
    this.jvmTarget = "1.8"
    jdkHome.set(file("path/to/jdkHome"))
}
tasks.register<io.gitlab.arturbosch.detekt.Detekt>("detektAllModules") {
    description = "Runs a custom detekt build."
    autoCorrect = false
    setSource(files(projectDir))
    config.setFrom(files("$rootDir/gradle/detekt/default-detekt-default-detekt-config.yml"))
    debug = true
    reports {
        xml { destination = file("build/reports/mydetekt.xml") }
        html.destination = file("build/reports/mydetekt.html")
    }
    include("**/*.kt")
    include("**/*.kts")
    exclude("**/resources/**")
    exclude("**/build/**")
    exclude("**/androidTest/**")
}
tasks.register<io.gitlab.arturbosch.detekt.Detekt>("detektAllModulesWithAutocorrect") {
    description = "Runs a custom detekt build."
    autoCorrect = true
    setSource(files(projectDir))
    config.setFrom(files("$rootDir/gradle/detekt/default-detekt-config.yml"))
    debug = true
    reports {
        xml { destination = file("build/reports/mydetekt.xml") }
        html.destination = file("build/reports/mydetekt.html")
    }
    include("**/*.kt")
    include("**/*.kts")
    exclude("**/resources/**")
    exclude("**/build/**")
    exclude("**/androidTest/**")
}

tasks.register<Copy>("copyGitHooks") {
    from("$rootDir/.github/git-hooks/") {
        include("**/*.sh")
        rename("(.*).sh", "$1")
    }
    into("$rootDir/.git/hooks")
}

tasks.register<Exec>("makeGitHooksExecutable") {
    workingDir(rootDir)
    commandLine("chmod")
    args("-R", "+x", ".git/hooks/")
    dependsOn("copyGitHooks")
}

tasks.findByPath(":androidApp:clean")?.dependsOn(":makeGitHooksExecutable")
