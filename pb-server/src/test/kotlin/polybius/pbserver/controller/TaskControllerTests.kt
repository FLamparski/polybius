package polybius.pbserver.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForObject
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient
import polybius.common.models.*
import polybius.pbserver.testStompSessionHandler
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
class TaskControllerTests(
        @Autowired private val restTemplate: TestRestTemplate,
        @LocalServerPort private val port: String
) {

    @Test
    fun `Task list is empty`() {
        val tasks = restTemplate.getForObject<List<Task>>("/tasks/")
        assert(tasks!!.isEmpty())
    }

    @Test
    fun `Can add a task`() {
        val task = Task(
                id = null,
                order = 1,
                type = TaskType.YOUTUBE,
                url = "https://youtube.com/watch?v=dQw4w9WgXcQ",
                submitter = User(
                        username = "test",
                        deviceName = "test"
                ),
                submittedOn = null,
                state = null
        )

        val res = restTemplate.postForEntity<Task>("/tasks/", task)
        assert(res.statusCode == HttpStatus.OK)
        assert(res.hasBody())

        val savedTask = res.body
        savedTask!!
        savedTask.id!!
    }

    @Test
    fun `Has tasks now`() {
        val tasks = restTemplate.getForObject<List<Task>>("/tasks/")
        assert(tasks!!.size == 1)
    }

    @Test
    fun `Next task is available`() {
        val task = restTemplate.getForObject<Task>("/tasks/next")
        task!!
    }

    @Test
    fun `Adding a task triggers a websocket message`() {
        val latch = CountDownLatch(1)
        val failure = AtomicReference<Throwable>()
        val sockTask = AtomicReference<Task>()

        val client = WebSocketStompClient(StandardWebSocketClient())
        val handler = testStompSessionHandler {
            onConnect { session, _ ->
                session!!.subscribe("/topic/tasks", this)
            }

            onGetPayload { ByteArray::class.java }

            onFrame { _, payload ->
                try {
                    val mapper = ObjectMapper()
                    mapper.registerModule(KotlinModule())
                    val msg = mapper.readValue<Message>(payload as ByteArray)
                    Assertions.assertEquals(MessageType.SERVER_TASK_ADDED, msg.messageType)
                    sockTask.set(msg.task)
                }
                catch (t: Throwable) {
                    failure.set(t)
                }
                finally {
                    latch.countDown()
                }
            }
        }
        client.connect("ws://localhost:$port/ws/channel", handler)

        val task = Task(
                id = null,
                order = 1,
                type = TaskType.YOUTUBE,
                url = "https://youtube.com/watch?v=dQw4w9WgXcQ",
                submitter = User(
                        username = "test",
                        deviceName = "test"
                ),
                submittedOn = null,
                state = null
        )

        val res = restTemplate.postForEntity<Task>("/tasks/", task)
        assert(res.statusCode == HttpStatus.OK)
        assert(res.hasBody())
        assert(res.body == sockTask.get())

        if (!latch.await(5, TimeUnit.SECONDS)) {
            Assertions.fail<Unit>("Timed out")
        }

        if (failure.get() != null) {
            throw AssertionError(failure.get().message, failure.get())
        }
    }

}