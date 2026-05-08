package shopping

class ProductValidator {
    fun lengthValidate(input: String): Boolean {
        return input.isNotEmpty() && input.length <= 15
    }

    fun charValidate(input: String): Boolean {
        return input.matches(Regex("^[가-힣a-zA-Z0-9 ()\\[\\]+\\-&/_]*$"))
    }
}