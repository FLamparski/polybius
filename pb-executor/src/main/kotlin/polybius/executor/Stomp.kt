package polybius.executor

import org.w3c.dom.WebSocket
import polybius.common.models.PolybiusMessage

@JsName("Stomp")
external object Stomp {
    fun client(url: String, protocols: Array<String>): StompClient = definedExternally
    fun over(ws: WebSocket): StompClient = definedExternally
}

external class SubscriptionHandle {
    val id: String
    fun unsubscribe(): Unit = definedExternally
}

external class Frame {
    val command: String
    val headers: dynamic
    val body: String
    override fun toString(): String = definedExternally
    fun ack(): Unit = definedExternally
    fun nack(): Unit = definedExternally
}

external class StompClient(ws: WebSocket) {
    val connected: Boolean
    fun connect(vararg args: Any?): Unit = definedExternally
    fun disconnect(callback: () -> Unit, headers: Any?): Unit = definedExternally
    fun send(destination: String, headers: Any?, body: Any?): Unit = definedExternally
    @JsName("subscribe")
    fun _subscribe(destination: String, callback: (Frame) -> Unit, headers: Any?): SubscriptionHandle = definedExternally
    fun unsubscribe(id: String): Unit = definedExternally
}

fun StompClient.subscribe(destination: String, headers: Any? = null, callback: (Frame) -> Unit): SubscriptionHandle {
    return this._subscribe(destination, callback, headers)
}

fun Frame.message() = JSON.parse<PolybiusMessage>(this.body)