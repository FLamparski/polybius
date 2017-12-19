package polybius.pbserver

import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaders
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.messaging.simp.stomp.StompSessionHandler
import java.lang.reflect.Type

typealias HandleExceptionHandler = (session: StompSession?, command: StompCommand?, headers: StompHeaders?, payload: ByteArray?, exception: Throwable?) -> Unit
typealias HandleTransportHandler = (session: StompSession?, exception: Throwable?) -> Unit
typealias HandleFrameHandler = (headers: StompHeaders?, payload: Any?) -> Unit
typealias AfterConnectedHandler = (session: StompSession?, connectedHeaders: StompHeaders?) -> Unit
typealias GetPayloadTypeHandler = (headers: StompHeaders?) -> Type

class TestStompSessionHandler : StompSessionHandler {
    private var onExceptionHandler: HandleExceptionHandler? = null
    private var onTransportErrorHandler: HandleTransportHandler? = null
    private var onFrameHandler: HandleFrameHandler? = null
    private var onConnectHandler: AfterConnectedHandler? = null
    private var onGetPayloadHandler: GetPayloadTypeHandler? = null

    override fun handleException(session: StompSession?, command: StompCommand?, headers: StompHeaders?, payload: ByteArray?, exception: Throwable?) {
        onExceptionHandler?.invoke(session, command, headers, payload, exception)
    }

    override fun handleTransportError(session: StompSession?, exception: Throwable?) {
        onTransportErrorHandler?.invoke(session, exception)
    }

    override fun handleFrame(headers: StompHeaders?, payload: Any?) {
        onFrameHandler?.invoke(headers, payload)
    }

    override fun afterConnected(session: StompSession?, connectedHeaders: StompHeaders?) {
        onConnectHandler?.invoke(session, connectedHeaders)
    }

    override fun getPayloadType(headers: StompHeaders?): Type? {
        return onGetPayloadHandler?.invoke(headers)
    }

    fun onException(handler: HandleExceptionHandler) {
        onExceptionHandler = handler
    }

    fun onTransportError(handler: HandleTransportHandler) {
        onTransportErrorHandler = handler
    }

    fun onFrame(handler: HandleFrameHandler) {
        onFrameHandler = handler
    }

    fun onConnect(handler: AfterConnectedHandler) {
        onConnectHandler = handler
    }

    fun onGetPayload(handler: GetPayloadTypeHandler) {
        onGetPayloadHandler = handler
    }
}

fun testStompSessionHandler(build: TestStompSessionHandler.() -> Unit) : TestStompSessionHandler {
    val handler = TestStompSessionHandler()
    handler.build()
    return handler
}