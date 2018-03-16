package com.walmartlabs.ticketservice.Exceptions;

/**
 * Created by lian.shi on 3/10/2018.
 */
public class IncorrectCustomerEmailException extends RuntimeException {
    public IncorrectCustomerEmailException() {
        super();
    }

    /**
     * a detailed Exception message.
     * @param msg - the detail message.
     */
    public IncorrectCustomerEmailException(String msg) {
        super(msg);
    }
}
