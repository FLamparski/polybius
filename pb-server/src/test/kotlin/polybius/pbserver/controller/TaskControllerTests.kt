package polybius.pbserver.controller

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.getForObject
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient
import polybius.common.models.Task
import polybius.common.models.TaskType
import polybius.common.models.User


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
class TaskControllerTests(
        @Autowired private val restTemplate: TestRestTemplate,
        @LocalServerPort private val port: String
) {
    private var wsClient: WebSocketStompClient? = null

    @BeforeEach
    fun setupClient() {
        wsClient = WebSocketStompClient(StandardWebSocketClient())
    }

    @AfterEach
    fun teardown() {
        wsClient?.stop()
    }

    @Test
    fun `Task list is empty`() {
        val tasks = restTemplate.getForObject<List<Task>>("/tasks/")
        assert(tasks!!.isEmpty())
    }

    @Test
    fun `Can add a task`() {
        val task = createTestTask()

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
    fun `Can remove a task`() {
        val task = restTemplate.postForEntity<Task>("/tasks/", createTestTask()).body
        val id = task.id

        restTemplate.delete("/tasks/$id")
        val res = restTemplate.getForEntity<Task>("/tasks/$id")
        assert(res.statusCode === HttpStatus.NOT_FOUND)
    }

    fun createTestTask() = Task(
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
}