package org.project.springbootstarter.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatusCode;

@Data
@Builder
public class ServiceException extends RuntimeException {
    private String errorCode;
    private String errorMessage;
    private HttpStatusCode httpStatusCode;

    public ServiceException(String errorCode, String errorMessage, HttpStatusCode httpStatusCode) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.httpStatusCode = httpStatusCode;
    }

    public ServiceException(Error error) {
        this.errorCode = error.getErrorCode();
        this.errorMessage = error.getErrorMessage();
        this.httpStatusCode = error.getHttpStatus();
    }
}
