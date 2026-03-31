package model;

// OOP: Value object holding calculated state (no behavior).
// Holds subtotal/discount/tax/total numbers.
public class Totals {
    public final double subtotal;
    public final double discount;
    public final double tax;
    public final double total;

    public Totals(double subtotal, double discount, double tax, double total) {
        this.subtotal = subtotal;
        this.discount = discount;
        this.tax = tax;
        this.total = total;
    }
}
