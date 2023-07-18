package me.geek.vault

import me.geek.vault.service.sql.ConfigSql
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.Platform
import taboolib.common.platform.PlatformSide
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile
import taboolib.module.configuration.Configuration.Companion.getObject
import kotlin.system.measureTimeMillis

/**
 * 作者: 老廖
 * 时间: 2023/4/26
 *
 **/
@PlatformSide([Platform.BUKKIT])
object SetTings {

    @Config(value = "settings.yml", autoReload = true)
    lateinit var config: ConfigFile
        private set

    @Awake(LifeCycle.ENABLE)
    fun init() {
        config.onReload { onLoadSetTings() }
    }

    lateinit var configSql: ConfigSql
    lateinit var redisConfig: RedisConfig

    var DeBug: Boolean = false

    fun onLoadSetTings() {
        measureTimeMillis {
            DeBug = config.getBoolean("debug", false)
            configSql = config.getObject("data_storage", false)
            redisConfig = config.getObject("Redis", false)
            configSql.sqlite = GeekEconomy.instance.dataFolder
        }
    }

}