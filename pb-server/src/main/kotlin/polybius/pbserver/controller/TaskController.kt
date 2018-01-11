package polybius.pbserver.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import polybius.common.models.Task
import polybius.pbserver.service.TaskService

@RestController
@RequestMapping("/tasks")
class TaskController(val tasks: TaskService) {

    @GetMapping("/")
    fun list() = tasks.getAllOrderByOrder()

    @GetMapping("/next")
    fun next() = tasks.getNext()

    @GetMapping("/{id}")
    fun get(@PathVariable id: String): ResponseEntity<Task> {
        val task = tasks.findById(id)
        return if (task != null) {
            ResponseEntity(task, HttpStatus.OK)
        }
        else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping("/")
    fun save(@RequestBody task: Task) = tasks.save(task)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String): ResponseEntity<Task> {
        val task = tasks.delete(id)
        return if (task != null) {
            ResponseEntity(task, HttpStatus.OK)
        }
        else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }
}