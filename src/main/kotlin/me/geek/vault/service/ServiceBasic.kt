package me.geek.vault.service

import me.geek.vault.RedisConfig
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig
import redis.clients.jedis.JedisPubSub
import taboolib.common.platform.function.submitAsync
import taboolib.common.platform.service.PlatformExecutor
import java.util.logging.Level

/**
 * 作者: 老廖
 * 时间: 2022/10/15
 **/
abstract class ServiceBasic(
    private val config: RedisConfig
): ServiceInit() {

    private var subscribeTask: PlatformExecutor.PlatformTask? = null


    private val jedisPool by lazy {
        if (config.password.isEmpty()) {
            val poolConfig = JedisPoolConfig()
            poolConfig.maxTotal = 10
            JedisPool(poolConfig, config.host, config.port, 5000, config.ssl)
        } else {
            JedisPool(JedisPoolConfig(), config.host, config.port, 5000, config.password, config.ssl)
        }
    }


    protected fun getRedisConnection() : Jedis {
        return this.jedisPool.resource
    }

    override fun onStart() {
        subscribeTask?.cancel()
        subscribeTask = submitAsync {
            getRedisConnection().use {
                if (it.isConnected) {
                    logger.log(Level.INFO, "serviceMessage 消息通道注册成功")
                } else {
                    logger.log(Level.WARNING, "无法与 Redis 服务器建立连接")
                    return@submitAsync
                }
                it.subscribe(object : JedisPubSub() {
                    override fun onMessage(channel: String, message: String) {
                        if (channel != serviceChannel) return
                        serviceMessage(message)
                    }
                }, serviceChannel)
            }
        }
    }

    override fun onClose() {
        jedisPool.close()
    }





}

