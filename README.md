# Course Project: JavaBeans POS System

**Course:** Object Oriented Programming

---
![coffee shop](Gemini_Generated_Image_tsk4u1tsk4u1tsk4.png)

## 1. The Vision

**"From Console to Cloud-Connected GUI"**

You have been hired by a local startup, **JavaBeans Coffee**, to modernize their business. Currently, they use pen and paper to track orders, leading to calculation errors and lost sales data.

**Your Mission:**
Build a professional **Point of Sale (POS) System** using Java. By Week 10, this application will allow a Barista to log in, view a graphical menu, process orders, and save sales history to a MySQL database.

This project is not just about writing code; it is about learning **Software Architecture**. You will move from writing simple C-style scripts to building a robust, 3-tier Java application.

---

## 2. Functional Requirements

Your system must support two specific User Roles:

### Role A: The Barista (Front of House)

* **Visual Menu:** View products (Drinks, Food) with prices and categories.
* **Shopping Cart:** Add items to a cart, update quantities, and remove items.
* **Checkout:** Automatically calculate **Subtotal**, **Tax (10%)**, and **Grand Total**.
* **Receipts:** Generate a digital receipt (Text File) for every sale.

### Role B: The Manager (Back Office)

* **Inventory Management:** Add new products or update prices (e.g., change "Latte" price from $3.50 to $4.00).
* **Data Persistence:** All changes must be saved to the database. If the app restarts, the data must not be lost.

---

## 3. Technical Architecture

We will use a **3-Tier Architecture** to separate concerns. This is an industry-standard approach.

| Layer | Responsibility | Tech Stack |
| --- | --- | --- |
| **1. Presentation** | What the user *sees*. Windows, Buttons, Tables. | Java Swing |
| **2. Business Logic** | How the system *thinks*. Calculations, Discounts, Tax. | Java Classes (OOP) |
| **3. Data Access** | How the system *remembers*. Saving/Loading data. | JDBC & MySQL |

**Tools:**
* **IDE:** Visual Studio Code (VS Code)
* **Build Tool:** Maven (for managing dependencies) 
* **Database:** MySQL

---

## 4. Project Roadmap (Weeks 2-9)

This project is integrated directly into your weekly syllabus. Every theory lesson corresponds to a project feature.

### **Phase 1: The Core Logic (Weeks 2-3)**

* **Week 2: Project Initialization & References** 

* *Theory:* Reference vs. Value types, Static keyword.
* *Task:* Initialize the project structure. Create the `Cart` class (using Arrays) and `ShopSettings` (static tax rate).


* **Week 3: Encapsulation & Collections** 
* *Theory:* Access Modifiers (`private`/`public`), Packages, ArrayList.
* *Task:* Secure the `Product` data. Replace fixed Arrays with `ArrayList` so the Cart can hold unlimited items.



### **Phase 2: Flexibility & Hierarchy (Weeks 4-5)**

* **Week 4: Contracts & Interfaces** 
* *Theory:* Interfaces and Abstract Methods.
* *Task:* Implement a `Discountable` interface. Create logic for "Staff Discount" vs. "Seasonal Promotion."


* **Week 5: Inheritance & Polymorphism** 
* *Theory:* Superclasses, Subclasses, and Overriding.
* *Task:* Expand the menu. Create `Drink` (has size) and `Food` (has heating option) classes that inherit from `Product`.



### **Phase 3: Robustness & Data (Weeks 6-7)**

* **Week 6: Exceptions & File I/O** 

* *Theory:* Try-Catch blocks, File Reading/Writing, Threads.
* *Task:* Prevent the app from crashing on invalid inputs. Generate a `receipt.txt` file upon checkout.


* **Week 7: Advanced Logic (Pre-GUI)** 

* *Theory:* Anonymous Inner Classes, Lambda Expressions.
* *Task:* Write the "Event Listeners" logic that will eventually power the buttons (e.g., sorting the menu by price).



### **Phase 4: The Final Product (Weeks 8-9)**

* **Week 8: GUI & Database Connection** 


* *Theory:* Java Swing components, JDBC, Maven.
* *Task:* Build the visual window. Connect the app to **MySQL** to load real data.


* **Week 9: Final Presentation** 


* *Task:* Demonstrate your working application.
* *Success Criteria:* App compiles, connects to DB, and processes a full order.



**Key Success Indicators:**

1. **Code Quality:** Proper naming, indentation, and file structure.
2. **OOP Implementation:** Correct usage of Inheritance, Interfaces, and Encapsulation.
3. **Functionality:** Does the math work? Does the database update correctly?
