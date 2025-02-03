package at.rspiegl.postgres_tree.service

import at.rspiegl.postgres_tree.domain.EdgeRepository
import at.rspiegl.postgres_tree.exception.EdgeAlreadyExistsException
import at.rspiegl.postgres_tree.exception.NotFoundException
import at.rspiegl.postgres_tree.model.EdgeDTO
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class EdgeServiceTest {

    private val edgeRepository = mockk<EdgeRepository>()

    private val edgeService = EdgeService(edgeRepository)


    @Test
    fun createThrowsIfExists() {
        every { edgeRepository.get(any()) } returnsArgument 0
        val edge = EdgeDTO(1, 2)

        assertThrows(EdgeAlreadyExistsException::class.java) {
            edgeService.create(edge)
        }
    }

    @Test
    fun deleteThrowsIfNotExists() {
        every { edgeRepository.get(any()) } returns null
        val edge = EdgeDTO(1, 2)

        assertThrows(NotFoundException::class.java) {
            edgeService.delete(edge)
        }
    }
}
