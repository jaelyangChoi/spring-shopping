package shopping

import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestClient
import org.springframework.web.client.toEntity

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL) //JUNIT 환경에서 자동 주입
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductE2ETest(
    @LocalServerPort private val port: Int,
    private val builder: RestClient.Builder
) {
//    @MockitoBean
//    private lateinit var productService: ProductService

    private lateinit var client: RestClient

    @BeforeEach
    fun setUp() {
        client = builder.baseUrl("http://localhost:$port").build()
    }

    @Test
    fun get_null() {
        // when
        val actual = client
            .get()
            .uri(PRODUCT_PATH)
            .retrieve()
            .toEntity<List<ProductResponse>>()

        // then
        actual.statusCode shouldBe HttpStatus.OK
        val body = actual.body
        body.shouldNotBeNull()
        body.shouldHaveSize(0)
    }

    @Test
    fun insert() {
        val actual = insertProduct()

        actual.statusCode shouldBe HttpStatus.CREATED
        actual.headers.location.toString() shouldContain "/api/products"
    }


    @Test
    fun getProducts() {
        insertProduct()

        val response = ProductResponse(
            1L,
            "아이스 카페 아메리카노 T",
            4500,
            "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg"
        )

        // when
        val actual = client
            .get()
            .uri(PRODUCT_PATH)
            .retrieve()
            .toEntity<List<ProductResponse>>()

        // then
        actual.statusCode shouldBe HttpStatus.OK
        val body = actual.body
        body.shouldNotBeNull()
        body.shouldHaveSize(1)
        body.shouldContain(response)
    }

    @Test
    fun getProduct() {
        insertProduct()

        val actual = client
            .get()
            .uri("$PRODUCT_PATH/1")
            .retrieve()
            .toEntity<ProductResponse>()

        actual.statusCode shouldBe HttpStatus.OK
        val body = actual.body
        body.shouldNotBeNull()
        body.id shouldBe 1L


    }

    @Test
    fun update() {
        // given
        val response = insertProduct()
        val body = response.body
        body.shouldNotBeNull()
        val productId = body.id

        val request = ProductRequest(
            name = "아이스 카페 아메리카노 T",
            price = 5000,
            imageUrl = "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg"
        )

        val actual = client
            .put()
            .uri("$PRODUCT_PATH/$productId")
            .body(request)
            .retrieve()
            .toEntity<ProductResponse>()

        val body2 = actual.body
        body2.shouldNotBeNull()
        body2.id shouldBe productId
        body2.price shouldBe request.price

    }


    private fun insertProduct(): ResponseEntity<ProductResponse> {
        val request = ProductRequest(
            name = "아이스 카페 아메리카노 T",
            price = 4500,
            imageUrl = "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg"
        )

        return client
            .post()
            .uri(PRODUCT_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .retrieve()
            .toEntity<ProductResponse>()
    }
}