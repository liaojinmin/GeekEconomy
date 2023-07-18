package me.geek.vault.api

import me.geek.vault.GeekEconomy
import me.geek.vault.SetTings
import me.geek.vault.DefaultPlayerData
import me.geek.vault.service.sql.*
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import taboolib.common.platform.Platform
import taboolib.common.platform.function.submitAsync
import taboolib.common.platform.service.PlatformExecutor
import java.sql.Connection
import java.sql.Statement
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

/**
 * @作者: 老廖
 * @时间: 2023/4/26 22:01
 * @包: me.geek.vault.api
 */
object DataManager {

    private val dataSub by lazy {
        if (SetTings.configSql.use_type.equals("mysql", ignoreCase = true)) {
            return@lazy Mysql(SetTings.configSql)
        } else return@lazy Sqlite(SetTings.configSql)
    }

    private val sqlImpl: SQLImpl by lazy { SQLImpl() }

    /**
     * 排行榜更新线程
     */
    private var borderTask: PlatformExecutor.PlatformTask? = null

    /**
     * 玩家数据缓存
     */
    private val playerCache: MutableMap<UUID, PlayerData> = ConcurrentHashMap()

    /**
     * 点券排行榜
     */
    val boardByPoints: MutableMap<Int, LeaderBoard> = mutableMapOf()

    /**
     * 金币排行榜
     */
    val boardByMoney: MutableMap<Int, LeaderBoard> = mutableMapOf()

    /**
     * 月券排行榜
     */
    val boardByTicket: MutableMap<Int, LeaderBoard> = mutableMapOf()


    fun getDataCache(uuid: UUID): PlayerData? {
        return playerCache[uuid]
    }

    fun getAllDataCache(): List<PlayerData> {
        return playerCache.values.toList()
    }

    fun Player.getDataCache(): PlayerData? {
        return getDataCache(this.uniqueId)
    }


    /**
     * 等待数据回调
     */
    fun waitData(uuid: UUID, player: Player?) {
        if (playerCache.containsKey(uuid)) {
            return
        }
        GeekEconomy.debug("未找到缓存，查询数据库数据...")
        if (Bukkit.isPrimaryThread()) {
            submitAsync {
                sqlImpl.select(uuid, player).also {
                    playerCache[it.getPlayerUUID()] = it
                }
            }
        } else {
            sqlImpl.select(uuid, player).also {
                playerCache[it.getPlayerUUID()] = it
            }
        }
    }


    fun savePlayerData(playerData: PlayerData, delete: Boolean = false) {
        saveToMysql(playerData)
        if (delete) playerCache.remove(playerData.getPlayerUUID())
    }

    private fun saveToMysql(data: PlayerData) {
        if (Bukkit.isPrimaryThread()) {
            submitAsync {
                sqlImpl.update(data)
            }
        } else {
            sqlImpl.update(data)
        }
    }

    fun start() {
        if (dataSub.isActive) return
        dataSub.onStart()
        if (dataSub.isActive) {
            dataSub.createTab {
                getConnection().use {
                    createStatement().action { statement ->
                        if (dataSub is Mysql) {
                            statement.addBatch(SqlTab.MYSQL_1.tab)
                        } else {
                            statement.addBatch("PRAGMA foreign_keys = ON;")
                            statement.addBatch("PRAGMA encoding = 'UTF-8';")
                            statement.addBatch(SqlTab.SQLITE_1.tab)
                        }
                        statement.executeBatch()
                    }
                }
            }
        }
        borderTask?.cancel()
        borderTask = submitAsync(period = 600 * 20) {
            GeekEconomy.debug("启动排行榜更新任务...")
            sqlImpl.selectTopByMoney()
            sqlImpl.selectTopByPoints()
            sqlImpl.selectTopByTicket()
        }
    }

    fun close() {
        borderTask?.cancel()
        dataSub.onClose()
    }


    private fun getConnection(): Connection {
        return dataSub.getConnection()
    }



    /**
     * 数据库实现类
     * 所以数据库操作私有
     */
    private class SQLImpl {

        fun insert(data: PlayerData) {
            if (dataSub.isActive) {
                getConnection().use {
                    prepareStatement(
                        "INSERT INTO player_data(`uuid`,`user`,`points`,`money`,`ticket`) VALUES(?,?,?,?,?);"
                    ).actions {
                        it.setString(1, data.getPlayerUUID().toString())
                        it.setString(2, data.getPlayerNAME())
                        it.setInt(3, data.getPlayerPoints())
                        it.setInt(4, data.getPlayerMoney())
                        it.setInt(5, data.getPlayerTicket())
                        it.executeUpdate()
                    }
                }
            }
        }

        fun delete(data: PlayerData) {
            TODO()
        }

        fun update(data: PlayerData) {
            if (dataSub.isActive) {
                getConnection().use {
                    prepareStatement(
                        "UPDATE `player_data` SET `user`=?,`points`=?,`money`=?,`ticket`=? WHERE `uuid`=?;"
                    ).actions {
                        it.setString(1, data.getPlayerNAME())
                        it.setInt(2, data.getPlayerPoints())
                        it.setInt(3, data.getPlayerMoney())
                        it.setInt(4, data.getPlayerTicket())
                        it.setString(5, data.getPlayerUUID().toString())
                        it.executeUpdate()
                    }
                }
            }
        }

        fun select(uuid: UUID, player: Player?): PlayerData {
            var data: PlayerData? = null
            if (dataSub.isActive) {
              getConnection().use {
                  prepareStatement(
                      "SELECT `user`,`points`,`money`,`ticket` FROM `player_data` WHERE `uuid`=?;"
                  ).actions {
                      it.setString(1, uuid.toString())
                      val res = it.executeQuery()
                      data = if (res.next()) {
                          DefaultPlayerData(
                              null,
                              res.getInt("points"),
                              res.getInt("money"),
                              res.getInt("ticket"),
                              uuid,
                              player?.name ?: res.getString("user")
                          )
                      } else {
                          GeekEconomy.debug("无法从数据库取得数据，返回默认值")
                          DefaultPlayerData(player, 0, 0, 0, uuid, player?.name ?: "").also { d ->
                              insert(d)
                          }
                      }
                  }
              }
            }
            return data ?: error("查询数据时发生异常...")
        }

        fun selectTopByPoints(limits: Int = 30) {
            if (dataSub.isActive) {
                getConnection().use {
                    this.createStatement().action {
                        val res = it.executeQuery("SELECT `uuid`,`user`,`points` FROM `player_data` ORDER BY `points` DESC limit $limits")
                        var index = 1
                        while (res.next()) {
                            boardByPoints[index] = LeaderBoard(
                                UUID.fromString(res.getString("uuid")),
                                res.getString("user"),
                                res.getInt("points"))
                            index++
                        }
                    }
                }
            }
        }
        fun selectTopByMoney(limits: Int = 30) {
            if (dataSub.isActive) {
                getConnection().use {
                    this.createStatement().action {
                        val res = it.executeQuery("SELECT `uuid`,`user`,`money` FROM `player_data` ORDER BY `money` DESC limit $limits")
                        var index = 1
                        while (res.next()) {
                            boardByMoney[index] = LeaderBoard(
                                UUID.fromString(res.getString("uuid")),
                                res.getString("user"),
                                res.getInt("money"))
                            index++
                        }
                    }
                }
            }
        }
        fun selectTopByTicket(limits: Int = 30) {
            if (dataSub.isActive) {
                getConnection().use {
                    this.createStatement().action {
                        val res = it.executeQuery("SELECT `uuid`,`user`,`ticket` FROM `player_data` ORDER BY `ticket` DESC limit $limits")
                        var index = 1
                        while (res.next()) {
                            boardByTicket[index] = LeaderBoard(
                                UUID.fromString(res.getString("uuid")),
                                res.getString("user"),
                                res.getInt("ticket"))
                            index++
                        }
                    }
                }
            }
        }
    }

    private enum class SqlTab(val tab: String) {
        SQLITE_1(
            "CREATE TABLE IF NOT EXISTS `player_data` (" +
                    " `uuid` CHAR(36) NOT NULL UNIQUE PRIMARY KEY, " +
                    " `user` varchar(36) NOT NULL, " +
                    " `points` BIGINT(20) NOT NULL, " +
                    " `money` BIGINT(20) NOT NULL, " +
                    " `ticket` BIGINT(20) NOT NULL" +
                    ");"),

        MYSQL_1(
            "CREATE TABLE IF NOT EXISTS `player_data` (" +
                    " `uuid` CHAR(36) NOT NULL UNIQUE," +
                    " `user` varchar(36) NOT NULL," +
                    " `points` int(36) NOT NULL," +
                    " `money` int(36) NOT NULL," +
                    " `ticket` int(36) NOT NULL," +
                    " `contexts` VARCHAR(200) NOT NULL DEFAULT ''," +
                    "PRIMARY KEY (`uuid`)" +
                    ");"
        ),
    }

}