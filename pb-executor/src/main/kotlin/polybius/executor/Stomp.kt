package polybius.executor

import org.w3c.dom.WebSocket

@JsName("Stomp")
external object Stomp {
    fun client(ws: WebSocket): StompClient = definedExternally
}

external class SubscriptionHandle {
    val id: String
    fun unsubscribe(): Unit = definedExternally
}

external class Frame {
    override fun toString(): String = definedExternally
    fun ack(): Unit = definedExternally
    fun nack(): Unit = definedExternally
}

external class StompClient(ws: WebSocket) {
    val connected: Boolean
    fun connect(vararg args: Any?): Unit = definedExternally
    fun disconnect(callback: () -> Unit, headers: Any?): Unit = definedExternally
    fun send(destination: String, headers: Any?, body: Any?): Unit = definedExternally
    fun subscribe(destination: String, callback: (Frame) -> Unit, headers: Any?): SubscriptionHandle = definedExternally
    fun unsubscribe(id: String): Unit = definedExternally
}