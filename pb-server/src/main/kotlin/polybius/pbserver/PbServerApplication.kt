package polybius.pbserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PbServerApplication

fun main(args: Array<String>) {
    runApplication<PbServerApplication>(*args)
}
