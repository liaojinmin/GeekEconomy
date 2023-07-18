package me.geek.vault.listener

import me.geek.vault.api.DataManager.waitData

import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerLoginEvent
import taboolib.common.platform.event.SubscribeEvent

/**
 * @作者: 老廖
 * @时间: 2023/4/27 21:50
 * @包: me.geek.vault.listener
 */
object PlayerJoin {

    @SubscribeEvent
    fun log(event: PlayerLoginEvent) {

    }

    @SubscribeEvent
    fun join(event: PlayerJoinEvent) {
        waitData(event.player.uniqueId, event.player)
    }
}