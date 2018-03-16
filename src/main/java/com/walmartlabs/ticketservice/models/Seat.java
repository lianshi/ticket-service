package com.walmartlabs.ticketservice.models;


import sun.security.jca.GetInstance;

import java.time.LocalDateTime;

/**
 * Created by lian.shi on 3/9/2018.
 */
public class Seat {
    private int row;
    private int num;
    private int seatHoldId;
    private int reservationId;
    private int value;
    private LocalDateTime expireTime;


    public Seat(int row, int num, int value) {
        this.row = row;
        this.num = num;
        this.value = value;
        this.seatHoldId = -1;
        this.reservationId = -1;
    }

    /**
     * @return boolean true if the seat is not temporarily held or reserved
     */
    public boolean isAvailable() {
        if (this.reservationId > 0) {
            return false;
        } else if (this.seatHoldId > 0) {
            return this.expireTime == null || this.expireTime.isBefore(LocalDateTime.now());
        }
        return true;
    }


    public int getRow() { return this.row; }

    public void setRow(int row) { this.row = row; }

    public int getNum() { return this.num; }

    public void setNum(int num) { this.num = num; }

    public int getSeatHoldId() { return this.seatHoldId; }

    public void setSeatHoldId(int seatHoldId) { this.seatHoldId = seatHoldId; }

    public int getReservationId() { return this.reservationId; }

    public void setReservationId(int reservationId) { this.reservationId = reservationId; }

    public LocalDateTime getExpireime() { return expireTime; }

    public void setExpireTime(LocalDateTime expireTime) { this.expireTime = expireTime; }

    public String toString() {
        return String.format("Seat: row 1d, num 2d", row, num);
    }

    public int getValue() { return value; }

    public void setValue(int value) { this.value = value; }

}
