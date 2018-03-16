package com.walmartlabs.ticketservice.Exceptions;

import javax.management.RuntimeMBeanException;

/**
 * Created by lian.shi on 3/9/2018.
 */
public class NoSuchSeatHoldException extends RuntimeException {

    public NoSuchSeatHoldException() {
        super();
    }

    /**
     * a detailed Exception message.
     * @param msg - the detail message.
     */
    public NoSuchSeatHoldException(String msg) {
        super(msg);
    }
}
