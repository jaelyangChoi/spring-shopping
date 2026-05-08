package shopping

data class ProductRequest(
    @field:ValidProductName
    val name: String,
    val price: Int,
    val imageUrl: String
)

data class ProductResponse(
    val id: Long,
    val name: String,
    val price: Int,
    val imageUrl: String,
){
    constructor(product: Product) : this(
        id = product.id,
        name = product.name,
        price = product.price,
        imageUrl = product.imageUrl
    )
}