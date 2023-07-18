package me.geek.vault

import me.geek.vault.api.PlayerData
import me.geek.vault.api.event.EventAction
import me.geek.vault.api.event.PlayerMoneyChangeEvent
import me.geek.vault.api.event.PlayerPointsChangeEvent
import org.bukkit.entity.Player
import java.util.*

/**
 * @作者: 老廖
 * @时间: 2023/4/26 21:41
 * @包: me.geek.vault.service
 */
class DefaultPlayerData(
    override val player: Player?,
    private var points: Int = 0,
    private var money: Int = 0,
    private var ticket: Int = 0,
    private val uuid: UUID,
    private val name: String
): PlayerData {


    override fun getPlayerUUID(): UUID {
        return this.uuid
    }

    override fun getPlayerNAME(): String {
        return this.name
    }


    /* 点券操作 */
    override fun setPlayerPoints(amount: Int, eval: Boolean): Boolean {
        if (amount <= 0) {
            return false
        }
        if (eval) {
            val evt = PlayerPointsChangeEvent(this, this.points, amount, EventAction.SET)
            evt.call()
        }
        this.points = amount
        return true
    }

    override fun givePlayerPoints(amount: Int, eval: Boolean): Boolean {
        if (amount <= 0) {
            return false
        }
        val r = this.points + amount
        if (this.points xor r and amount xor r < 0) {
            return false
        }
        if (eval) {
            val evt = PlayerPointsChangeEvent(this, this.points, points+amount, EventAction.GIVE)
            evt.call()
        }
        this.points+=amount
        return true
    }

    override fun takePlayerPoints(amount: Int, eval: Boolean): Boolean {
        if (amount > this.points  || amount <= 0) {
            return false
        }
        if (eval) {
            val evt = PlayerPointsChangeEvent(this, this.points, points-amount, EventAction.TAKE)
            evt.call()
        }
        this.points-=amount
        return true
    }

    override fun getPlayerPoints(): Int {
        return this.points
    }




    /* 金币操作 */
    override fun setPlayerMoney(amount: Int, eval: Boolean): Boolean {
        if (amount <= 0) {
            return false
        }
        if (eval) {
            val evt = PlayerMoneyChangeEvent(this, this.money, amount, EventAction.SET)
            evt.call()
        }
        this.money = amount
        return true
    }

    override fun givePlayerMoney(amount: Int, eval: Boolean): Boolean {
        GeekEconomy.debug("收到数量: $amount by give")
        if (amount <= 0) {
            return false
        }
        val r = this.money + amount
        if (money xor r and amount xor r < 0) {
            return false
        }
        if (eval) {
            val evt = PlayerMoneyChangeEvent(this, this.money, money+amount, EventAction.GIVE)
            evt.call()
        }
        this.money+=amount
        return true
    }


    override fun takePlayerMoney(amount: Int, eval: Boolean): Boolean {
        GeekEconomy.debug("收到数量: $amount 当前拥有:$money by take ac=${amount > this.money}")
        if (amount > this.money || amount <= 0) {
            return false
        }

        if (eval) {
            val evt = PlayerMoneyChangeEvent(this, this.money, money-amount, EventAction.TAKE)
            evt.call()
        }
        this.money-=amount
        return true
    }

    override fun getPlayerMoney(): Int {
        return money
    }


    /* 月券 */

    override fun setPlayerTicket(amount: Int, eval: Boolean): Boolean {
        if (amount <= 0) return false

        if (eval) {
            val evt = PlayerMoneyChangeEvent(this, this.ticket, amount, EventAction.SET)
            evt.call()
        }
        this.ticket = amount
        return true
    }

    override fun givePlayerTicket(amount: Int, eval: Boolean): Boolean {
        if (amount <= 0) return false

        val r = this.ticket + amount
        if (this.ticket xor r and amount xor r < 0) {
            return false
        }
        if (eval) {
            val evt = PlayerMoneyChangeEvent(this, this.ticket, ticket+amount, EventAction.GIVE)
            evt.call()
        }
        this.ticket+=amount
        return true
    }

    override fun takePlayerTicket(amount: Int, eval: Boolean): Boolean {
        if (this.ticket < amount || amount <= 0) return false

        if (eval) {
            val evt = PlayerMoneyChangeEvent(this, this.ticket, ticket-amount, EventAction.TAKE)
            evt.call()
        }
        this.ticket-=amount
        return true
    }

    override fun getPlayerTicket(): Int {
        return this.ticket
    }

}