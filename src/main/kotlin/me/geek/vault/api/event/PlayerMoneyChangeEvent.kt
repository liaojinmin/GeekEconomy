package me.geek.vault.api.event

import me.geek.vault.api.PlayerData
import taboolib.platform.type.BukkitProxyEvent

/**
 * @作者: 老廖
 * @时间: 2023/4/28 1:28
 * @包: me.geek.vault.api.event
 */
class PlayerMoneyChangeEvent(
    val data: PlayerData,
    val old: Int,
    val new: Int,
    val eventAction: EventAction
): BukkitProxyEvent()