package com.feedsome.service.exception;

public class NotFoundServiceException extends ServiceException {


    public NotFoundServiceException() {
        super(ErrorCode.NOT_FOUND, "Requested target does not exist in the system");
    }
}
