package polybius.executor.service

import org.w3c.dom.WebSocket
import polybius.executor.Stomp

class WebSocketService(url: String = "ws://localhost:8080/ws/channel") {
    private val client = Stomp.client(WebSocket(url))

    val isConnected
        get() = client.connected

    init {
        client.connect({}, this::onConnected, this::onError)
    }

    private fun onConnected() {
        console.log("Connected!")
    }

    private fun onError(vararg args: Any?) {
        console.error("Connection error:", *args)
    }
}