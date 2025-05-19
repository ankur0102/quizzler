package org.project.springbootstarter.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.project.springbootstarter.controller.QuizController;
import org.project.springbootstarter.controller.SessionController;
import org.project.springbootstarter.pojo.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = { QuizController.class, SessionController.class })
public class GlobalExceptionHandler {

    @ExceptionHandler({ServiceException.class})
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ServiceException ex, HttpServletRequest request) {
        return new ResponseEntity<>(new ErrorResponse(ex.getErrorCode(), ex.getMessage()), ex.getHttpStatusCode());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleException(Exception ex, HttpServletRequest request) {
        return new ResponseEntity<>(new ErrorResponse(Error.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
