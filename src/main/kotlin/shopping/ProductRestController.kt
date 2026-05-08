package shopping

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.net.URI

const val PRODUCT_PATH = "/api/products"

@RestController()
class ProductRestController(
    private val productService: ProductService
) {

    @GetMapping(PRODUCT_PATH)
    fun getProducts(): List<ProductResponse> {
        return productService.getProducts()
    }

    @GetMapping("$PRODUCT_PATH/{id}")
    fun getProduct(@PathVariable id: Long): ProductResponse {
        return productService.getProduct(id)
    }

    @PostMapping(PRODUCT_PATH)
    fun addProduct(@RequestBody request: ProductRequest): ResponseEntity<ProductResponse> {
        val response = productService.addProduct(request)
        return ResponseEntity
            .created(URI.create("$PRODUCT_PATH/${response.id}"))
            .body(response)
    }

    @PutMapping("$PRODUCT_PATH/{id}")
    fun updateProduct(@PathVariable id: Long, @RequestBody request: ProductRequest): ProductResponse {
        return productService.updateProduct(id, request)
    }
}