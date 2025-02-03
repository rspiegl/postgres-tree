package at.rspiegl.postgres_tree.rest

import at.rspiegl.postgres_tree.model.TreeDTO
import at.rspiegl.postgres_tree.service.TreeService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping(
    value = ["/api/trees"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
class TreeController(
    private val treeService: TreeService
) {

    @GetMapping("/{id}/non-optimal")
    fun getTreeNonOptimal(@PathVariable id: Int): ResponseEntity<TreeDTO> {
        val tree = treeService.findTree(id)
        return ResponseEntity(tree, HttpStatus.OK)
    }

    @GetMapping("/{id}/optimal")
    fun getTreeOptimal(@PathVariable id: Int, response: HttpServletResponse) {
        response.setHeader(HttpHeaders.CONTENT_ENCODING, MediaType.APPLICATION_JSON_VALUE)
        treeService.findTreeStreaming(id, response.outputStream)
    }

    @PostMapping("/generate")
    fun generateTree(@RequestParam nodes: Int) {
        treeService.generateRandom(nodes)
    }
}
