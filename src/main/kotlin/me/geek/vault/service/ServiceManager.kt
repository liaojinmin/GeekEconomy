package me.geek.vault.service

import me.geek.vault.GeekEconomy
import me.geek.vault.SetTings
import me.geek.vault.api.DataManager
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.Platform
import taboolib.common.platform.PlatformSide
import taboolib.common.platform.function.submitAsync
import taboolib.common.platform.service.PlatformExecutor
import java.util.*

/**
 * @作者: 老廖
 * @时间: 2023/4/28 1:46
 * @包: me.geek.vault.service
 */
@PlatformSide([Platform.BUKKIT])
object ServiceManager {

    private val taskPack: MutableList<Message> = mutableListOf()

    private var serviceTask: PlatformExecutor.PlatformTask? = null

    val redisImpl by lazy {
        if (SetTings.redisConfig.use) {
            DefaultService(SetTings.redisConfig)
        } else null
    }

    fun Message.addTask() {
        taskPack.add(this)
    }

    @Awake(LifeCycle.ACTIVE)
    fun start() {
        redisImpl?.onStart()
        if (redisImpl != null) {
            serviceTask?.cancel()
            serviceTask = submitAsync(period = 20) {
                try {
                    val a = taskPack.listIterator()
                    while (a.hasNext()) {
                        val c = a.next()
                        GeekEconomy.say("&8Server&a@&7${c.server} &8target&a@&7${c.target} &8Job&a@&7${c.job.name}&aBy&7${c.context}")
                        eval(c)
                        a.remove()
                    }
                } catch (ex: Throwable) {
                    ex.printStackTrace()
                }
            }
        }
    }

    @Awake(LifeCycle.DISABLE)
    fun close() {
        redisImpl?.onClose()
        serviceTask?.cancel()
    }


    private fun eval(redis: Message) {
        when (redis.job) {
            Job.PLAYER_DATA_UP -> TODO()
            Job.PLAYER_UP_POINTS -> {
                val uuid = UUID.fromString(redis.target)
                DataManager.getDataCache(uuid)?.setPlayerPoints(redis.context.toInt(), false)
                return
            }
            Job.PLAYER_UP_MONEY -> {
                val uuid = UUID.fromString(redis.target)
                DataManager.getDataCache(uuid)?.setPlayerMoney(redis.context.toInt(), false)
                return
            }

            Job.PLAYER_UP_TICKET -> {
                val uuid = UUID.fromString(redis.target)
                DataManager.getDataCache(uuid)?.setPlayerTicket(redis.context.toInt(), false)
                return
            }
        }
    }
}