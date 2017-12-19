package polybius.pbserver.messaging

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import polybius.common.models.Message

/**
 * Allows sending Message objects directly on the session
 * object
 */
fun WebSocketSession.sendMessage (msg: Message) {
    val str = ObjectMapper().writeValueAsString(msg)
    sendMessage(TextMessage(str))
}