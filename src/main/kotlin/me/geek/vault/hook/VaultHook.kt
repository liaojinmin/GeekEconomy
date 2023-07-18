package me.geek.vault.hook

import me.geek.vault.GeekEconomy
import me.geek.vault.api.DataManager
import net.milkbowl.vault.economy.AbstractEconomy
import net.milkbowl.vault.economy.EconomyResponse
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * @作者: 老廖
 * @时间: 2023/4/26 21:40
 * @包: me.geek.vault.hook
 */
class VaultHook: AbstractEconomy() {
    override fun isEnabled(): Boolean {
        return true
    }

    override fun getName(): String {
        return GeekEconomy.pluginName
    }

    override fun hasBankSupport(): Boolean {
        return false
    }

    override fun fractionalDigits(): Int {
        return 0
    }

    override fun format(amount: Double): String {
        val a = amount.toString()
        return a.substring(0, a.indexOf("."))
    }

    override fun currencyNamePlural(): String {
        return ""
    }

    override fun currencyNameSingular(): String {
        return ""
    }

    override fun hasAccount(playerName: String): Boolean {
        return true
    }

    override fun hasAccount(player: OfflinePlayer): Boolean {
        return true
    }

    override fun hasAccount(playerName: String?, worldName: String?): Boolean {
        return true
    }

    override fun getBalance(playerName: String): Double {
       return Bukkit.getOfflinePlayer(playerName)?.let {
           DataManager.getDataCache(it.uniqueId)?.getPlayerMoney()?.toDouble() ?: 0.0
        } ?: 0.0
    }

    override fun getBalance(player: OfflinePlayer): Double {
        return DataManager.getDataCache(player.uniqueId)?.getPlayerMoney()?.toDouble() ?: 0.0
    }

    override fun getBalance(playerName: String, world: String): Double {
        return getBalance(playerName)
    }

    override fun has(playerName: String, amount: Double): Boolean {
        return getBalance(playerName) >= amount
    }

    override fun has(playerName: String, worldName: String?, amount: Double): Boolean {
        return has(playerName, amount)
    }

    override fun withdrawPlayer(player: OfflinePlayer, amount: Double): EconomyResponse {
        val data = DataManager.getDataCache(player.uniqueId) ?: return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "玩家账户没有这么多钱")
        val bal = data.getPlayerMoney().toDouble()
        if (bal < amount) {
            return EconomyResponse(0.0, bal, EconomyResponse.ResponseType.FAILURE, "玩家账户没有这么多钱")
        }
        val ac = BigDecimal.valueOf(amount).setScale(0, RoundingMode.DOWN)
        data.takePlayerMoney(ac.toInt())
        return EconomyResponse(amount, bal, EconomyResponse.ResponseType.SUCCESS, "")
    }

    override fun withdrawPlayer(playerName: String, amount: Double): EconomyResponse {
        Bukkit.getOfflinePlayer(playerName).let {
           return withdrawPlayer(it, amount)
        }
    }

    override fun withdrawPlayer(playerName: String, worldName: String, amount: Double): EconomyResponse {
        return withdrawPlayer(playerName, amount)
    }

    /**
     * 给予
     */
    override fun depositPlayer(player: OfflinePlayer, amount: Double): EconomyResponse {
        val data = DataManager.getDataCache(player.uniqueId) ?: return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Max balance!");
        val bal = data.getPlayerMoney().toDouble()
        val ac = BigDecimal.valueOf(amount).setScale(0, RoundingMode.DOWN).toInt()
        if ((data.getPlayerMoney() + ac) >= Int.MAX_VALUE) {
            return EconomyResponse(0.0, bal, EconomyResponse.ResponseType.FAILURE, "Max balance!");
        }
        data.givePlayerMoney(ac)
        return EconomyResponse(amount, bal, EconomyResponse.ResponseType.SUCCESS, "")
    }

    override fun depositPlayer(playerName: String, amount: Double): EconomyResponse {
        Bukkit.getOfflinePlayer(playerName).let {
            return depositPlayer(it, amount)
        }
    }

    override fun depositPlayer(playerName: String, worldName: String, amount: Double): EconomyResponse {
        return depositPlayer(playerName, amount)
    }








    override fun createBank(name: String?, player: String?): EconomyResponse? {
        return null
    }

    override fun deleteBank(name: String?): EconomyResponse? {
       return null
    }

    override fun bankBalance(name: String?): EconomyResponse? {
        return null
    }

    override fun bankHas(name: String?, amount: Double): EconomyResponse? {
        return null
    }

    override fun bankWithdraw(name: String?, amount: Double): EconomyResponse? {
        return null
    }

    override fun bankDeposit(name: String?, amount: Double): EconomyResponse? {
        return null
    }

    override fun isBankOwner(name: String?, playerName: String?): EconomyResponse? {
        return null
    }

    override fun isBankMember(name: String?, playerName: String?): EconomyResponse? {
        return null
    }

    override fun getBanks(): MutableList<String>? {
        return null
    }


    override fun createPlayerAccount(playerName: String): Boolean {
        return true
    }

    override fun createPlayerAccount(playerName: String?, worldName: String?): Boolean {
        return true
    }
}