package shopping

import io.kotest.assertions.print.print
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
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
import org.springframework.web.client.HttpClientErrorException
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
        val actual = insertProduct("아이스 카페 아메리카노 T")

        actual.statusCode shouldBe HttpStatus.CREATED
        actual.headers.location.toString() shouldContain "/api/products"
    }


    @Test
    fun getProducts() {
        insertProduct("아이스 카페 아메리카노 T")

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
        insertProduct("아이스 카페 아메리카노 T")

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
        val response = insertProduct("아이스 카페 아메리카노 T")
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

    @ParameterizedTest
    @CsvSource(value = [
        "아이스아메리카노,201",
        "아이스아메리카노!,400",
        "아이스fuck,400"
    ])
    fun validatorTest(productName: String, expected: Int) {
        val actual = insertProduct(productName)
        actual.statusCode.value() shouldBe expected
        if (expected == 201) actual.body.shouldNotBeNull()
    }


    private fun insertProduct(productName: String): ResponseEntity<ProductResponse> {
        val request = ProductRequest(
            name = productName,
            price = 4500,
            imageUrl = "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg"
        )

        return try {
            client
                .post()
                .uri(PRODUCT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toEntity<ProductResponse>()
        } catch (e: HttpClientErrorException) {
            ResponseEntity.status(e.statusCode).build()
        }
    }

    @ParameterizedTest
    @CsvSource(value = [
        "fuck,true",
        "ice,false"
    ])
    fun callExtApiTest(text: String, expected: Boolean) {
        val extApiClient = builder.baseUrl("").build()
        val actual = extApiClient
            .get()
            .uri("https://www.purgomalum.com/service/containsprofanity?text=$text")
            .retrieve()
            .body(String::class.java)

        actual.toBoolean() shouldBe expected
    }

}