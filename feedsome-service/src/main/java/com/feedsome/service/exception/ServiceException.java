package com.feedsome.service.exception;

import lombok.Data;
import lombok.Getter;

/**
 * Represents an {@link Exception} that is thrown by the service layer.
 */
public class ServiceException extends Exception {

    /**
     * The dedicated error code that corresponds to a specific {@link ServiceException}
     */
    @Getter
    private String code;

    /**
     * A more detailed error message of the {@link ServiceException}
     */
    @Getter
    private String developerMessage;

    public ServiceException() {
        this.code = ErrorCode.INTERNAL_SERVER_ERROR.value;
        this.developerMessage = "Internal Server Error";
    }

    public ServiceException(final String code, final String developerMessage) {
        this.code = code;
        this.developerMessage = developerMessage;
    }

}
