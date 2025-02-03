package at.rspiegl.postgres_tree.domain

import at.rspiegl.postgres_tree.generated.Tables.EDGE
import at.rspiegl.postgres_tree.model.EdgeDTO
import org.assertj.core.api.Assertions.assertThat
import org.jooq.DSLContext
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Import
import org.springframework.test.context.jdbc.Sql
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@JooqTest
@Import(EdgeRepository::class)
@Testcontainers
@Sql("classpath:/init.sql")
class EdgeRepositoryTest {
    @Autowired
    lateinit var edgeRepository: EdgeRepository

    @Autowired
    lateinit var dslContext: DSLContext

    @Test
    fun testCreate() {
        val edge = EdgeDTO(0, 1)

        val saved = edgeRepository.create(edge)

        val retrieved = dslContext.selectFrom(EDGE)
            .where(EDGE.FROMID.eq(edge.fromId).and(EDGE.TOID.eq(edge.toId)))
            .fetchOne()!!.let { EdgeDTO.of(it) }
        assertThat(saved).isEqualTo(retrieved)
    }

    @Test
    fun testGet() {
        val edge = EdgeDTO(10, 11)
        edgeRepository.create(edge)

        val fetched = edgeRepository.get(edge)

        assertThat(fetched!!).isEqualTo(edge)
    }

    @Test
    fun testDelete() {
        val edge = EdgeDTO(11, 12)
        edgeRepository.create(edge)

        edgeRepository.delete(edge)

        val shouldNotExist = edgeRepository.get(edge)
        assertThat(shouldNotExist).isNull()
    }

    @Test
    fun testDeleteAll() {
        val edges = listOf(EdgeDTO(1, 2), EdgeDTO(2, 3), EdgeDTO(3, 4))
        edges.forEach { edgeRepository.create(it) }

        edgeRepository.deleteAll()

        edges.forEach {
            assertThat(edgeRepository.get(it)).isNull()
        }
    }

    @Test
    fun testFindAll() {
        val edges = listOf(EdgeDTO(1, 2), EdgeDTO(2, 3), EdgeDTO(3, 4))
        edges.forEach { edgeRepository.create(it) }

        val retrieved = edgeRepository.findAll(setOf(1, 2))

        val needToExist = edges.take(2)
        assertThat(retrieved).containsExactlyInAnyOrderElementsOf(needToExist)
    }

    companion object {
        @Container
        @ServiceConnection
        val db = PostgreSQLContainer("postgres:17.2")
    }
}
