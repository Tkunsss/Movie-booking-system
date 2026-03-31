package ui;

import model.Totals;
import service.Cart;
import service.ShopSettings;

public class TotalsUtil {
    public static Totals calculateTotals(Cart cart) {
        double subtotal = cart.calculateSubtotal();
        double discount = subtotal * ShopSettings.DISCOUNT_RATE;
        double taxable = subtotal - discount;
        double tax = taxable * ShopSettings.TAX_RATE;
        double total = taxable + tax;
        return new Totals(subtotal, discount, tax, total);
    }
}
