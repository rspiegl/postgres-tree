package at.rspiegl.postgres_tree.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class EdgeAlreadyExistsException(message: String) : RuntimeException(message)
