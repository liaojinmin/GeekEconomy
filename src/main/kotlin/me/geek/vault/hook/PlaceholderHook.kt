package me.geek.vault.hook

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import me.geek.vault.GeekEconomy
import me.geek.vault.api.DataManager
import me.geek.vault.api.DataManager.getDataCache
import org.bukkit.entity.Player

/**
 * @作者: 老廖
 * @时间: 2023/4/28 3:19
 * @包: me.geek.vault.hook
 */
class PlaceholderHook: PlaceholderExpansion() {


    // %gkv_money%
    // %gkv_money_top_name_(1/99)%
    // %gkv_money_top_amount_(1/99)%
    // %gkv_points%
    // %gkv_points_top_name_(1/99)%
    // %gkv_points_top_amount_(1/99)%
    // %gkv_ticket%
    // %gkv_ticket_top_name_(1/99)%
    // %gkv_ticket_top_amount_(1/99)%
    override fun onPlaceholderRequest(player: Player, s: String): String {
        when (s) {
            "money" -> {
                return player.getDataCache()?.getPlayerMoney()?.toString() ?: "0"
            }
            "points" -> {
                return player.getDataCache()?.getPlayerPoints()?.toString() ?: "0"
            }
            "ticket" -> {
                return player.getDataCache()?.getPlayerTicket()?.toString() ?: "0"
            }
            else -> {
                val index = s.filter { it.isDigit() }.toIntOrNull() ?: return "null"
                return when (s) {
                    "money_top_name_$index" -> {
                        DataManager.boardByMoney[index]?.user ?: "暂无"
                    }
                    "money_top_amount_$index" -> {
                        DataManager.boardByMoney[index]?.amount?.toString() ?: "暂无"
                    }
                    "points_top_name_$index" -> {
                        DataManager.boardByPoints[index]?.user ?: "暂无"
                    }
                    "points_top_amount_$index" -> {
                        DataManager.boardByPoints[index]?.amount?.toString() ?: "暂无"
                    }
                    "ticket_top_name_$index" -> {
                        DataManager.boardByTicket[index]?.user ?: "暂无"
                    }
                    "ticket_top_amount_$index" -> {
                        DataManager.boardByTicket[index]?.amount?.toString() ?: "暂无"
                    }
                    else -> {
                        "null"
                    }
                }
            }
        }
    }


    override fun getIdentifier(): String {
        return "gkv"
    }

    override fun getAuthor(): String {
        return "极客天上工作室"
    }

    override fun getVersion(): String {
        return GeekEconomy.VERSION.toString()
    }

}