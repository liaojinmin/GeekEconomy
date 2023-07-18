package me.geek.vault.service

import java.util.UUID


interface ServiceAction {

    fun sendPlayerPointsUp(uuid: UUID, amount: Int)

    fun sendPlayerMoneyUp(uuid: UUID, amount: Int)

    fun sendPlayerTicketUp(uuid: UUID, amount: Int)

}