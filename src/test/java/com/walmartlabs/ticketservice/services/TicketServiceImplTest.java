package com.walmartlabs.ticketservice.services;

import com.walmartlabs.ticketservice.Exceptions.IncorrectCustomerEmailException;
import com.walmartlabs.ticketservice.Exceptions.NoSuchSeatHoldException;
import com.walmartlabs.ticketservice.models.Seat;
import com.walmartlabs.ticketservice.models.SeatHold;
import com.walmartlabs.ticketservice.models.Venue;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by lian.shi on 3/10/2018.
 */
public class TicketServiceImplTest {
    private static final String CUSTOMEREMAIL = "john@example.com";
    private Venue venue;

    @Before
    public void setUp() {
        venue = new Venue(3, 3);
    }

    @Test
    public void testTicketServiceInit(){
        TicketServiceImpl ticketServiceImpl = new TicketServiceImpl(venue);
        Assert.assertEquals(ticketServiceImpl.getMaxHoldTime(), 300000);
        Assert.assertNotNull(ticketServiceImpl);
        Assert.assertEquals(ticketServiceImpl.getSeatHoldMap().size(), 0);
    }

    @Test
    public void testNumOfAvailable() {
        TicketServiceImpl ticketServiceImp = new TicketServiceImpl(venue);
        Assert.assertEquals(ticketServiceImp.numSeatsAvailable(), 9);
    }

    @Test(expected = NoSuchSeatHoldException.class)
    public void testNoSuchSeatHoldException() throws NoSuchSeatHoldException {
        TicketServiceImpl ticketServiceImpl = new TicketServiceImpl(venue);
        ticketServiceImpl.reserveSeats(5, CUSTOMEREMAIL);
    }

    @Test
    public void testNotEnoughSeats() {
        TicketServiceImpl ticketService = new TicketServiceImpl(venue);
        SeatHold seatHold = ticketService.findAndHoldSeats(10,CUSTOMEREMAIL);
        Assert.assertNull(seatHold);
    }

    @Test
    public void testFindBestAvailableSeats() {
        TicketServiceImpl ticketService = new TicketServiceImpl(venue);
        SeatHold seatHold = ticketService.findAndHoldSeats(1,CUSTOMEREMAIL);
        Seat seat = seatHold.getSeatsToHold().get(0);
        Assert.assertTrue(seat.getRow() == 0);
        Assert.assertTrue(seat.getNum() == 1);
    }

    @Test
    public void testFindAndHolds() {
        TicketServiceImpl ticketService = new TicketServiceImpl(venue, 1000);
        SeatHold seatHold = ticketService.findAndHoldSeats(2, CUSTOMEREMAIL);
        Assert.assertNotNull(seatHold);
        Assert.assertTrue(CUSTOMEREMAIL.equals(seatHold.getCustomerEmail()));
        Assert.assertTrue(!seatHold.isExpired());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(seatHold.isExpired());
    }

    @Test
    public void testReserveSeats() {
        TicketServiceImpl ticketService = new TicketServiceImpl(venue);
        SeatHold seatHold = ticketService.findAndHoldSeats(2, CUSTOMEREMAIL);
        Assert.assertNotNull(seatHold);
        Assert.assertTrue(CUSTOMEREMAIL.equals(seatHold.getCustomerEmail()));
        Assert.assertTrue(!seatHold.isExpired());
        String confirmation = ticketService.reserveSeats(seatHold.getSeatHoldId(), CUSTOMEREMAIL);
        Assert.assertNotNull(confirmation);
        Assert.assertEquals(venue.getCapacity()-2, ticketService.numSeatsAvailable());
        Assert.assertNull(ticketService.getSeatHoldMap().get(seatHold));
    }

    @Test(expected = IncorrectCustomerEmailException.class)
    public void testIncorrectCustomerEmailException() throws IncorrectCustomerEmailException {
        TicketServiceImpl ticketServiceImpl = new TicketServiceImpl(venue, 2000);
        SeatHold seatHold1 = ticketServiceImpl.findAndHoldSeats(2, CUSTOMEREMAIL);
        ticketServiceImpl.reserveSeats(seatHold1.getSeatHoldId(), "Bob@exmaple.com");
    }

    @Test
    public void testReleaseHoldSeats() {
        TicketServiceImpl ticketServiceImpl = new TicketServiceImpl(venue, 1000);
        SeatHold seatHold = ticketServiceImpl.findAndHoldSeats(2, CUSTOMEREMAIL);
        String confirmation = ticketServiceImpl.reserveSeats(seatHold.getSeatHoldId(), CUSTOMEREMAIL);

        SeatHold seatHold2 = ticketServiceImpl.findAndHoldSeats(2, CUSTOMEREMAIL);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(seatHold2.isExpired());
        int nums = ticketServiceImpl.numSeatsAvailable();
        Assert.assertEquals(venue.getCapacity()-2, ticketServiceImpl.numSeatsAvailable());
    }




}
