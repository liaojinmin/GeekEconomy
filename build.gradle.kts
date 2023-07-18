
val taboolibVersion: String by project

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.7.20"
    id("io.izzel.taboolib") version "1.56"
}

taboolib {
    install(
        "common",
        "common-5",
        "platform-bukkit",
        "module-configuration",
        "module-kether",
        "module-metrics",
    )
    description {
        load("STARTUP")
        contributors {
            name("HSDLao_liao")
        }
        dependencies {
            bukkitApi("1.13")
            name("Vault").optional(true)
        }
    }
    relocate("com.zaxxer.hikari", "com.zaxxer.hikari_4_0_3_vault")
    relocate("me.geek.vault", group.toString())
    classifier = null
    version = taboolibVersion
}


repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://repo.tabooproject.org/repository/releases")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.codemc.org/repository/maven-public")
    maven("https://repo.rosewooddev.io/repository/public/")
    maven("https://repo.opencollab.dev/maven-snapshots/")
    maven("https://jitpack.io")
}


dependencies {

    compileOnly(kotlin("stdlib"))
    // Server Core
    compileOnly("ink.ptms.core:v11701:11701-minimize:mapped")
    compileOnly("ink.ptms.core:v11701:11701-minimize:universal")
    compileOnly("ink.ptms.core:v11701:11701")

    compileOnly("com.zaxxer:HikariCP:5.0.1")
    compileOnly("redis.clients:jedis:4.2.2")

    // Hook Plugins
    compileOnly("me.clip:placeholderapi:2.10.9") { isTransitive = false }

    compileOnly("com.github.MilkBowl:VaultAPI:-SNAPSHOT") { isTransitive = false }

    // Libraries
    compileOnly(fileTree("lib"))
}

