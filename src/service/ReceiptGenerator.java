package service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// OOP: Static utility class (no object state).
// This class writes receipt files.
public class ReceiptGenerator {
  
  // Write a receipt with full details.
  public static void generateReceipt(Customer customer, Cart cart,
                                     double subtotal, double discount,
                                     double tax, double totalAmount) {
    // Use time to make file name unique.
    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    String fileName = "receipt_" + timestamp + ".txt";

    // Write receipt to file.
    try (FileWriter writer = new FileWriter(fileName)) {
      writer.write("===== Movie Ticket Receipt =====\n");
      writer.write("Customer: " + customer.getFullName() + "\n");
      writer.write("Customer ID: " + customer.getCustomerId() + "\n");
      writer.write("Phone: " + customer.getPhone() + "\n");
      writer.write("-----------------------------\n");
      writer.write("Tickets:\n");

      if (cart == null || cart.getItems().isEmpty()) {
        writer.write("- (No ticket details)\n");
      } else {
        for (Ticket ticket : cart.getItems()) {
          writer.write("- " + ticket + "\n");
        }
      }

      writer.write("-----------------------------\n");
      writer.write("Subtotal: $" + subtotal + "\n");
      writer.write("Discount: -$" + discount + "\n");
      writer.write("Tax: $" + tax + "\n");
      writer.write("Total Amount Paid: $" + totalAmount + "\n");
      writer.write("Balance Remaining: $" + customer.getBalance() + "\n");
      writer.write("-----------------------------\n");
      writer.write("Thank you for your purchase!\n");
    } catch (IOException e) {
      // Print error if file write fails.
      System.err.println("Error generating receipt: " + e.getMessage());
    }
  }

  // Simple receipt method (backward compatible).
  public static void generateReceipt(Customer customer, double totalAmount) {
    generateReceipt(customer, null, totalAmount, 0, 0, totalAmount);
  }
}
