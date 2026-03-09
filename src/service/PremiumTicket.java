package service;

import model.Movie;

public class PremiumTicket extends Ticket {

    public PremiumTicket(Movie movie, int seatNumber) {
        super(movie, seatNumber);
    }

    @Override
    public double calculatePrice() {
        return movie.getPrice() + 3.0; // VIP extra charge
    }

    @Override
    public String getType() {
        return "Premium Ticket (VIP)";
    }
}
