package me.geek.vault.command

import me.geek.vault.api.DataManager
import me.geek.vault.api.DataManager.getDataCache
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.*


/**
 * @作者: 老廖
 * @时间: 2023/4/28 2:50
 * @包: me.geek.vault.command
 */
@CommandHeader("money", aliases = ["金币"])
object CommandMoney {

    @CommandBody
    val look = subCommand {
        execute<Player> { sender, _, _ ->
            sender.getDataCache()?.let {
                sender.sendMessage("§7你的金币: ${it.getPlayerMoney()}")
            }
        }
        dynamic("玩家") {
            execute<ProxyCommandSender> { sender, context, _ ->
                if (sender.isOp) {
                    val player = Bukkit.getOfflinePlayer(context["玩家"])
                    getDataCache(player.uniqueId)?.let {
                        sender.sendMessage("""
                            §a|
                            §a|§7玩家: §f${it.getPlayerNAME()}
                            §a|§7可用余额: §f${it.getPlayerMoney()}
                            §a|
                        """.trimIndent())
                    }
                }
            }
        }
    }

    @CommandBody(permission = "GeekEconomy.command.give", optional = true)
    val give = subCommand {
        dynamic("玩家") {
            suggestUncheck {
                DataManager.getAllDataCache().map { it.getPlayerNAME() }
            }
            dynamic("数量") {
                execute<ProxyCommandSender> { sender, context, _ ->
                    val target = Bukkit.getOfflinePlayer(context["玩家"])
                    val money = context["数量"].toIntOrNull()
                    if (money != null) {
                        getDataCache(target.uniqueId)?.let {
                            if (it.givePlayerMoney(money)) {
                                sender.sendMessage("""
                                    §a|
                                    §a|§7成功给予玩家: §f$money §7金币
                                    §a|§7可用余额: §f${it.getPlayerMoney()}
                                    §a|
                                    """.trimIndent())
                            } else {
                                sender.sendMessage("§c给与失败，你输入的数可能过大或是负数")
                            }
                        }
                    } else {
                        sender.sendMessage("§c你输入的不是数字")
                    }
                }
            }
        }
    }

    @CommandBody(permission = "GeekEconomy.command.take", optional = true)
    val take = subCommand {
        dynamic("玩家") {
            suggestUncheck {
                DataManager.getAllDataCache().map { it.getPlayerNAME() }
            }
            dynamic("数量") {
                execute<ProxyCommandSender> { sender, context, _ ->
                    val target = Bukkit.getOfflinePlayer(context["玩家"])
                    val money = context["数量"].toIntOrNull()
                    if (money != null) {
                        getDataCache(target.uniqueId)?.let {
                            if (it.takePlayerMoney(money)) {
                                sender.sendMessage("""
                                    §a|
                                    §a|§7成功扣除玩家: §f$money §7金币
                                    §a|§7可用余额: §f${it.getPlayerMoney()}
                                    §a|
                                    """.trimIndent())
                            } else {
                                sender.sendMessage("§c你输入的数字大于余额")
                            }
                        }
                    } else {
                        sender.sendMessage("§c你输入的不是数字")
                    }
                }
            }
        }
    }

    @CommandBody(permission = "GeekEconomy.command.set", optional = true)
    val set = subCommand {
        dynamic("玩家") {
            suggestUncheck {
                DataManager.getAllDataCache().map { it.getPlayerNAME() }
            }
            dynamic("数量") {
                execute<ProxyCommandSender> { sender, context, _ ->
                    val target = Bukkit.getOfflinePlayer(context["玩家"])
                    val money = context["数量"].toIntOrNull()
                    if (money != null) {
                        getDataCache(target.uniqueId)?.let {
                            if (it.setPlayerMoney(money)) {
                                sender.sendMessage("""
                                    §a|
                                    §a|§7设置玩家金币为: §f$money
                                    §a|§7可用余额: §f${it.getPlayerMoney()}
                                    §a|
                                    """.trimIndent())
                            } else {
                                sender.sendMessage("§c你输入的是负数")
                            }
                        }
                    } else {
                        sender.sendMessage("§c你输入的不是数字")
                    }
                }
            }
        }
    }
}