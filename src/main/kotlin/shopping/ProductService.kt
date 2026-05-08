package shopping

import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicLong
import kotlin.collections.map

class Product(
    val id: Long,
    val name: String,
    val price: Int,
    val imageUrl: String,
)

@Service
class ProductService {
    private val ids: AtomicLong = AtomicLong(0)
    private val products: MutableMap<Long, Product> = mutableMapOf()

    fun getProducts(): List<ProductResponse> {
        return products.values
            .map { ProductResponse(it) }
    }

    fun getProduct(id: Long): ProductResponse {
        val product = products[id] ?: throw NoSuchElementException("Product not found with id: $id")
        return ProductResponse(product)
    }

    fun addProduct(request: ProductRequest): ProductResponse {
        val id = ids.incrementAndGet()
        val product = Product(id, request.name, request.price, request.imageUrl)
        products[id] = product
        return ProductResponse(product)
    }

    fun updateProduct(id: Long, request: ProductRequest): ProductResponse {
        val product = products[id] ?: throw NoSuchElementException("Product not found with id: $id")
        val updatedProduct = Product(product.id, request.name, request.price, request.imageUrl)
        products[id] = updatedProduct
        return ProductResponse(updatedProduct)
    }
}