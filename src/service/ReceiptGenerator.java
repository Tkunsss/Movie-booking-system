package service;

import java.io.FileWriter;
import java.io.IOException;

public class ReceiptGenerator {
  
  public static void generateReceipt(Customer customer, double totalAmount){
    try (FileWriter writer = new FileWriter("receipt.txt")){
      writer.write("===== Movie Ticket Receipt =====\n");
      writer.write("Customer: " + customer.getFullName() + "\n");
      writer.write("Customer ID: " + customer.getCustomerId() + "\n");
      writer.write("Total Amount Paid: $" +totalAmount + "\n");
      writer.write("Balance Remaining: $" + customer.getBalance() + "\n");
      writer.write("-----------------------------\n");
      writer.write("Thank you for your purchase!\n");
    } catch (IOException e) {
      System.err.println("Error generating receipt: " + e.getMessage());

    }
  }
}
