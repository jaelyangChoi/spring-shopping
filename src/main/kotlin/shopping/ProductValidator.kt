package shopping

import org.springframework.web.client.RestClient

class ProductValidator(private val restClient: RestClient = RestClient.create()) {
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