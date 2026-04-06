package service;

import model.Movie;

// OOP: Inheritance (is-a Ticket) + overriding.
// Normal seat ticket.
public class StandardTicket extends Ticket {

    // Make a normal ticket.
    public StandardTicket(Movie movie, int seatNumber) {
        super(movie, seatNumber);
    }

    @Override
    // Overriding: specialized price for StandardTicket.
    // Normal price.
    public double calculatePrice() {
        return movie.getPrice(); // normal price
    }

    @Override
    // Overriding: specialized type name for StandardTicket.
    // Name shown on ticket.
    public String getType() {
        return "Standard Ticket";
    }
}
