package shopping

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class ProductValidator(restClientBuilder: RestClient.Builder) : ConstraintValidator<ValidProductName, String> {
    private val restClient: RestClient = restClientBuilder.build()

    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        if (value == null) return false
        return lengthValidate(value) && charValidate(value) && slangValidate(value)
    }

    fun lengthValidate(input: String): Boolean {
        return input.isNotEmpty() && input.length <= 15
    }

    fun charValidate(input: String): Boolean {
        return input.matches(Regex("^[가-힣a-zA-Z0-9 ()\\[\\]+\\-&/_]*$"))
    }

    fun slangValidate(input: String): Boolean {
        val result = restClient
            .get()
            .uri("https://www.purgomalum.com/service/containsprofanity?text=$input")
            .retrieve()
            .body(String::class.java)
        return result?.toBoolean()?.not() ?: true
    }
}