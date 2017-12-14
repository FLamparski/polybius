package polybius.pbserver

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForObject
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension
import polybius.common.models.Task
import polybius.common.models.TaskType
import polybius.common.models.User

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
class PbServerApplicationTests(@Autowired private val restTemplate: TestRestTemplate) {

	@Test
	fun `The context loads`() {
	}

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
}
