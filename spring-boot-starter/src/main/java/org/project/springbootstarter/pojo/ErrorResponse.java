package org.project.springbootstarter.pojo;

import lombok.Builder;
import lombok.Data;
import org.project.springbootstarter.exception.Error;

@Data
@Builder
public class ErrorResponse {
    private String errorCode;
    private String errorMessage;


    public ErrorResponse(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ErrorResponse(Error error) {
        this.errorCode = error.getErrorCode();
        this.errorMessage = error.getErrorMessage();
    }
}