package polybius.android.service

import android.app.Service
import android.content.Intent
import android.os.Process
import android.support.v4.app.NotificationCompat
import android.util.Log
import okhttp3.*
import org.koin.android.ext.android.inject
import polybius.android.fake.FakeDataGenerator
import polybius.android.kextensions.tag
import polybius.android.repo.InMemoryStateRepository

/**
 * Created by filip on 14/05/18.
 */
class PolybiusService : Service() {
    private val httpClient : OkHttpClient by inject()
    private val stateRepository : InMemoryStateRepository by inject()
    private val fakeDataGenerator : FakeDataGenerator by inject()

    private var webSocket : WebSocket? = null
    private var fakeDataThread : Thread? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(tag(this), "onStartCommand (action = ${intent?.action}, flags = $flags, startId = $startId)")
        handleIntent(intent)
        return START_STICKY
    }

    override fun onBind(intent: Intent?) = null

    fun handleIntent(intent: Intent?) {
        if (intent == null) {
            return
        }

        Log.i(tag(this), "onHandleIntent (action = ${intent.action})")

        when (intent.action) {
            COMMAND_START -> {
                startForeground(SERVICE_ID, notification())
                stateRepository.isConnected.postValue(true)
                webSocket = startWebsocket()
                fakeDataThread = startRandomData()
            }
            COMMAND_STOP -> {
                stopForeground(true)
                stopSelf()
            }
            else -> {
                Log.w(tag(this), "Unknown action: ${intent.action}")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(tag(this), "onDestroy")
        stateRepository.isConnected.postValue(false)
        webSocket?.close(1000, "Client disconnected")
        fakeDataThread?.interrupt()
    }

    private fun notification() = NotificationCompat.Builder(this, CHANNEL_ID)
            .setOngoing(true)
            .setContentTitle("Polybius")
            .setContentText("Ongoing notification")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

    private fun startWebsocket() = httpClient.newWebSocket(
            Request.Builder().url("ws://demos.kaazing.com/echo").build(),
            object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    webSocket.send("Hello")
                }

                override fun onMessage(webSocket: WebSocket, string: String) {
                    Log.i(tag(this), "Websocket message: $string")
                }
            })

    private fun startRandomData(): Thread {
        val thread = Thread {
            var running = true
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND)
            while (running && !Thread.interrupted()) {
                fakeDataGenerator.tick(1000)
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    running = false
                }
            }
        }
        thread.start()
        return thread
    }

    companion object {
        private const val SERVICE_ID = 40875290
        private const val CHANNEL_ID = "CHANNEL_DEFAULT_IMPORTANCE"

        const val COMMAND_START = "START"
        const val COMMAND_STOP = "STOP"
    }
}