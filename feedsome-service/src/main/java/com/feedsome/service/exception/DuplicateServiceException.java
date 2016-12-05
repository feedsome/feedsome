package com.feedsome.service.exception;

public class DuplicateServiceException extends ServiceException {

    public DuplicateServiceException() {
        super(ErrorCode.DUPLICATE, "The provided target already exists on the system");
    }
}
