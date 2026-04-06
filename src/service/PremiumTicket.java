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
    // Overriding: specialized price for PremiumTicket.
    // VIP price (extra charge).
    public double calculatePrice() {
        return movie.getPrice() + 3.0; // VIP extra charge
    }

    @Override
    // Overriding: specialized type name for PremiumTicket.
    // Name shown on ticket.
    public String getType() {
        return "Premium Ticket (VIP)";
    }
}
