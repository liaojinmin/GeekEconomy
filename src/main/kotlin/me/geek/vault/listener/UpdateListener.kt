package me.geek.vault.listener

import me.geek.vault.api.DataManager
import me.geek.vault.api.event.PlayerMoneyChangeEvent
import me.geek.vault.api.event.PlayerPointsChangeEvent
import me.geek.vault.api.event.PlayerTicketChangeEvent
import me.geek.vault.service.ServiceManager
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submitAsync

/**
 * @作者: 老廖
 * @时间: 2023/4/28 2:29
 * @包: me.geek.vault.listener
 */
object UpdateListener {

    @SubscribeEvent
    fun points(event: PlayerPointsChangeEvent) {
        submitAsync {
            ServiceManager.redisImpl?.sendPlayerPointsUp(event.data.getPlayerUUID(), event.new)
            DataManager.savePlayerData(event.data)
        }
    }

    @SubscribeEvent
    fun money(event: PlayerMoneyChangeEvent) {
        submitAsync {
            ServiceManager.redisImpl?.sendPlayerMoneyUp(event.data.getPlayerUUID(), event.new)
            DataManager.savePlayerData(event.data)
        }
    }

    @SubscribeEvent
    fun ticket(event: PlayerTicketChangeEvent) {
        submitAsync {
            ServiceManager.redisImpl?.sendPlayerMoneyUp(event.data.getPlayerUUID(), event.new)
            DataManager.savePlayerData(event.data)
        }
    }
}