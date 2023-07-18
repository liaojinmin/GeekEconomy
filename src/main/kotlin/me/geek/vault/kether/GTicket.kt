package me.geek.vault.kether

import me.geek.vault.api.DataManager.getDataCache
import taboolib.library.kether.ArgTypes
import taboolib.library.kether.ParsedAction
import taboolib.module.kether.*
import java.util.concurrent.CompletableFuture

/**
 * @作者: 老廖
 * @时间: 2023/4/28 12:49
 * @包: me.geek.vault.kether
 */
object GTicket {

    @KetherParser(value = ["GTicket"], shared = true)
    fun parser() = scriptParser {
        it.switch {
            case("give") {
                val a = it.next(ArgTypes.ACTION)
                actionNow {
                    newFrame(a).run<String>().thenApply { e ->
                        val ac = e.toInt()
                        if (ac > 0) {
                            player().getDataCache()?.givePlayerTicket(ac, true)
                        }
                    }
                }
            }
            case("take") {
                val a = it.next(ArgTypes.ACTION)
                actionNow {
                    newFrame(a).run<String>().thenApply { e ->
                        val ac = e.toInt()
                        if (ac > 0) {
                            player().getDataCache()?.takePlayerTicket(ac, true)
                        }
                    }
                }
            }
            case("get") {
                actionNow {
                    player().getDataCache()?.getPlayerTicket() ?: "0"
                }
            }
            case("has") {
                val a = it.next(ArgTypes.ACTION)
                Has(a)
            }
        }
    }
    class Has(private val key: ParsedAction<*>): KetherSub<Boolean>() {
        override fun run(frame: ScriptFrame): CompletableFuture<Boolean> {
            return frame.newFrame(key).run<Any>().thenApply {
                val ac = it.toString().toInt()
                (getPlayer(frame).getDataCache()?.getPlayerTicket() ?: 0) >= ac
            }
        }
    }
}