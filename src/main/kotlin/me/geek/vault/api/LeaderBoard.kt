package me.geek.vault.api

import java.util.UUID

/**
 * @作者: 老廖
 * @时间: 2023/4/28 11:01
 * @包: me.geek.vault.api
 */
data class LeaderBoard(
    val uuid: UUID,
    val user: String,
    val amount: Int
)