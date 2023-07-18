package me.geek.vault.api

import org.bukkit.entity.Player
import java.util.UUID

/**
 * @作者: 老廖
 * @时间: 2023/4/26 21:16
 * @包: me.geek.vault.data
 */
interface PlayerData {

    val player: Player?

    /**
     * 获取玩家UUID
     * @return 玩家UUID
     */
    fun getPlayerUUID(): UUID

    /**
     * 获取玩家名字
     * @return 玩家名称
     */
    fun getPlayerNAME(): String


    /*  点券 */
    /**
     * 直接设置玩家点券数量
     * @param amount 需要设置的数量
     * @return 如果更改成功 返回 true 反之 false
     */
    fun setPlayerPoints(amount: Int, eval: Boolean = true): Boolean

    /**
     * 为玩家添加点券
     * @param amount 需要给予的数量
     * @return 如果更改成功 返回 true 反之 false
     */
    fun givePlayerPoints(amount: Int, eval: Boolean = true): Boolean

    /**
     * 扣除玩家金币
     */
    fun takePlayerPoints(amount: Int, eval: Boolean = true): Boolean

    /**
     * 获取玩家点券数量
     * @return 数量
     */
    fun getPlayerPoints(): Int


    /* 金币 */

    /**
     * 直接设置玩家金币数量
     * @param amount 需要设置的数量
     * @return 如果更改成功 返回 true 反之 false
     */
    fun setPlayerMoney(amount: Int, eval: Boolean = true): Boolean

    /**
     * 为玩家添加金币
     * @param amount 需要给予的数量
     * @return 如果更改成功 返回 true 反之 false
     */
    fun givePlayerMoney(amount: Int, eval: Boolean = true): Boolean

    /**
     * 扣除玩家金币
     */
    fun takePlayerMoney(amount: Int, eval: Boolean = true): Boolean

    /**
     * 获取玩家金币数量
     * @return 数量
     */
    fun getPlayerMoney(): Int


    /* 月券 */

    /**
     * 直接设置玩家金币数量
     * @param amount 需要设置的数量
     * @return 如果更改成功 返回 true 反之 false
     */
    fun setPlayerTicket(amount: Int, eval: Boolean = true): Boolean

    /**
     * 为玩家添加金币
     * @param amount 需要给予的数量
     * @return 如果更改成功 返回 true 反之 false
     */
    fun givePlayerTicket(amount: Int, eval: Boolean = true): Boolean

    /**
     * 扣除玩家金币
     */
    fun takePlayerTicket(amount: Int, eval: Boolean = true): Boolean

    /**
     * 获取玩家金币数量
     * @return 数量
     */
    fun getPlayerTicket(): Int
}