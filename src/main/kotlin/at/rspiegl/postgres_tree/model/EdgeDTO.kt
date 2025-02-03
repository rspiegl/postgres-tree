package at.rspiegl.postgres_tree.model

import at.rspiegl.postgres_tree.generated.tables.records.EdgeRecord

data class EdgeDTO(val fromId: Int, val toId: Int) {

    override fun toString(): String {
        return "Edge from $fromId to $toId"
    }

    // Override hashcode to prevent false hashCode matches when working with Sets
    // the generation code makes it so fromId is smaller than toId, so I'm giving more weight to toId
    override fun hashCode(): Int {
        return (17 * 31 + toId) * 31 + fromId
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EdgeDTO

        if (fromId != other.fromId) return false
        if (toId != other.toId) return false

        return true
    }

    companion object {
        fun of(edgeRecord: EdgeRecord): EdgeDTO = EdgeDTO(edgeRecord.fromid, edgeRecord.toid)
    }
}
