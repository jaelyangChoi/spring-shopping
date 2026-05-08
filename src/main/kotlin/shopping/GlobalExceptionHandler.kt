package shopping

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(e: MethodArgumentNotValidException): ResponseEntity<String> {
        val message = e.bindingResult.fieldErrors
            .joinToString(", ") { it.defaultMessage ?: "유효하지 않은 값입니다." }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message)
    }
}
