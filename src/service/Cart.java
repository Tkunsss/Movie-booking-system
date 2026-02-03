package service;

import model.Booking;

public class Cart {
    private Booking booking;

    public void addBooking(Booking booking) {
        this.booking = booking;
    }

    public Booking getBooking() {
        return booking;
    }
}
