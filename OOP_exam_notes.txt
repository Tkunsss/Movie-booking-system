OOP Oral Exam Quick Notes (Your Project)
Use this 3-step formula for any answer:
1) Define the concept in 1-2 sentences.
2) Point to where it appears in your code (file + class/method).
3) Explain why you designed it that way (benefit).

Project Anchor Files (use these often)
- Encapsulation + validation: src/service/Customer.java
- Inheritance + overriding: src/service/Ticket.java, StandardTicket.java, PremiumTicket.java
- Interface + polymorphism: src/service/istaff.java + ManagerStaff/CashierStaff/Ticketing_Agent
- Composition: src/model/Order.java (has Cart + Totals + Customer), src/service/Cart.java (has List<Ticket>)
- Collections: src/ui/AppState.java (lists of movies/staff/customers/orders)
- Static: src/service/ShopSettings.java, src/service/ReceiptGenerator.java, src/db/Database.java
- Exceptions: DAO classes in src/db/*.java (try/catch), UI parsing in src/ui/*.java
- GUI: src/ui/*.java (Swing)

# OOP Exam Q&A Notes

This repository contains summarized answers for 50 OOP exam sets. Each question is followed immediately by its answer for easy memorization.

---

## Set 1

**Q1: What is the difference between a class and an object, and why do objects need both fields and methods?**  
A: Class = blueprint, Object = real instance. Fields = state, methods = behavior.

**Q2: In your project, show one class and explain its responsibility, its important fields, and one method that changes the object’s state.**  
A: Example class Customer (src/service/Customer.java): fields name/balance, method deductBalance() changes state.

**Q3: In your project, explain why this part should be modeled using objects instead of writing everything in one long main() method or using many unrelated variables.**  
A: Using objects keeps data + behavior together; avoids one huge main() and duplicated logic.

---

## Set 2

**Q1: What is the difference between an object’s state and behavior, and how do fields and methods represent them?**  
A: State = stored data, behavior = actions. Fields represent state, methods represent behavior.

**Q2: Show one object from your project and explain its state and behavior clearly.**  
A: Order (src/model/Order.java): state = totals/status, behavior in UI (checkout/refund) changes status.

**Q3: Explain why keeping state and behavior together inside the same class is better than storing data in one place and controlling it from unrelated code.**  
A: Keeping state + behavior together prevents mismatch and makes code easier to maintain.

---

## Set 3

**Q1: What is a constructor, and how is it different from a normal method?**  
A: Constructor initializes object at creation; normal methods act later.

**Q2: Show one constructor in your project and explain what it initializes immediately.**  
A: Movie constructor in src/model/Movie.java sets title/price/date immediately.

**Q3: Explain what design or runtime problems could happen if objects in your project are created without proper initialization.**  
A: Without proper init, objects may be invalid (null/0), causing bugs in pricing or UI.

---

## Set 4

**Q1: What is the purpose of the this keyword, and when is it especially useful?**  
A: this refers to current object; used to distinguish fields vs parameters.

**Q2: Show one constructor or setter in your project where this helps distinguish a field from a parameter.**  
A: Customer setters use this.field in src/service/Customer.java.

**Q3: Explain what happens if a programmer forgets to use this when parameter names and field names are the same.**  
A: Without this, fields may not be updated and state stays wrong.

---

## Set 5

**Q1: What is encapsulation, and how is it related to private and public?**  
A: Encapsulation hides fields (private) and controls access via methods.

**Q2: Show one example in your project where a field is hidden and controlled through methods.**  
A: Customer has private fields + getters/setters in src/service/Customer.java.

**Q3: If all fields in your project were made public, what correctness or design problems could happen?**  
A: Public fields allow invalid values, break validation, and cause inconsistent data.

---

## Set 6

**Q1: Why do programmers use getters and setters, and how is that connected to encapsulation?**  
A: Getters/setters give controlled access and validation.

**Q2: Show one getter or setter in your project and explain what control it gives you.**  
A: Customer.setPhone validates digits/length (src/service/Customer.java).

**Q3: Explain when a setter should not exist, or when direct modification of data would be dangerous in your project.**  
A: Some setters shouldn’t exist (e.g., setting balance directly) to prevent abuse.

---

## Set 7

**Q1: What is the difference between a primitive value and a reference type in Java?**  
A: Primitive = value stored directly; reference = points to object.

**Q2: Explain what happens when two variables refer to the same object.**  
A: Two variables can reference same Customer object (lists in AppState).

**Q3: Show how shared references could create unexpected side effects in a project if the programmer is careless.**  
A: Shared references can cause unexpected side effects if you modify through one reference.

---

## Set 8

**Q1: What is the difference between passing a primitive value and passing an object reference to a method?**  
A: Passing primitive copies value; passing object passes reference.

**Q2: Show one method in your project that receives an object as a parameter and explain why.**  
A: Methods use Customer object (e.g., ReceiptGenerator.generateReceipt in src/service/ReceiptGenerator.java).

**Q3: Explain how object references allow different classes to collaborate on the same data without copying everything.**  
A: References allow collaboration without copying entire objects.

---

## Set 9

**Q1: What is inheritance, and how is it different from copying code into multiple classes?**  
A: Inheritance = child inherits parent; not same as copying code.

**Q2: Show one parent-child relationship in your project and explain what the child gets from the parent.**  
A: StandardTicket extends Ticket (src/service/StandardTicket.java).

**Q3: Explain why that relationship is a real is-a relationship and not just code reuse.**  
A: It is a real is-a: a StandardTicket IS a Ticket.

---

## Set 10

**Q1: What is the purpose of the super keyword, and how is it related to inheritance?**  
A: super calls parent constructor or method.

**Q2: Show where super could be used in your project, either in a constructor or in an overridden method.**  
A: StandardTicket calls super(movie, seat) in constructor.

**Q3: Explain what may go wrong if a child class does not properly initialize the parent part of the object.**  
A: If parent isn’t initialized, child may miss required data like movie/seat.

---

## Set 11

**Q1: What is method overriding, and how is it related to inheritance?**  
A: Overriding replaces parent method in child.

**Q2: Show one overridden method in your project and explain why the child version is different from the parent version.**  
A: PremiumTicket overrides calculatePrice() in src/service/PremiumTicket.java.

**Q3: Explain how overriding helps you avoid writing large if-else blocks based on object type.**  
A: Avoids if-else based on type and keeps pricing logic in subclasses.

---

## Set 12

**Q1: What is polymorphism in your own words?**  
A: Polymorphism = one reference can work with many actual types.

**Q2: Show one example from your project where one reference can work with different object types.**  
A: List<Ticket> in Cart holds StandardTicket and PremiumTicket (src/service/Cart.java).

**Q3: Explain how polymorphism makes your design easier to extend when new child classes are added.**  
A: New ticket types can be added without changing Cart logic.

---

## Set 13

**Q1: What is the difference between a variable’s reference type and the real object type it points to at runtime?**  
A: Reference type is compile-time; real object type is runtime.

**Q2: Show one example in your project where the reference type is more general than the created object.**  
A: Ticket t = new PremiumTicket(...) (Ticket reference, PremiumTicket object).

**Q3: Explain how Java decides which overridden method to call at runtime.**  
A: Java uses runtime type to choose overridden method (dynamic dispatch).

---

## Set 14

**Q1: What is an interface, and why is it often described as a contract?**  
A: Interface = contract of methods.

**Q2: Show one interface from your project and explain the behavior it promises.**  
A: istaff interface promises can(), getUsername(), etc. (src/service/istaff.java).

**Q3: Explain why using an interface is better than depending directly on one concrete class in that situation.**  
A: UI can treat staff uniformly without depending on concrete class.

---

## Set 15

**Q1: How are interface and polymorphism connected?**  
A: Interfaces enable polymorphism.

**Q2: Show how one interface in your project could allow multiple implementations.**  
A: List<istaff> in AppState can store ManagerStaff/CashierStaff/Ticketing_Agent.

**Q3: Explain how this design helps future extension without changing too much old code.**  
A: Add new staff type with minimal changes.

---

## Set 16

**Q1: What is an abstract class, and why can it not be used to create direct objects?**  
A: Abstract class has shared behavior but can’t be instantiated.

**Q2: Show one place in your project where an abstract class would make sense.**  
A: Ticket could be abstract if only subclasses should be created.

**Q3: Explain why making that class abstract is better than making it a normal concrete class.**  
A: Prevents creating incomplete base objects.

---

## Set 17

**Q1: What is an abstract method, and why does it have no body?**  
A: Abstract method has no body; subclasses must implement.

**Q2: Show one behavior in your project that different child classes could implement differently.**  
A: Ticket.calculatePrice could be abstract with different pricing.

**Q3: Explain how abstract methods help a team keep class design consistent.**  
A: Forces consistent design across subclasses.

---

## Set 18

**Q1: What are two important differences between an abstract class and an interface?**  
A: Abstract class can have state + methods; interface only method contracts.

**Q2: Show where your project uses, or should use, an interface or an abstract class.**  
A: Use interface for staff (multiple implementations), abstract for Ticket if needed.

**Q3: For that case, justify why one is a better design choice than the other.**  
A: Interface is better when you want flexible multiple implementations.

---

## Set 19

**Q1: What is a has-a relationship, and how is it different from an is-a relationship?**  
A: has-a = composition, is-a = inheritance.

**Q2: Show one has-a relationship and one is-a relationship from your project, if possible.**  
A: Order has-a Cart; StandardTicket is-a Ticket.

**Q3: Explain why choosing the wrong relationship type would make the design weaker or more confusing.**  
A: Wrong choice makes design confusing and harder to extend.

---

## Set 20

**Q1: What is composition, and how is it different from inheritance?**  
A: Composition = use objects inside, inheritance = extend.

**Q2: Show one place in your project where one class contains or uses another class.**  
A: Cart contains List<Ticket>.

**Q3: Explain why composition is a better choice than inheritance for that case.**  
A: Composition keeps classes focused; inheritance can be too rigid.

---

## Set 21

**Q1: What does it mean for two objects to collaborate without one inheriting from the other?**  
A: Collaboration = classes work together without inheriting.

**Q2: Show two classes in your project that work together and explain their relationship.**  
A: ReceiptGenerator uses Customer + Cart to produce receipt.

**Q3: Explain why distributing responsibility across collaborating classes makes the system easier to maintain.**  
A: Collaboration distributes responsibility and improves maintainability.

---

## Set 22

**Q1: Why is it useful for a class to have one clear responsibility?**  
A: Single responsibility makes class easier to maintain.

**Q2: Show one class in your project and explain its main responsibility.**  
A: MovieDao handles only movie DB operations.

**Q3: Identify one class that may currently do too much and explain how you would redesign it.**  
A: App.java does too much; could be refactored into controllers.

---

## Set 23

**Q1: What is cohesion, and how is it related to good class design?**  
A: Cohesion = class does one focused job.

**Q2: Show one class in your project that has good cohesion.**  
A: OrderDao has cohesive DB operations for orders.

**Q3: Explain how weak cohesion can make a class harder to test, understand, or extend.**  
A: Low cohesion makes code harder to test and change.

---

## Set 24

**Q1: What does loose coupling mean, and why is it valuable in object-oriented design?**  
A: Loose coupling = minimal dependencies.

**Q2: Identify two parts of your project that should not depend too tightly on each other.**  
A: UI uses DAOs instead of raw SQL.

**Q3: Explain how interfaces, better method design, or clearer responsibilities could reduce coupling there.**  
A: Interfaces or DAOs reduce coupling and make changes safer.

---

## Set 25

**Q1: What is the difference between an instance field and a static field?**  
A: Instance field belongs to object; static shared by class.

**Q2: Show one place in your project where an instance variable makes more sense than a static variable.**  
A: Customer.balance should be instance not static.

**Q3: Explain a case where using static incorrectly would cause wrong shared behavior between objects.**  
A: Static balance would incorrectly share money across customers.

---

## Set 26

**Q1: What is the difference between an instance method and a static method?**  
A: Instance method works on object state; static doesn’t.

**Q2: Show one method in your project that should clearly be instance-based and explain why.**  
A: Customer.deductBalance must be instance.

**Q3: Explain how a project becomes weaker if too many methods are made static.**  
A: Too many static methods leads to procedural code.

---

## Set 27

**Q1: What is method overloading, and how is it different from method overriding?**  
A: Overloading = same name, different params; overriding = child replaces parent.

**Q2: Explain one situation in your project where overloading would be useful.**  
A: ReceiptGenerator.generateReceipt overloaded with two signatures.

**Q3: Compare overloading and overriding in terms of extensibility and design value.**  
A: Overloading helps reuse; overriding helps polymorphism.

---

## Set 28

**Q1: What is an ArrayList, and how is it different from a normal array?**  
A: ArrayList grows dynamically, unlike fixed array.

**Q2: Show where you used an ArrayList in your project and explain why it was a better choice.**  
A: AppState uses List<Movie> / List<Order>.

**Q3: Explain how ArrayList makes object management easier when the number of items can change.**  
A: Movie list changes, so ArrayList fits.

---

## Set 29

**Q1: Why is it useful to store objects inside a collection of a common type?**  
A: Collection of common type simplifies processing.

**Q2: Show how inheritance or interfaces allow multiple objects to be kept in the same list.**  
A: List<Ticket> holds StandardTicket + PremiumTicket.

**Q3: Explain how that design helps when you need to process many similar objects using one loop.**  
A: Loop over Ticket list and call calculatePrice() for all.

---

## Set 30

**Q1: Why is validation important when changing object state?**  
A: Validation prevents invalid state.

**Q2: Show one method in your project that changes an object and explain what should be validated.**  
A: Customer.setPhone validates digits/length.

**Q3: Explain where object-level rules should be enforced so the rest of the program stays safe.**  
A: Enforce rules inside object methods for safety.

---

## Set 31

**Q1: What is an exception in Java, and how is it different from a compile-time syntax error?**  
A: Exception = runtime error; syntax error = compile-time.

**Q2: Show one place in your project where invalid data or bad input could cause a runtime exception.**  
A: Parsing numbers in UI can throw NumberFormatException.

**Q3: Explain why exception handling should support good object design, not replace proper validation and class structure.**  
A: Exceptions supplement validation; don’t replace good design.

---

## Set 32

**Q1: What is the purpose of a try-catch block in practical program design?**  
A: try-catch handles errors to avoid crash.

**Q2: Show one place in your project where a try-catch block could prevent the program from crashing.**  
A: DAO methods catch SQLException.

**Q3: Explain why it is bad practice to rely on try-catch everywhere instead of designing objects and validation properly.**  
A: Overusing try-catch hides poor validation.

---

## Set 33

**Q1: What is the difference between preventing an error and catching an error after it happens?**  
A: Preventing error = validate early; catching = handle after.

**Q2: Show one rule in your project that should be checked before the risky operation happens.**  
A: Validate balance before deductBalance.

**Q3: Compare validation through object methods with handling an exception afterward. Which should come first, and why?**  
A: Validation should come first for cleaner design.

---

## Set 34

**Q1: What is variable scope, and what is the difference between a field and a local variable?**  
A: Field = class member; local variable = method-only.

**Q2: Show one method in your project and identify its local variables and class fields.**  
A: In Customer, fields are private; locals in deductBalance.

**Q3: Explain how poor scope decisions can create confusion or bugs in object-oriented code.**  
A: Poor scope causes confusion and bugs.

---

## Set 35

**Q1: Why is naming important in class and method design?**  
A: Good names communicate responsibility.

**Q2: Show one class name and one method name from your project and explain why they are appropriate or not.**  
A: MovieDao clearly signals DB access for Movie.

**Q3: Explain how poor naming can confuse object responsibility, inheritance meaning, or class interaction.**  
A: Bad names hide intent and confuse collaboration.

---

## Set 36

**Q1: What is the role of access modifiers like private, protected, and public in OOP?**  
A: Access modifiers control visibility.

**Q2: Show one class in your project and explain why some members should not all have the same access level.**  
A: Customer fields are private to protect data.

**Q3: Explain how poor access control can
