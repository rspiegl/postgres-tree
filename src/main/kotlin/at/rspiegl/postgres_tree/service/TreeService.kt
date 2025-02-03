package at.rspiegl.postgres_tree.service

import at.rspiegl.postgres_tree.domain.EdgeRepository
import at.rspiegl.postgres_tree.model.EdgeDTO
import at.rspiegl.postgres_tree.model.TreeDTO
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.ServletOutputStream
import org.springframework.stereotype.Service
import kotlin.random.Random
import kotlin.random.nextInt

@Service
class TreeService(
    private val edgeService: EdgeService,
    private val edgeRepository: EdgeRepository,
    private val jacksonMapper: ObjectMapper
) {
    fun findTree(fromId: Int): TreeDTO {
        val nodesToCheck = mutableSetOf(fromId)
        val edges = mutableSetOf<EdgeDTO>()
        while (nodesToCheck.isNotEmpty()) {
            val edgesFound = edgeRepository.findAll(nodesToCheck)
            edges.addAll(edgesFound)
            nodesToCheck.clear()
            nodesToCheck.addAll(edgesFound.map { it.toId })
        }
        return TreeDTO(edges)
    }

    fun findTreeStreaming(fromId: Int, outputStream: ServletOutputStream) {
        val writer = outputStream.writer()
        writer.write("[")
        var nodesToCheck = setOf(fromId)
        val edges = mutableSetOf<Int>()
        var contentWasWritten = false
        while (nodesToCheck.isNotEmpty()) {
            nodesToCheck.chunked(2000).forEach { chunk ->
                val edgesFound = edgeRepository.findAll(chunk.toSet())
                if (contentWasWritten && edgesFound.isNotEmpty()) writer.write(",")
                writer.write(edgesFound.joinToString(separator = ",") { jacksonMapper.writeValueAsString(it) })
                contentWasWritten = true
                writer.flush()
                edges.addAll(edgesFound.map { it.toId })
            }
            nodesToCheck = edges.toSet()
            edges.clear()
        }
        writer.write("]")
        writer.flush()
        writer.close()
    }

    fun generateRandom(nodes: Int) {
        var index = 1
        val edges = mutableSetOf<EdgeDTO>()
        repeat(Random.nextInt(1..4)) {
            edges.add(EdgeDTO(0, index++))
        }
        val nodesToAppend = ArrayDeque(edges.map { it.toId })
        while (edges.size < nodes) {
            val node = nodesToAppend.removeFirst()
            repeat(Random.nextInt(1..4)) {
                val childNode = index++
                edges.add(EdgeDTO(node, childNode))
                nodesToAppend.addLast(childNode)
            }
        }
        edgeService.createBatched(edges)
    }
}
