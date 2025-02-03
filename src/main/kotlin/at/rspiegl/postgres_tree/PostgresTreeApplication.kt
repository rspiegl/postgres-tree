package at.rspiegl.postgres_tree

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.transaction.annotation.EnableTransactionManagement


@SpringBootApplication
@EnableTransactionManagement
class PostgresTreeApplication

fun main(args: Array<String>) {
    runApplication<PostgresTreeApplication>(*args)
}
