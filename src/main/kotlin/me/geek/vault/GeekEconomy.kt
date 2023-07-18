package me.geek.vault

import me.geek.vault.api.DataManager
import me.geek.vault.hook.PlaceholderHook
import me.geek.vault.hook.VaultHook
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.plugin.ServicePriority
import taboolib.common.env.DependencyScope
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency
import taboolib.common.platform.Platform
import taboolib.common.platform.PlatformSide
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.console
import taboolib.module.metrics.Metrics
import taboolib.platform.BukkitPlugin

@RuntimeDependencies(

    RuntimeDependency(value = "!com.zaxxer:HikariCP:4.0.3",
        relocate = ["!com.zaxxer.hikari", "!com.zaxxer.hikari_4_0_3_vault"]),
    RuntimeDependency(
        value = "org.apache.commons:commons-pool2:2.11.1",
        test = "org.apache.commons.pool2.impl.GenericObjectPoolConfig",
        transitive = false, ignoreOptional = true, scopes = [DependencyScope.PROVIDED],
    ),
    RuntimeDependency(value = "redis.clients:jedis:4.2.2",
        test = "redis.clients.jedis.exceptions.JedisException",
        transitive = false, ignoreOptional = true, scopes = [DependencyScope.PROVIDED]
    ),
)
@PlatformSide([Platform.BUKKIT])
object GeekEconomy: Plugin() {

    val instance by lazy { BukkitPlugin.getInstance() }

    const val VERSION = 1.0

    private lateinit var econ: Economy

    const val pluginName = "GeekEconomy"


    override fun onLoad() {
        console().sendMessage("")
        console().sendMessage("正在加载 §3§lGeekEconomy §f...  §8" + Bukkit.getVersion())
        console().sendMessage("")
    }

    override fun onEnable() {
        console().sendMessage("")
        console().sendMessage("       §aGeekEconomy§8-§6Plus  §bv$VERSION §7by §awww.geekcraft.ink")
        console().sendMessage("       §8适用于Bukkit: §71.16.5-1.19.4 §8当前: §7 ${Bukkit.getServer().version}")
        console().sendMessage("")
        loadVault()
        loadPapi()
        SetTings.onLoadSetTings()
        DataManager.start()
    }

    override fun onDisable() {
        DataManager.close()
    }

    override fun onActive() {
        Metrics( 18327, "V$VERSION", Platform.BUKKIT)
    }


    private fun loadVault() {
        if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            say("&7正在加载 &fVault &7...")
            econ = VaultHook()
            instance.server.servicesManager.register(Economy::class.java, econ, instance, ServicePriority.Normal)
        }
    }
    private fun loadPapi() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            say("&7正在加载 &fPlaceholderAPI &7...")
            PlaceholderHook().register()
        }
    }

    @JvmStatic
    fun say(msg: String) {
        console().sendMessage("§8[§aGeekEconomy§8] ${msg.replace("&", "§")}")
    }


    @JvmStatic
    fun debug(msg: String) {
        if(SetTings.DeBug) {
            console().sendMessage("§8[§aGeekEconomy§8]§8[§cDeBug§8]§7 ${msg.replace("&", "§")}")
        }
    }
}