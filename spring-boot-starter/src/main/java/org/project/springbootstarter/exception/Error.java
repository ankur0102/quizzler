package org.project.springbootstarter.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public enum Error {
    LATE_SUBMISSION_ERROR("403.LATE_SUBMISSION_ERROR", "LATE_SUBMISSION_ERROR", HttpStatus.FORBIDDEN),
    PLAYER_NOT_FOUND("404.PLAYER_NOT_FOUND", "PLAYER_NOT_FOUND", HttpStatus.NOT_FOUND),
    QUESTION_NOT_FOUND("404.QUESTION_NOT_FOUND", "QUESTION_NOT_FOUND", HttpStatus.NOT_FOUND),
    SESSION_NOT_FOUND("404.SESSION_NOT_FOUND", "SESSION_NOT_FOUND", HttpStatus.NOT_FOUND),
    QUIZ_NOT_FOUND("404.PLAYER_NOT_FOUND", "PLAYER_NOT_FOUND", HttpStatus.NOT_FOUND),
    INTERNAL_SERVER_ERROR("500.INTERNAL_SERVER_ERROR", "INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR),
    SUBMISSION_ALREADY_DONE("403.SUBMISSION_ALREADY_DONE", "SUBMISSION_ALREADY_DONE", HttpStatus.FORBIDDEN);

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;

    Error(String errorCode, String errorMessage, HttpStatus httpStatus) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }
}
