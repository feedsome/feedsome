package com.feedsome.service.exception;

import lombok.Getter;

/**
 * Error codes for {@link ServiceException}
 */
public enum ErrorCode {

    /**
     * An error occurred, that in normal circumstances should
     * not have been produced by the system.
     */
    INTERNAL_SERVER_ERROR("500", "internalServerError"),

    /**
     * The requested target was not found on the system
     */
    NOT_FOUND("404", "notFound"),

    /**
     * The target already exists in the system
     */
    DUPLICATE("409", "duplicate");


    public final String value;

    public final String friendlyValue;

    ErrorCode(final String value, final String friendlyValue) {
        this.friendlyValue = friendlyValue;
        this.value = value;
    }




}
