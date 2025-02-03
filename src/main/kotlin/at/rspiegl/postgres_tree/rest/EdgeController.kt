package at.rspiegl.postgres_tree.rest

import at.rspiegl.postgres_tree.domain.EdgeRepository
import at.rspiegl.postgres_tree.model.EdgeDTO
import at.rspiegl.postgres_tree.service.EdgeService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping(
    value = ["/api/edges"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
class EdgeController(
    private val edgeService: EdgeService,
    private val edgeRepository: EdgeRepository
) {

    @PostMapping
    fun createEdge(@RequestBody @Valid edgeDTO: EdgeDTO): ResponseEntity<EdgeDTO> {
        val createdEdge = edgeService.create(edgeDTO)
        return ResponseEntity(createdEdge, HttpStatus.CREATED)
    }

    @DeleteMapping
    fun deleteEdge(@RequestBody @Valid edgeDTO: EdgeDTO): ResponseEntity<Void> {
        edgeService.delete(edgeDTO)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/all")
    fun deleteAll() {
        edgeRepository.deleteAll()
    }
}
