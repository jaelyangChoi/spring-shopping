package shopping

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.mockito.Mockito.anyString
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestConstructor
import org.springframework.web.client.RestClient


class ProductValidateTest {

    private val mockRestClient: RestClient = mock(RestClient::class.java)
    private val mockBuilder: RestClient.Builder = mock(RestClient.Builder::class.java).also {
        given(it.build()).willReturn(mockRestClient)
    }
    val validator: ProductValidator = ProductValidator(mockBuilder)

    @ParameterizedTest
    @CsvSource(value = [
        "아이스아메리카노,true",
        "'',false",
        "123456789012345,true",
        "1234567890123456,false",
        "ice am 123,true",
        "ice12345                         ,false"
    ],ignoreLeadingAndTrailingWhitespace = false)
    fun lengthValidation(input: String, expected: Boolean) {
        validator.lengthValidate(input) shouldBe expected
    }

    @ParameterizedTest
    @CsvSource(value = [
        "아이스아메리카노*,false",
        "'()',true",
        "[!!)),false",
        "'( 아이스아메리카노@)',false",
        "아이스라떼샷추가+-,true"
    ])
    fun charValidation(input: String, expected: Boolean) {
        validator.charValidate(input) shouldBe expected
    }

    @ParameterizedTest
    @CsvSource(value = [
        "fuck,false",
        "ice,true"
    ])
    fun slangValidation(input: String, expected: Boolean) {
        // given
        val mockUriSpec = mock(RestClient.RequestHeadersUriSpec::class.java)
        val mockHeadersSpec = mock(RestClient.RequestHeadersSpec::class.java)
        val mockResponseSpec = mock(RestClient.ResponseSpec::class.java)

        given(mockRestClient.get()).willReturn(mockUriSpec)
        given(mockUriSpec.uri(anyString())).willReturn(mockHeadersSpec)
        given(mockHeadersSpec.retrieve()).willReturn(mockResponseSpec)
        given(mockResponseSpec.body(String::class.java)).willReturn((!expected).toString())

        // when & then
        validator.slangValidate(input) shouldBe expected
    }

}