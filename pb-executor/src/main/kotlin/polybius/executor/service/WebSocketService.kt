package polybius.executor.service

import org.w3c.dom.WebSocket
import polybius.executor.Stomp
import polybius.executor.message
import polybius.executor.subscribe

class WebSocketService(url: String = "ws://localhost:8080/ws/channel") {
    private val client = Stomp.over(WebSocket(url))

    val isConnected
        get() = client.connected

    init {
        client.connect({}, this::onConnected, this::onError)
    }

    private fun onConnected() {
        client.subscribe("/topic/tasks") { frame ->
            frame.ack()
            val msg = frame.message()
            console.log(msg)
        }
    }

    private fun onError(vararg args: Any?) {
        console.error("Connection error:", *args)
    }
}