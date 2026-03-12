package com.controleazulpessoal.finance_api.exception;

import com.controleazulpessoal.finance_api.exception.category.CategoryAlreadyExistsException;
import com.controleazulpessoal.finance_api.exception.category.CategoryNotFoundException;
import com.controleazulpessoal.finance_api.exception.transaction.TransactionAccessDeniedException;
import com.controleazulpessoal.finance_api.exception.transaction.TransactionNotFoundException;
import com.controleazulpessoal.finance_api.exception.user.UserAlreadyExistsException;
import com.controleazulpessoal.finance_api.exception.user.UserNotAuthenticatedException;
import com.controleazulpessoal.finance_api.exception.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Usuário já existe (409 - Conflict)
    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Map<String, Object>> handleUsuarioAlreadyExistsException(UserAlreadyExistsException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, "user-already-exists", ex);
    }

    // Usuário não encontrado (404 - Not Found)
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, Object>> handleUsuarioNotFoundException(UserNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "user-not-found", ex);
    }

    // Categoria já existe
    @ExceptionHandler(CategoryAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Map<String, Object>> handleCategoryAlreadyExistsException(CategoryAlreadyExistsException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, "category-already-exists", ex);
    }

    // Categoria não encontrada
    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, Object>> handleCategoryNotFoundException(CategoryNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "category-not-found", ex);
    }

    // Transação não encontrada
    @ExceptionHandler(TransactionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, Object>> handleTransactionNotFoundException(TransactionNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "transaction-not-found", ex);
    }

    // Transação negada
    @ExceptionHandler(TransactionAccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Map<String, Object>> handleTransactionAccessDeniedException(TransactionAccessDeniedException ex) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, "transaction-access-denied", ex);
    }

    // Requisição inválida (400 - Bad Request) para erros de argumento ilegal
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "invalid-request", ex);
    }

    // Captura qualquer erro inesperado (500 - Internal Server Error)
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "internal-server-error", ex);
    }

    // Falha na autenticação (401 - Unauthorized)
    @ExceptionHandler(UserNotAuthenticatedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Map<String, Object>> handleUsuarioNaoAutenticadoException(UserNotAuthenticatedException ex) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "authentication-failed", ex);
    }

    // Falha de excpetion generalizada
    @ExceptionHandler(ForbiddenActionException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Map<String, Object>> handleForbiddenActionException(ForbiddenActionException ex) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, "forbidden-action", ex);
    }

    // Método auxiliar para criar respostas padronizadas
    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String error, Exception ex) {
        Map<String, Object> response = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", status.value(),
                "error", error,
                "message", ex.getMessage(),
                "errorCode", (ex instanceof HasErrorCode) ? ((HasErrorCode) ex).getErrorCode() : "UNDEFINED_ERROR"
        );
        return ResponseEntity.status(status).body(response);
    }

    // Adicione este método dentro do seu GlobalExceptionHandler
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(
                        java.util.stream.Collectors.toMap(
                                FieldError::getField,
                                FieldError::getDefaultMessage,
                                (msg1, msg2) -> msg1
                        )
                );

        Map<String, Object> response = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", "Erro de validação",
                "message", "Campos inválidos na requisição",
                "errorCode", "VALIDATION_ERROR",
                "errors", fieldErrors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}
