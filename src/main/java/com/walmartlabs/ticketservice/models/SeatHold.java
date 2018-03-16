package com.walmartlabs.ticketservice.models;

import sun.security.jca.GetInstance;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lian.shi on 3/9/2018.
 */
public class SeatHold {
    private static int id = 0;
    private int seatHoldId;
    private LocalDateTime expireTime;
    private List<Seat> seatsToHold;
    private int nums;
    private String customerEmail;

    public SeatHold(int nums, LocalDateTime expireTime, String customerEmail) {
        this.seatHoldId = ++id;
        this.nums = nums;
        this.expireTime = expireTime;
        this.customerEmail = customerEmail;
    }

    /**
     * @return boolean true if the seatHOld is not expired.
     */
    public boolean isExpired() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return expireTime.isBefore(localDateTime);
    }

    public int getSeatHoldId() { return seatHoldId; }

    public void setSeatHoldId(int seatHoldId) { this.seatHoldId = seatHoldId; }

    public LocalDateTime getExpireTime() { return expireTime; }

    public void setExpireTime(LocalDateTime expireTime) { this.expireTime = expireTime; }

    public List<Seat> getSeatsToHold() { return seatsToHold; }

    public void setSeatsToHold(List<Seat> seatsToHold) { this.seatsToHold = seatsToHold; }

    public String getCustomerEmail() { return customerEmail; }

    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public int getNums() { return nums; }

    public void setNums(int nums) { this.nums = nums; }
}
