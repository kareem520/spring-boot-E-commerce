package com.ecomm.spring_ecomm.handler;

import com.ecomm.spring_ecomm.exception.BusinessException;
import com.ecomm.spring_ecomm.exception.ErrorCode;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class ApplicationExceptionHandler {




    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException ex) {
        ErrorResponse body = ErrorResponse.builder()
                .message(ex.getMessage())
                .code(ex.getErrorCode().getCode())
                .build();

        log.info("Business Exception: {}", body);
        log.debug(ex.getMessage(), ex);

        return ResponseEntity.status((ex.getErrorCode().getStatus()==null?
                        BAD_REQUEST:ex.getErrorCode().getStatus()))
                .body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        final List<ErrorResponse.ValidationErrors> errorsList = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(
                (fieldError) -> {
            final String fieldName = fieldError.getField();
            final String errorMessage = fieldError.getDefaultMessage();
            errorsList.add(ErrorResponse.ValidationErrors.builder()
                    .field(fieldName)
                    .message(errorMessage)
                    .code("INVALID_FIELD")
                    .build());});
        ErrorResponse errorResponse = ErrorResponse.builder()
                .validationErrorsList(errorsList)
                .message("Validation failed for request")
                .code("VALIDATION_ERROR")
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleException(final BadCredentialsException exception) {
        log.debug(exception.getMessage(), exception);
        final ErrorResponse response = ErrorResponse.builder()
                .message(ErrorCode.BAD_CREDENTIALS.getDefaultMessage())
                .code(ErrorCode.BAD_CREDENTIALS.getCode())
                .build();
        return new ResponseEntity<>(response, UNAUTHORIZED);
    }


    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(final UsernameNotFoundException exception) {
        log.debug(exception.getMessage(), exception);
        final ErrorResponse response = ErrorResponse.builder()
                .code(ErrorCode.USERNAME_NOT_FOUND.getCode())
                .message(ErrorCode.USERNAME_NOT_FOUND.getDefaultMessage())
                .build();
        return new ResponseEntity<>(response,
                NOT_FOUND);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleException(final AuthorizationDeniedException exception) {
        log.debug(exception.getMessage(), exception);
        final ErrorResponse response = ErrorResponse.builder()
                .message("You are not authorized to perform this operation")
                .build();
        return new ResponseEntity<>(response, UNAUTHORIZED);
    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleException(final Exception exception) {
//        log.error(exception.getMessage(), exception);
//        final ErrorResponse response = ErrorResponse.builder()
//                .code(ErrorCode.INTERNAL_EXCEPTION.getCode())
//                .message(ErrorCode.INTERNAL_EXCEPTION.getDefaultMessage())
//                .build();
//        return new ResponseEntity<>(response, INTERNAL_SERVER_ERROR);
//    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(final EntityNotFoundException ex) {
        log.debug(ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ErrorCode.ENTITY_NOT_FOUND.getCode())
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(ErrorCode.ENTITY_NOT_FOUND.getStatus())
                .body(errorResponse);
    }
}
