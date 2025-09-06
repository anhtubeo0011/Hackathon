package com.knowledgehub.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  // Custom exception response class
  public static class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private Map<String, String> validationErrors;

    public ErrorResponse() {
      this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(int status, String error, String message, String path) {
      this();
      this.status = status;
      this.error = error;
      this.message = message;
      this.path = path;
    }

    // Getters and Setters
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public Map<String, String> getValidationErrors() { return validationErrors; }
    public void setValidationErrors(Map<String, String> validationErrors) { this.validationErrors = validationErrors; }
  }

  // Handle validation errors
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(
      MethodArgumentNotValidException ex, WebRequest request) {

    logger.warn("Validation error: {}", ex.getMessage());

    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        "Validation Failed",
        "Dữ liệu đầu vào không hợp lệ",
        request.getDescription(false).replace("uri=", "")
    );

    Map<String, String> validationErrors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      validationErrors.put(fieldName, errorMessage);
    });
    errorResponse.setValidationErrors(validationErrors);

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  // Handle constraint violation exceptions
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolationException(
      ConstraintViolationException ex, WebRequest request) {

    logger.warn("Constraint violation: {}", ex.getMessage());

    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        "Constraint Violation",
        "Dữ liệu vi phạm ràng buộc: " + ex.getMessage(),
        request.getDescription(false).replace("uri=", "")
    );

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  // Handle entity not found exceptions
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleEntityNotFoundException(
      EntityNotFoundException ex, WebRequest request) {

    logger.warn("Entity not found: {}", ex.getMessage());

    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.NOT_FOUND.value(),
        "Not Found",
        "Không tìm thấy tài nguyên: " + ex.getMessage(),
        request.getDescription(false).replace("uri=", "")
    );

    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  // Handle authentication exceptions
  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ErrorResponse> handleAuthenticationException(
      AuthenticationException ex, WebRequest request) {

    logger.warn("Authentication error: {}", ex.getMessage());

    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.UNAUTHORIZED.value(),
        "Authentication Failed",
        "Xác thực thất bại: " + ex.getMessage(),
        request.getDescription(false).replace("uri=", "")
    );

    return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
  }

  // Handle bad credentials
  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ErrorResponse> handleBadCredentialsException(
      BadCredentialsException ex, WebRequest request) {

    logger.warn("Bad credentials: {}", ex.getMessage());

    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.UNAUTHORIZED.value(),
        "Bad Credentials",
        "Tên đăng nhập hoặc mật khẩu không đúng",
        request.getDescription(false).replace("uri=", "")
    );

    return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
  }

  // Handle access denied exceptions
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAccessDeniedException(
      AccessDeniedException ex, WebRequest request) {

    logger.warn("Access denied: {}", ex.getMessage());

    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.FORBIDDEN.value(),
        "Access Denied",
        "Bạn không có quyền truy cập tài nguyên này",
        request.getDescription(false).replace("uri=", "")
    );

    return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
  }

  // Handle file upload size exceeded
  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(
      MaxUploadSizeExceededException ex, WebRequest request) {

    logger.warn("File size exceeded: {}", ex.getMessage());

    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.PAYLOAD_TOO_LARGE.value(),
        "File Too Large",
        "Kích thước file vượt quá giới hạn cho phép (10MB)",
        request.getDescription(false).replace("uri=", "")
    );

    return new ResponseEntity<>(errorResponse, HttpStatus.PAYLOAD_TOO_LARGE);
  }

  // Handle illegal argument exceptions
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
      IllegalArgumentException ex, WebRequest request) {

    logger.warn("Illegal argument: {}", ex.getMessage());

    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        "Invalid Argument",
        "Tham số không hợp lệ: " + ex.getMessage(),
        request.getDescription(false).replace("uri=", "")
    );

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  // Handle runtime exceptions
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorResponse> handleRuntimeException(
      RuntimeException ex, WebRequest request) {

    logger.error("Runtime exception: {}", ex.getMessage(), ex);

    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        "Runtime Error",
        "Đã xảy ra lỗi trong quá trình xử lý: " + ex.getMessage(),
        request.getDescription(false).replace("uri=", "")
    );

    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  // Handle all other exceptions
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGlobalException(
      Exception ex, WebRequest request) {

    logger.error("Unexpected error: {}", ex.getMessage(), ex);

    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        "Internal Server Error",
        "Đã xảy ra lỗi không mong muốn. Vui lòng thử lại sau.",
        request.getDescription(false).replace("uri=", "")
    );

    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
