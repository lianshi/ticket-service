package com.walmartlabs.ticketservice.model;

import com.walmartlabs.ticketservice.models.Reservation;
import com.walmartlabs.ticketservice.models.SeatHold;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;

/**
 * Created by lian.shi on 3/10/2018.
 */
public class SeatHoldTest {

    @Test
    public void testUniqueReservationId() {
        SeatHold sh1 = new SeatHold(1, LocalDateTime.now().plusMinutes(2) , "john@example.com");
        SeatHold sh2 = new SeatHold(2, LocalDateTime.now().plusMinutes(3) , "john@example.com");
        Assert.assertEquals(sh2.getSeatHoldId() - sh1.getSeatHoldId(), 1);

    }}
