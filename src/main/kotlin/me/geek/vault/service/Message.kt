package me.geek.vault.service

class Message(

    val server: String,
) {
    var target: String = "all"

    var context: String = "null"

    var job: Job = Job.PLAYER_DATA_UP

    fun setTarget(server: String): Message {
        this.target = server
        return this
    }
    fun setContext(context: String): Message {
        this.context = context
        return this
    }
    fun setJob(job: Job): Message {
        this.job = job
        return this
    }
    override fun toString(): String {
        return "$server$division$target$division$context$division${job.name}"
    }

    companion object {

        fun redisBuild(server: String, builder: Message.() -> Unit): Message {
            if (server.isEmpty()) {
                error("server is empty")
            }
            return Message(server).also(builder)
        }

        const val division = "|"
        /**
         * data[0] server = 消息发送者
         * data[1] target = 目标服务器
         * data[2] job = 执行任务
         */
        fun format(data: List<String>) : Message? {
            if (data.size < 4) return null
            return Message(data[0])
                .setTarget(data[1])
                .setContext(data[2])
                .setJob(Job.valueOf(data[3]))
        }
    }
}