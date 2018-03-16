package com.walmartlabs.ticketservice.model;

import com.sun.org.apache.regexp.internal.RE;
import com.walmartlabs.ticketservice.models.Reservation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by lian.shi on 3/10/2018.
 */
public class ReservationTest {

    @Test
    public void testUniqueReservationId() {
        Reservation r1 = new Reservation(1, "john@example.com");
        Reservation r2 = new Reservation(2, "john@example.com");

        System.out.println(r1.getReservationId());
        System.out.println(r2.getReservationId());
        Assert.assertEquals(r2.getReservationId() - r1.getReservationId(), 1);

    }
}
