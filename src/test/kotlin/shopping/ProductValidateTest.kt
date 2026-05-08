package shopping

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestConstructor


class ProductValidateTest {

    val validator: ProductValidator = ProductValidator()

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

    @Test
    fun charValidation() {

    }

    @Test
    fun slangValidation() {

    }

}