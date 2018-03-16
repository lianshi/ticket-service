package com.walmartlabs.ticketservice.services;

import com.walmartlabs.ticketservice.Exceptions.IncorrectCustomerEmailException;
import com.walmartlabs.ticketservice.Exceptions.NoSuchSeatHoldException;
import com.walmartlabs.ticketservice.models.Reservation;
import com.walmartlabs.ticketservice.models.Seat;
import com.walmartlabs.ticketservice.models.SeatHold;
import com.walmartlabs.ticketservice.models.Venue;


import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by lian.shi on 3/9/2018.
 */
public class TicketServiceImpl implements TicketService {

    private final static Logger logger = Logger.getLogger(TicketServiceImpl.class.getName());

    /**
     * Set the default max hold time to be 5 minutes;
     */
    private static final long DEFAULT_HOLD_TIME = 5 * 60 * 1000;
    private long maxHoldTime;
    private List<Seat> allSeats = new ArrayList<>();
    private Map<Integer, SeatHold> seatHoldMap = new HashMap<>();
    private Map<Integer, Reservation> reservations = new HashMap<>();

    private Venue venue;

    //If hold time is not specified when creating the instance, use the default hold time.
    public TicketServiceImpl(Venue venue) {
        this(venue, DEFAULT_HOLD_TIME);
    }

    public TicketServiceImpl(Venue venue, long maxHoldTime) {
        this.venue = venue;
        this.maxHoldTime = (maxHoldTime > 0)? maxHoldTime : DEFAULT_HOLD_TIME;
        initVenue(venue);
    }

    //When initial the venue, each seat is available and assigned with the value and then the seats are sorted by the value.
    private List<Seat> initVenue(Venue venue) {
        int rows = venue.getRows();
        int nums = venue.getSeatsPerRow();
        for (int i = 0; i < venue.getRows(); i++) {
            for (int j = 0; j < nums; j++) {
                int mid = (nums % 2 == 0)? nums/2 -1: nums/2   ;
                int value = rows-i;
                if (j <= mid) {
                    value *= (j - 0 + 1);
                } else {
                    value *= (nums - j);
                }
                Seat seat = new Seat(i, j, value);
                allSeats.add(seat);
            }
        }
        allSeats.sort(new Comparator<Seat>() {
            @Override
            public int compare(Seat o1, Seat o2) {
                return (o1.getValue() < o2.getValue())? 1 : -1;
            }
        });
        return allSeats;
    }

    /**
     * The number of seats in the venue that are neither held nor reserved
     *
     * @return the number of tickets available in the venue
     */
    @Override
    public synchronized int numSeatsAvailable() {
        return allSeats.stream().filter(s -> s.isAvailable()).toArray().length;

    }

    /**
     * Find and hold the best available seats for a customer
     *
     * @param numSeats      the number of seats to find and hold
     * @param customerEmail unique identifier for the customer
     * @return a SeatHold object identifying the specific seats and related
     * information
     */
    @Override
    public synchronized SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
        if (numSeats > venue.getCapacity()) {
            logger.log(Level.INFO, String.format(" The venue doesn't have enough requested seats: %d for customer: $s", numSeats, customerEmail));
            return null;
        }
        if (numSeats > numSeatsAvailable()) {
            logger.log(Level.INFO, String.format("There are not enough available seats for the requested seats: %d" +
                    " for customer: $s", numSeats, customerEmail));
            return null;
        }
        List<Seat> seatsToHold = new ArrayList<>();
        LocalDateTime expireTime = LocalDateTime.now().plusSeconds(maxHoldTime/1000);
        SeatHold seatHold = new SeatHold(numSeats, expireTime, customerEmail);
        int count = 0;
        for (Seat seat : allSeats) {
            if (seat.isAvailable()) {
                seat.setSeatHoldId(seatHold.getSeatHoldId());
                seat.setExpireTime(expireTime);
                seatsToHold.add(seat);
                if (++count == numSeats)
                    break;
            }
        }
        seatHold.setSeatsToHold(seatsToHold);
        seatHoldMap.put(seatHold.getSeatHoldId(), seatHold);
        return seatHold;
    }
    /**
     * Commit seats held for a specific customer
     *
     * @param seatHoldId    the seat hold identifier
     * @param customerEmail the email address of the customer to which the
     *                      seat hold is assigned
     * @return a reservation confirmation code
     * @throws NoSuchSeatHoldException if a corresponding hold cannot be found.
     * @throws IncorrectCustomerEmailException if the customer email doesn't match the seatHold email;
     */
    @Override
    public synchronized String reserveSeats(int seatHoldId, String customerEmail) {
        logger.log(Level.INFO, String.format("Starting to reserve seats for customer %s ", customerEmail));
        //A valid seatHodId is always greater than 0
        if (seatHoldId < 0) {
            throw new IllegalArgumentException("Invalid seatHoldId!");
        }
        //Customer email is required for any reservation and cannot be empty;
        if (customerEmail == null || customerEmail.isEmpty()) {
            throw new IllegalArgumentException("Invalid email address!");
        }

        //TO make a reservation, a seatHoldId should exist.
        if (!seatHoldMap.containsKey(seatHoldId)) {
            throw new NoSuchSeatHoldException("Could not find such seatHoldId: " + seatHoldId);
        }

        //To make a reservation, a seatHold should not be expired by the time.
        SeatHold seatHold = seatHoldMap.get(seatHoldId);
        if (seatHold.isExpired()) {
            return null;
        }

        //The customer email should match the seatHold's email.
        if (!customerEmail.equals(seatHold.getCustomerEmail())) {
            throw new IncorrectCustomerEmailException("Customer email doesn't match!");
        }

        Reservation reservation = new Reservation(seatHoldId, customerEmail);
        List<Seat> seatsReserved = seatHold.getSeatsToHold();

        //Update each reserved seat with the reservationID
        for (Seat seat : seatsReserved) {
            seat.setReservationId(reservation.getReservationId());
        }

        //Put the reservation into the map;
        reservations.put(reservation.getReservationId(), reservation);

        //remove the seatHold from the seatHoldMap as the reservation is finished.
        seatHoldMap.remove(seatHoldId);
        String confirmation = String.format("Reservation succeeded: %d. Confirmation letter will be sent to your email: %s", reservation.getReservationId(), customerEmail);
        logger.log(Level.INFO, confirmation);
        return confirmation;
    }

    public static long getDefaultHoldTime() { return DEFAULT_HOLD_TIME; }


    public long getMaxHoldTime() { return maxHoldTime; }

    public void setMaxHoldTime(long maxHoldTime) { this.maxHoldTime = maxHoldTime; }

    public List<Seat> getAllSeats() { return allSeats; }

    public void setAllSeats(List<Seat> allSeats) { this.allSeats = allSeats; }

    public Map<Integer, SeatHold> getSeatHoldMap() { return seatHoldMap; }

    public void setSeatHoldMap(Map<Integer, SeatHold> seatHoldMap) { this.seatHoldMap = seatHoldMap; }

    public Map<Integer, Reservation> getReservations() { return reservations; }

    public void setReservations(Map<Integer, Reservation> reservations) { this.reservations = reservations; }

    public Venue getVenue() { return venue; }

    public void setVenue(Venue venue) { this.venue = venue; }
}
