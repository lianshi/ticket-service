package com.walmartlabs.ticketservice.models;

/**
 * Created by lian.shi on 3/9/2018.
 */
public class Venue {
    private int rows;
    private int seatsPerRow;

    public Venue(int rows, int seatsPerRow) {
        this.rows = rows;
        this.seatsPerRow = seatsPerRow;

    }

    public int getRows() { return rows; }

    public void setRows(int rows) { this.rows = rows; }

    public int getSeatsPerRow() { return seatsPerRow; }

    public void setSeatsPerRow(int seatsPerRow) { this.seatsPerRow = seatsPerRow; }

    public int getCapacity() { return this.rows * this.seatsPerRow ; }
}
