package at.rspiegl.postgres_tree.service

import at.rspiegl.postgres_tree.domain.EdgeRepository
import at.rspiegl.postgres_tree.exception.EdgeAlreadyExistsException
import at.rspiegl.postgres_tree.exception.NotFoundException
import at.rspiegl.postgres_tree.model.EdgeDTO
import org.springframework.stereotype.Service

@Service
class EdgeService(
    private val edgeRepository: EdgeRepository
) {
    fun create(edge: EdgeDTO): EdgeDTO {
        if (edge.exists())
            throw EdgeAlreadyExistsException("$edge already exists")

        return edgeRepository.create(edge)
    }

    fun createBatched(edges: Set<EdgeDTO>) {
        edgeRepository.createBatch(edges)
    }

    fun delete(edge: EdgeDTO) {
        if (!edge.exists())
            throw NotFoundException("$edge doesn't exist")

        edgeRepository.delete(edge)
    }

    private fun EdgeDTO.exists() =
        edgeRepository.get(this) != null
}
