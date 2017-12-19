package polybius.pbserver.messaging

import org.slf4j.LoggerFactory
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession
import polybius.common.models.Message
import polybius.common.models.MessageType
import polybius.pbserver.repo.ExecutorSessionRegistry

@Component
class MessageHandler(
        val executorSessionRegistry: ExecutorSessionRegistry
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @MessageMapping("/executors")
    fun handler(msg: Message, session: WebSocketSession) {
        when (msg.messageType) {
            MessageType.EXECUTOR_CONNECTED -> registerSession(session)
            else -> sendError(session, "Invalid message for channel")
        }
    }

    private fun registerSession(session: WebSocketSession) {
        log.info("Executor connected: {}", session.id)
        executorSessionRegistry.add(session)
    }

    private fun sendError(session: WebSocketSession, error: String) {
        session.sendMessage(Message.error(error))
    }

}