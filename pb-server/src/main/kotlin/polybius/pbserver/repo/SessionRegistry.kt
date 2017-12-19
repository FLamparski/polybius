package polybius.pbserver.repo

import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap

abstract class SessionRegistry {
    private val sessions = ConcurrentHashMap<String, WebSocketSession>()

    fun add(session: WebSocketSession) {
        sessions[session.id] = session
    }

    fun remove(id: String) = sessions.remove(id)

    fun getById(id: String) = sessions[id]

    fun getAll() = HashMap(sessions)
}