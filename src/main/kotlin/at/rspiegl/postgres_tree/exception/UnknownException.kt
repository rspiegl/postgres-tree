package at.rspiegl.postgres_tree.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus


@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
class UnknownException(message: String, cause: Throwable) : RuntimeException(message, cause)
