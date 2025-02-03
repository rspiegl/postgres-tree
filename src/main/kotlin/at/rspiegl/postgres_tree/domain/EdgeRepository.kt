package at.rspiegl.postgres_tree.domain

import at.rspiegl.postgres_tree.exception.UnknownException
import at.rspiegl.postgres_tree.generated.Tables.EDGE
import at.rspiegl.postgres_tree.model.EdgeDTO
import org.jooq.DSLContext
import org.springframework.stereotype.Service


@Service
class EdgeRepository(
    private val dslContext: DSLContext
) {

    fun findAll(froms: Set<Int>): List<EdgeDTO> {
        return dslContext.selectFrom(EDGE)
            .where(EDGE.FROMID.`in`(froms))
            .orderBy(EDGE.FROMID)
            .fetch(EdgeDTO::of)
    }

    fun delete(edge: EdgeDTO) {
        try {
            dslContext.deleteFrom(EDGE)
                .where(EDGE.FROMID.eq(edge.fromId).and(EDGE.TOID.eq(edge.toId)))
                .execute()
        } catch (e: RuntimeException) {
            throw UnknownException("Exception encountered during deletion of $edge", e)
        }
    }

    fun create(edge: EdgeDTO): EdgeDTO {
        try {
            return dslContext.insertInto(EDGE, EDGE.FROMID, EDGE.TOID)
                .values(edge.fromId, edge.toId)
                .returning()
                .fetchOne(EdgeDTO::of)!!
        } catch (e: RuntimeException) {
            throw UnknownException("Exception encountered during creation of $edge", e)
        }
    }

    fun createBatch(edges: Set<EdgeDTO>) {
        if (edges.isEmpty()) return

        val batch = dslContext.batch(dslContext.insertInto(EDGE, EDGE.FROMID, EDGE.TOID).values(0, 0))
        var batchBindStep = batch.bind(edges.first().fromId, edges.first().toId)
        edges.drop(1).forEach {
            batchBindStep = batchBindStep.bind(it.fromId, it.toId)
        }
        try {
            batchBindStep.execute()
        } catch (e: RuntimeException) {
            throw UnknownException("Exception encountered during creation of ${edges.size} edges", e)
        }
    }

    fun `get`(edge: EdgeDTO): EdgeDTO? {
        return dslContext.selectFrom(EDGE)
            .where(EDGE.FROMID.eq(edge.fromId).and(EDGE.TOID.eq(edge.toId)))
            .fetchOne()?.let { EdgeDTO.of(it) }
    }

    fun deleteAll() {
        dslContext.deleteFrom(EDGE)
            .execute()
    }
}
