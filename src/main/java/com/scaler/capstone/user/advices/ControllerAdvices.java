package com.scaler.capstone.user.advices;

import com.scaler.capstone.user.dtos.ExceptionDTO;
import com.scaler.capstone.user.exception.InvalidDataException;
import com.scaler.capstone.user.exception.InvalidPasswordException;
import com.scaler.capstone.user.exception.UserAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvices {

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<ExceptionDTO> handleRuntimeException(RuntimeException ex){
        ExceptionDTO exceptionDTO = new ExceptionDTO(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return new ResponseEntity<>(exceptionDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    ResponseEntity<ExceptionDTO> handleDuplicateRecordsException(UserAlreadyExistException ex){
        return new ResponseEntity<>(new ExceptionDTO(HttpStatus.BAD_REQUEST, ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    ResponseEntity<ExceptionDTO> handleBadCredentialsException(BadCredentialsException ex){
        return new ResponseEntity<>(new ExceptionDTO(HttpStatus.UNAUTHORIZED, ex.getMessage()),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    ResponseEntity<ExceptionDTO> handleUserNameNotFoundException(UsernameNotFoundException ex){
        return new ResponseEntity<>(new ExceptionDTO(HttpStatus.NOT_FOUND, ex.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    ResponseEntity<ExceptionDTO> handleInvalidPasswordException(InvalidPasswordException ex){
        return new ResponseEntity<>(new ExceptionDTO(HttpStatus.BAD_REQUEST, ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDataException.class)
    ResponseEntity<ExceptionDTO> handleInvalidDataException(InvalidDataException ex){
        return new ResponseEntity<>(new ExceptionDTO(HttpStatus.BAD_REQUEST, ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<ExceptionDTO> handleRuntimeException(AccessDeniedException ex){
        ExceptionDTO exceptionDto = new ExceptionDTO(HttpStatus.FORBIDDEN, ex.getMessage());
        return new ResponseEntity<>(exceptionDto, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}