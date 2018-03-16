package com.walmartlabs.ticketservice.models;

import java.util.List;

/**
 * Created by lian.shi on 3/10/2018.
 */
public class Reservation {
    private int seatHoldId;
    private static int id = 0;
    private int reservationId;
    private String customerEmail;
    List<Seat> seatReserved;

    public Reservation(int seatHoldId, String customerEmail) {
        this.reservationId = ++ id;
        this.seatHoldId = seatHoldId;
        this.customerEmail = customerEmail;
    }

    public int getSeatHoldId() { return this.seatHoldId; }

    public void setSeatHoldId(int seatHoldId) { this.seatHoldId = seatHoldId;
    }

    public int getReservationId() { return this.reservationId; }

    public void setReservationId(int reservationId) { this.reservationId = reservationId;
    }

    public String getCustomerEmail() { return this.customerEmail;
    }

    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail;
    }

    public List<Seat> getSeatReserved() { return seatReserved;
    }

    public void setSeatReserved(List<Seat> seatReserved) { this.seatReserved = seatReserved;
    }
}
