package polybius.pbserver.messaging

import org.slf4j.LoggerFactory
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Component
import polybius.common.models.Message
import polybius.common.models.MessageType
import polybius.pbserver.repo.ExecutorSessionRegistry

@Component
class MessageHandler(
        val executorSessionRegistry: ExecutorSessionRegistry
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @MessageMapping("/executors")
    fun handler(msg: Message) {
        if (msg.messageType == MessageType.EXECUTOR_CONNECTED) {
            log.info("Executor connected")
        }
    }

}