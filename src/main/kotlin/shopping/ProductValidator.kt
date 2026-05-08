package shopping

class ProductValidator {
    fun lengthValidate(input: String): Boolean {
        return input.isNotEmpty() && input.length <= 15
    }
}