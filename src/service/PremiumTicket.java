package service;

import model.Movie;

// OOP: Inheritance (is-a Ticket) + overriding.
// VIP seat ticket.
public class PremiumTicket extends Ticket {

    // Make a VIP ticket.
    public PremiumTicket(Movie movie, int seatNumber) {
        super(movie, seatNumber);
    }

    @Override
    // VIP price (extra charge).
    public double calculatePrice() {
        return movie.getPrice() + 3.0; // VIP extra charge
    }

    @Override
    // Name shown on ticket.
    public String getType() {
        return "Premium Ticket (VIP)";
    }
}
