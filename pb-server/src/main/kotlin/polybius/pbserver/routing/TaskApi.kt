package polybius.pbserver.routing

import kotlinx.coroutines.experimental.Unconfined
import kotlinx.coroutines.experimental.reactor.flux
import kotlinx.coroutines.experimental.reactor.mono
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import org.springframework.web.reactive.function.server.router
import polybius.common.models.Task
import polybius.pbserver.repo.TaskRepo
import reactor.core.publisher.toFlux
import reactor.core.publisher.toMono
import java.util.stream.Collectors

@RestController
@RequestMapping("/tasks")
class TaskApi(val tasks: TaskRepo) {

    @GetMapping("/")
    fun list() = tasks.getAllOrderByOrder().collect(Collectors.toList())

    @GetMapping("/{id}")
    fun get(@PathVariable id: String): ResponseEntity<Task> {
        val task = tasks.findById(id)
        return if (task != null) {
            ResponseEntity(task, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping("/")
    fun save(@RequestBody task: Task) = tasks.save(task)
}