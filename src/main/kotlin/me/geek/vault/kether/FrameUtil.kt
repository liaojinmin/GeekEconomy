package me.geek.vault.kether

import org.bukkit.entity.Player
import taboolib.module.kether.ScriptFrame
import taboolib.module.kether.script

fun ScriptFrame.player() = script().sender?.castSafely<Player>() ?: error("unknown player")