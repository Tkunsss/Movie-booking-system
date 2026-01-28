# Movie Ticket Booking System

**Course:** Object Oriented Programming  

---

## 1. Project Vision

**"From Console to Cloud-Connected GUI"**

You have been hired by a local startup, **Movie Ticket Booking System**, to modernize their business. Currently, they use pen and paper to track bookings, leading to calculation errors, double bookings, and lost sales data.

**Mission:**  
Build a professional **Ticket Booking System** using Java. By Week 10, this application will allow a **Front Desk Staff** to log in, view movie showtimes, book tickets, and save sales history to a MySQL database.

This project focuses not just on coding, but on **Software Architecture**. You will move from simple scripts to building a robust, 3-tier Java application.

---

## 2. Functional Requirements

The system must support **two user roles**:

### Role A: Front Desk Staff
- **Visual Movie Schedule:** View available movies, showtimes, and prices.
- **Seat Selection:** Select seats with real-time availability checks.
- **Shopping Cart:** Add multiple tickets, update quantities, remove tickets.
- **Checkout:** Automatically calculate:
  - Subtotal  
  - Tax (10%)  
  - Grand Total
- **Receipts:** Generate a digital receipt (`receipt.txt`) for every sale.

### Role B: Manager (Back Office)
- **Movie & Showtime Management:** Add new movies, update ticket prices, manage available seats.
- **Data Persistence:** All changes are saved to the database. Data remains intact after app restarts.

---

## 3. Technical Architecture

The system follows a **3-Tier Architecture** to separate concerns:

| Layer | Responsibility | Tech Stack |
|-------|----------------|------------|
| **Presentation** | What the user *sees*. Windows, Buttons, Tables. | Java Swing |
| **Business Logic** | How the system *thinks*. Calculations, Discounts, Seat Availability. | Java Classes (OOP) |
| **Data Access** | How the system *remembers*. Saving/Loading data. | JDBC & MySQL |

**Tools:**
- **IDE:** Visual Studio Code (VS Code)
- **Build Tool:** Maven (dependency management) 
- **Database:** MySQL

---

## 4. Project Roadmap (Weeks 2-9)

This project is integrated directly into the weekly syllabus. Every theory lesson corresponds to a project feature.

### Phase 1: Core Logic (Weeks 2-3)
- **Week 2: Project Initialization & References**  
  *Theory:* Reference vs. Value types, Static keyword.  
  *Task:* Initialize project structure. Create `BookingCart` class (using Arrays) and `TheaterSettings` (static tax rate, max seats per show).

- **Week 3: Encapsulation & Collections**  
  *Theory:* Access Modifiers (`private`/`public`), Packages, ArrayList.  
  *Task:* Secure `Movie` and `Ticket` data. Replace fixed Arrays with `ArrayList` for unlimited tickets.

### Phase 2: Flexibility & Hierarchy (Weeks 4-5)
- **Week 4: Contracts & Interfaces**  
  *Theory:* Interfaces and Abstract Methods.  
  *Task:* Implement `Discountable` interface. Logic for "Staff Discount" vs. "Seasonal Promotion."

- **Week 5: Inheritance & Polymorphism**  
  *Theory:* Superclasses, Subclasses, and Overriding.  
  *Task:* Expand ticket types. Create `StandardTicket` (normal seat) and `PremiumTicket` (VIP seat) inheriting from `Ticket`.

### Phase 3: Robustness & Data (Weeks 6-7)
- **Week 6: Exceptions & File I/O**  
  *Theory:* Try-Catch blocks, File Reading/Writing, Threads.  
  *Task:* Prevent app crashing on invalid inputs. Generate `receipt.txt` on checkout.

- **Week 7: Advanced Logic (Pre-GUI)**  
  *Theory:* Anonymous Inner Classes, Lambda Expressions.  
  *Task:* Write event listener logic (e.g., filter movies by genre or showtime).

### Phase 4: Final Product (Weeks 8-9)
- **Week 8: GUI & Database Connection**  
  *Theory:* Java Swing, JDBC, Maven.  
  *Task:* Build the GUI and connect to MySQL to load real movie and ticket data.

- **Week 9: Final Presentation**  
  *Task:* Demonstrate the working application.  
  *Success Criteria:* App compiles, connects to DB, processes full booking, and generates receipts.

---

## 5. Key Success Indicators

1. **Code Quality:** Proper naming, indentation, and file structure.  
2. **OOP Implementation:** Correct use of Inheritance, Interfaces, and Encapsulation.  
3. **Functionality:** Correct calculations, seat tracking, and database updates.

---

## 6. Optional Diagram

A class diagram (example):

