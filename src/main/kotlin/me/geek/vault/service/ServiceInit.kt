package me.geek.vault.service

import me.geek.vault.service.ServiceManager.addTask
import org.bukkit.Bukkit
import java.util.logging.Logger


abstract class ServiceInit: ServiceAction {

    val serviceChannel = "GeekEconomy"

    val logger: Logger = Logger.getLogger(serviceChannel)

    val locServer = Bukkit.getServer().port.toString()

    abstract fun sendMessage(messagePack: Message)

    abstract fun onStart()
    abstract fun onClose()

    protected fun serviceMessage(msg: String) {
        val data = msg.split(Message.division)
        if (data.size == 4) {
            if (data[0] != locServer) {
                Message.format(data)?.addTask()
            }
        }
    }

}