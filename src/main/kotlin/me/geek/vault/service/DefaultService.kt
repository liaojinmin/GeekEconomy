package me.geek.vault.service

import me.geek.vault.RedisConfig
import me.geek.vault.service.Message.Companion.redisBuild
import java.util.*


class DefaultService(data: RedisConfig): ServiceBasic(data) {

    override fun sendMessage(messagePack: Message) {
        getRedisConnection().use {
            it.publish(serviceChannel, messagePack.toString())
        }
    }

    override fun sendPlayerPointsUp(uuid: UUID, amount: Int) {
        sendMessage(redisBuild(locServer) {
            target = uuid.toString()
            job = Job.PLAYER_UP_POINTS
            context = amount.toString()
        })
    }

    override fun sendPlayerMoneyUp(uuid: UUID, amount: Int) {
        sendMessage(redisBuild(locServer) {
            target = uuid.toString()
            job = Job.PLAYER_UP_MONEY
            context = amount.toString()
        })
    }

    override fun sendPlayerTicketUp(uuid: UUID, amount: Int) {
        sendMessage(redisBuild(locServer) {
            target = uuid.toString()
            job = Job.PLAYER_UP_TICKET
            context = amount.toString()
        })
    }

}