package com.walmartlabs.ticketservice.model;

import com.walmartlabs.ticketservice.models.Seat;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.time.LocalDateTime;


/**
 * Created by lian.shi on 3/10/2018.
 */
public class SeatTest {
    private Seat seat;

    @Before
    public void setUp() {
        seat = new Seat(1,2,0);

    }

    @Test
    public void testAfterConstructed() {
        Assert.assertTrue(seat.getRow() == 1);
        Assert.assertTrue(seat.getNum() == 2);
        Assert.assertTrue(seat.getSeatHoldId() == -1);
        Assert.assertTrue(seat.getReservationId() == -1);
        Assert.assertEquals(seat.getExpireime(), null);
        Assert.assertTrue(seat.isAvailable());


    }

    @Test
    public void testIsAvailable(){
        Assert.assertTrue(seat.isAvailable());
    }

    @Test
    public void testNotAvailableForHeld() {
        seat.setSeatHoldId(1);
        seat.setExpireTime(LocalDateTime.now().minusMinutes(5));
        Assert.assertTrue(seat.isAvailable());

        seat.setExpireTime(LocalDateTime.now().plusMinutes(2));
        Assert.assertFalse(seat.isAvailable());
    }

    @Test
    public void testNotAvailableForReserved() {
        seat.setReservationId(1);
        Assert.assertFalse(seat.isAvailable());

    }
    }
