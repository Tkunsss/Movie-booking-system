# OOP Exam Bank - Project Answers With Paths

This README lists answers for Sets 1-50 and shows a concrete file path from this project for each question.

## Set 1
Q1: What is the difference between a class and an object, and why do objects need both fields and methods?
A: A class is a blueprint; an object is a real instance. Fields store state and methods provide behavior to change or use that state. (Path example: src/service/Customer.java)
Q2: In your project, show one class and explain its responsibility, its important fields, and one method that changes the object?s state.
A: Customer manages customer data and balance. Important fields: customerId, fullName, balance. Method deductBalance changes the balance. (Path: src/service/Customer.java)
Q3: In your project, explain why this part should be modeled using objects instead of writing everything in one long main() method or using many unrelated variables.
A: Orders, carts, and customers are separate objects so data and behavior stay together and the main flow stays readable. (Path: src/model/Order.java)

## Set 2
Q1: What is the difference between an object?s state and behavior, and how do fields and methods represent them?
A: State is stored data; behavior is what the object can do. Fields represent state; methods represent behavior. (Path example: src/service/Customer.java)
Q2: Show one object from your project and explain its state and behavior clearly.
A: Cart state is its list of Ticket items; behavior includes calculateSubtotal and calculateTax. (Path: src/service/Cart.java)
Q3: Explain why keeping state and behavior together inside the same class is better than storing data in one place and controlling it from unrelated code.
A: It prevents mismatches and keeps logic close to the data it affects, improving maintainability. (Path: src/service/Cart.java)

## Set 3
Q1: What is a constructor, and how is it different from a normal method?
A: A constructor runs once at object creation to initialize fields; normal methods run anytime after creation. (Path example: src/model/Movie.java)
Q2: Show one constructor in your project and explain what it initializes immediately.
A: The Movie constructor sets id, title, price, and releaseDate immediately. (Path: src/model/Movie.java)
Q3: Explain what design or runtime problems could happen if objects in your project are created without proper initialization.
A: Tickets without a Movie or seat cause null access or wrong pricing. (Path: src/service/Ticket.java)

## Set 4
Q1: What is the purpose of the this keyword, and when is it especially useful?
A: this refers to the current object; it is useful when parameter names match field names. (Path example: src/service/CashierStaff.java)
Q2: Show one constructor or setter in your project where this helps distinguish a field from a parameter.
A: Staff constructors call setters where this.field is used to assign values. (Path: src/service/ManagerStaff.java)
Q3: Explain what happens if a programmer forgets to use this when parameter names and field names are the same.
A: The assignment updates only the parameter, leaving the field unchanged. (Path example: src/service/ManagerStaff.java)

## Set 5
Q1: What is encapsulation, and how is it related to private and public?
A: Encapsulation hides internal data with private fields and exposes controlled access via public methods. (Path: src/service/Customer.java)
Q2: Show one example in your project where a field is hidden and controlled through methods.
A: Customer balance is private and changed through addBalance and deductBalance. (Path: src/service/Customer.java)
Q3: If all fields in your project were made public, what correctness or design problems could happen?
A: Any code could set invalid phone numbers or negative balances, breaking rules. (Path: src/service/Customer.java)

## Set 6
Q1: Why do programmers use getters and setters, and how is that connected to encapsulation?
A: They control and validate access to fields, enforcing rules. (Path example: src/service/Customer.java)
Q2: Show one getter or setter in your project and explain what control it gives you.
A: setPhone validates digits and length before storing. (Path: src/service/Customer.java)
Q3: Explain when a setter should not exist, or when direct modification of data would be dangerous in your project.
A: A direct setBalance could skip validation and allow negative money. (Path: src/service/Customer.java)

## Set 7
Q1: What is the difference between a primitive value and a reference type in Java?
A: Primitive stores a value directly; reference stores a pointer to an object. (Path example: src/service/Cart.java)
Q2: Explain what happens when two variables refer to the same object.
A: Changes via one reference affect the same object seen by the other. (Path example: src/model/Order.java)
Q3: Show how shared references could create unexpected side effects in a project if the programmer is careless.
A: If two orders share the same Cart, changing items changes both orders. (Path: src/model/Order.java)

## Set 8
Q1: What is the difference between passing a primitive value and passing an object reference to a method?
A: Primitive is copied; object reference points to the same object so methods can change it. (Path example: src/App.java)
Q2: Show one method in your project that receives an object as a parameter and explain why.
A: processPayment receives Customer and Cart to update balance and generate receipt. (Path: src/App.java)
Q3: Explain how object references allow different classes to collaborate on the same data without copying everything.
A: DAO updates use the same Customer object without duplicating it. (Path: src/App.java)

## Set 9
Q1: What is inheritance, and how is it different from copying code into multiple classes?
A: Inheritance shares behavior from a parent class; copying duplicates code and is harder to maintain. (Path: src/service/Ticket.java)
Q2: Show one parent-child relationship in your project and explain what the child gets from the parent.
A: StandardTicket extends Ticket and inherits movie, seatNumber, and base methods. (Path: src/service/StandardTicket.java)
Q3: Explain why that relationship is a real is-a relationship and not just code reuse.
A: A StandardTicket is a Ticket with specialized pricing and type. (Path: src/service/StandardTicket.java)

## Set 10
Q1: What is the purpose of the super keyword, and how is it related to inheritance?
A: super calls the parent constructor or method to initialize inherited parts. (Path example: src/service/StandardTicket.java)
Q2: Show where super could be used in your project, either in a constructor or in an overridden method.
A: StandardTicket and PremiumTicket constructors call super(movie, seatNumber). (Path: src/service/StandardTicket.java)
Q3: Explain what may go wrong if a child class does not properly initialize the parent part of the object.
A: Parent fields like movie or seat could be null or invalid, causing errors. (Path: src/service/Ticket.java)

## Set 11
Q1: What is method overriding, and how is it related to inheritance?
A: A child class replaces a parent method with a specific version. (Path: src/service/PremiumTicket.java)
Q2: Show one overridden method in your project and explain why the child version is different from the parent version.
A: PremiumTicket overrides calculatePrice to add VIP extra charge. (Path: src/service/PremiumTicket.java)
Q3: Explain how overriding helps you avoid writing large if-else blocks based on object type.
A: Polymorphism lets each subclass handle its pricing without if-else. (Path: src/service/Cart.java)

## Set 12
Q1: What is polymorphism in your own words?
A: One reference type can point to different object types and call the right behavior. (Path example: src/service/Cart.java)
Q2: Show one example from your project where one reference can work with different object types.
A: List<Ticket> in Cart stores StandardTicket and PremiumTicket. (Path: src/service/Cart.java)
Q3: Explain how polymorphism makes your design easier to extend when new child classes are added.
A: New Ticket types work in Cart without changing Cart code. (Path: src/service/Cart.java)

## Set 13
Q1: What is the difference between a variable?s reference type and the real object type it points to at runtime?
A: Reference type is compile-time; runtime type is the actual object created. (Path example: src/App.java)
Q2: Show one example in your project where the reference type is more general than the created object.
A: Ticket ticket = new PremiumTicket(...). (Path: src/App.java)
Q3: Explain how Java decides which overridden method to call at runtime.
A: Java uses the runtime object type (dynamic dispatch). (Path example: src/service/Cart.java)

## Set 14
Q1: What is an interface, and why is it often described as a contract?
A: An interface defines required methods so implementers promise that behavior. (Path: src/service/istaff.java)
Q2: Show one interface from your project and explain the behavior it promises.
A: istaff promises login info, can(), and identity methods. (Path: src/service/istaff.java)
Q3: Explain why using an interface is better than depending directly on one concrete class in that situation.
A: App can accept any staff type without changing logic. (Path: src/App.java)

## Set 15
Q1: How are interface and polymorphism connected?
A: Interfaces allow multiple classes to be treated as the same type. (Path example: src/service/istaff.java)
Q2: Show how one interface in your project could allow multiple implementations.
A: List<istaff> holds ManagerStaff, CashierStaff, Ticketing_Agent. (Path: src/App.java)
Q3: Explain how this design helps future extension without changing too much old code.
A: Adding a new staff type only requires implementing istaff. (Path: src/service/istaff.java)

## Set 16
Q1: What is an abstract class, and why can it not be used to create direct objects?
A: Abstract classes are incomplete and cannot be instantiated. (Path suggestion: src/service/Ticket.java)
Q2: Show one place in your project where an abstract class would make sense.
A: Ticket could be abstract to force subclasses for pricing. (Path: src/service/Ticket.java)
Q3: Explain why making that class abstract is better than making it a normal concrete class.
A: It prevents creating generic Ticket without a specific type. (Path: src/service/Ticket.java)

## Set 17
Q1: What is an abstract method, and why does it have no body?
A: It has no body because subclasses must implement it. (Path suggestion: src/service/Ticket.java)
Q2: Show one behavior in your project that different child classes could implement differently.
A: calculatePrice differs for StandardTicket and PremiumTicket. (Path: src/service/Ticket.java)
Q3: Explain how abstract methods help a team keep class design consistent.
A: They force all ticket types to provide required behavior. (Path: src/service/Ticket.java)

## Set 18
Q1: What are two important differences between an abstract class and an interface?
A: Abstract class can hold state and method bodies; interface defines a contract without state. (Path examples: src/service/Ticket.java, src/service/istaff.java)
Q2: Show where your project uses, or should use, an interface or an abstract class.
A: istaff is used as an interface; Ticket could be abstract. (Path: src/service/istaff.java)
Q3: For that case, justify why one is a better design choice than the other.
A: Interface suits staff because many unrelated classes can implement it. (Path: src/service/istaff.java)

## Set 19
Q1: What is a has-a relationship, and how is it different from an is-a relationship?
A: has-a means composition; is-a means inheritance. (Path examples: src/model/Order.java, src/service/StandardTicket.java)
Q2: Show one has-a relationship and one is-a relationship from your project, if possible.
A: Order has a Cart; StandardTicket is a Ticket. (Path: src/model/Order.java)
Q3: Explain why choosing the wrong relationship type would make the design weaker or more confusing.
A: Inheritance where composition fits can create rigid, misleading hierarchies. (Path example: src/service/Cart.java)

## Set 20
Q1: What is composition, and how is it different from inheritance?
A: Composition builds classes using other objects; inheritance builds on a parent class. (Path examples: src/model/Order.java, src/service/Ticket.java)
Q2: Show one place in your project where one class contains or uses another class.
A: Cart contains List<Ticket>. (Path: src/service/Cart.java)
Q3: Explain why composition is a better choice than inheritance for that case.
A: Cart is not a Ticket; it just contains tickets. (Path: src/service/Cart.java)

## Set 21
Q1: What does it mean for two objects to collaborate without one inheriting from the other?
A: They work together by using each other?s methods without parent-child relation. (Path example: src/App.java)
Q2: Show two classes in your project that work together and explain their relationship.
A: App uses OrderDao to store Order objects. (Path: src/App.java)
Q3: Explain why distributing responsibility across collaborating classes makes the system easier to maintain.
A: Each class stays focused, so changes are localized. (Path: src/db/OrderDao.java)

## Set 22
Q1: Why is it useful for a class to have one clear responsibility?
A: Single responsibility makes code easier to understand and test. (Path example: src/db/MovieDao.java)
Q2: Show one class in your project and explain its main responsibility.
A: MovieDao handles movie database operations only. (Path: src/db/MovieDao.java)
Q3: Identify one class that may currently do too much and explain how you would redesign it.
A: App.java handles UI, flow, and logic; split into controllers/services. (Path: src/App.java)

## Set 23
Q1: What is cohesion, and how is it related to good class design?
A: Cohesion means a class focuses on one job; high cohesion is good design. (Path example: src/db/OrderDao.java)
Q2: Show one class in your project that has good cohesion.
A: OrderDao focuses only on order persistence. (Path: src/db/OrderDao.java)
Q3: Explain how weak cohesion can make a class harder to test, understand, or extend.
A: Mixed responsibilities create large, fragile classes. (Path example: src/App.java)

## Set 24
Q1: What does loose coupling mean, and why is it valuable in object-oriented design?
A: Loose coupling means minimal dependencies, so changes are safer. (Path example: src/App.java)
Q2: Identify two parts of your project that should not depend too tightly on each other.
A: UI flow should not depend on DB details; it uses DAOs. (Path: src/App.java)
Q3: Explain how interfaces, better method design, or clearer responsibilities could reduce coupling there.
A: Keeping DAO interfaces stable keeps UI changes minimal. (Path: src/db/Database.java)

## Set 25
Q1: What is the difference between an instance field and a static field?
A: Instance fields belong to each object; static fields are shared. (Path example: src/service/Customer.java)
Q2: Show one place in your project where an instance variable makes more sense than a static variable.
A: Customer.balance must be instance, not shared. (Path: src/service/Customer.java)
Q3: Explain a case where using static incorrectly would cause wrong shared behavior between objects.
A: If balance were static, all customers would share the same money. (Path: src/service/Customer.java)

## Set 26
Q1: What is the difference between an instance method and a static method?
A: Instance methods use object state; static methods do not. (Path example: src/service/Customer.java)
Q2: Show one method in your project that should clearly be instance-based and explain why.
A: Customer.deductBalance must change one customer?s balance. (Path: src/service/Customer.java)
Q3: Explain how a project becomes weaker if too many methods are made static.
A: It becomes procedural and loses encapsulation. (Path example: src/App.java)

## Set 27
Q1: What is method overloading, and how is it different from method overriding?
A: Overloading is same method name with different parameters; overriding replaces a parent method. (Path example: src/service/ReceiptGenerator.java)
Q2: Explain one situation in your project where overloading would be useful.
A: ReceiptGenerator could overload generateReceipt for different inputs. (Path: src/service/ReceiptGenerator.java)
Q3: Compare overloading and overriding in terms of extensibility and design value.
A: Overloading adds convenience; overriding enables polymorphism. (Path example: src/service/PremiumTicket.java)

## Set 28
Q1: What is an ArrayList, and how is it different from a normal array?
A: ArrayList resizes dynamically; arrays are fixed size. (Path example: src/service/Cart.java)
Q2: Show where you used an ArrayList in your project and explain why it was a better choice.
A: Cart uses ArrayList to store tickets as the number changes. (Path: src/service/Cart.java)
Q3: Explain how ArrayList makes object management easier when the number of items can change.
A: Tickets can be added without resizing logic. (Path: src/service/Cart.java)

## Set 29
Q1: Why is it useful to store objects inside a collection of a common type?
A: It allows processing many objects with one loop. (Path example: src/service/Cart.java)
Q2: Show how inheritance or interfaces allow multiple objects to be kept in the same list.
A: List<Ticket> stores StandardTicket and PremiumTicket. (Path: src/service/Cart.java)
Q3: Explain how that design helps when you need to process many similar objects using one loop.
A: You can call calculatePrice on all tickets without type checks. (Path: src/service/Cart.java)

## Set 30
Q1: Why is validation important when changing object state?
A: It prevents invalid values and protects invariants. (Path example: src/service/Customer.java)
Q2: Show one method in your project that changes an object and explain what should be validated.
A: setPhone validates digits and length; setBalance prevents negative values. (Path: src/service/Customer.java)
Q3: Explain where object-level rules should be enforced so the rest of the program stays safe.
A: Inside the class methods that modify the state. (Path: src/service/Customer.java)

## Set 31
Q1: What is an exception in Java, and how is it different from a compile-time syntax error?
A: Exceptions are runtime errors; syntax errors are compile-time and prevent building. (Path example: src/App.java)
Q2: Show one place in your project where invalid data or bad input could cause a runtime exception.
A: Integer parsing can throw NumberFormatException. (Path: src/App.java)
Q3: Explain why exception handling should support good object design, not replace proper validation and class structure.
A: Validation prevents errors earlier; try-catch should not replace design rules. (Path: src/service/Customer.java)

## Set 32
Q1: What is the purpose of a try-catch block in practical program design?
A: It prevents the program from crashing and handles errors gracefully. (Path example: src/db/Database.java)
Q2: Show one place in your project where a try-catch block could prevent the program from crashing.
A: DAO database operations can catch SQL exceptions. (Path: src/db/OrderDao.java)
Q3: Explain why it is bad practice to rely on try-catch everywhere instead of designing objects and validation properly.
A: It hides poor validation and makes behavior unpredictable. (Path: src/service/Customer.java)

## Set 33
Q1: What is the difference between preventing an error and catching an error after it happens?
A: Prevention validates before an action; catching handles errors after they occur. (Path example: src/service/Customer.java)
Q2: Show one rule in your project that should be checked before the risky operation happens.
A: Check balance before deducting for payment. (Path: src/App.java)
Q3: Compare validation through object methods with handling an exception afterward. Which should come first, and why?
A: Validation should come first to keep state correct; exceptions handle unexpected cases. (Path: src/service/Customer.java)

## Set 34
Q1: What is variable scope, and what is the difference between a field and a local variable?
A: Fields belong to the class; local variables live only inside methods. (Path example: src/service/Customer.java)
Q2: Show one method in your project and identify its local variables and class fields.
A: In Customer.deductBalance, balance is a field; amount is a local variable. (Path: src/service/Customer.java)
Q3: Explain how poor scope decisions can create confusion or bugs in object-oriented code.
A: Using locals for shared state or public fields leads to hidden dependencies. (Path example: src/App.java)

## Set 35
Q1: Why is naming important in class and method design?
A: Names communicate responsibility and intent. (Path example: src/db/MovieDao.java)
Q2: Show one class name and one method name from your project and explain why they are appropriate or not.
A: MovieDao clearly indicates DB operations for Movie; deductBalance describes action. (Path: src/service/Customer.java)
Q3: Explain how poor naming can confuse object responsibility, inheritance meaning, or class interaction.
A: Weak names hide purpose and lead to misuse. (Path example: src/service/staff.java)

## Set 36
Q1: What is the role of access modifiers like private, protected, and public in OOP?
A: They control visibility and protect data. (Path example: src/service/Customer.java)
Q2: Show one class in your project and explain why some members should not all have the same access level.
A: Customer fields are private to protect data; methods are public for controlled access. (Path: src/service/Customer.java)
Q3: Explain how poor access control can make inheritance, maintenance, or debugging more difficult.
A: Exposing internals makes changes risky and breaks encapsulation. (Path: src/service/Customer.java)

## Set 37
Q1: What is the benefit of hiding implementation details behind methods?
A: Callers use the method without knowing internal logic, which protects changes. (Path example: src/service/Customer.java)
Q2: Show one example in your project where other classes use a method without needing to know the internal logic.
A: App calls customer.deductBalance without handling balance rules itself. (Path: src/App.java)
Q3: Explain how this kind of encapsulation makes debugging and modification easier later.
A: Changes stay inside the class, reducing ripple effects. (Path: src/service/Customer.java)

## Set 38
Q1: Why is reusability an important goal in OOP?
A: Reusable classes reduce duplication and improve consistency. (Path example: src/service/ReceiptGenerator.java)
Q2: Show one class or method in your project that can be reused in multiple situations.
A: ReceiptGenerator can be used by cashier and customer flows. (Path: src/service/ReceiptGenerator.java)
Q3: Explain what design choices made that reuse possible.
A: It is a focused utility that accepts parameters, not tied to UI. (Path: src/service/ReceiptGenerator.java)

## Set 39
Q1: Why is inheritance not always the best solution in OOP?
A: Inheritance can be rigid and create incorrect is-a relationships. (Path example: src/service/Cart.java)
Q2: Give one example of a situation where inheritance seems possible but may not be the best design.
A: Cart could be forced to extend Ticket, but that would be wrong. (Path: src/service/Cart.java)
Q3: Compare inheritance and composition for that case, and justify which one would be more maintainable.
A: Composition is better because Cart contains tickets instead of pretending to be one. (Path: src/service/Cart.java)

## Set 40
Q1: Why is using many if-else checks on object type often a sign of weak OOP design?
A: It ignores polymorphism and scatters type logic. (Path example: src/App.java)
Q2: Show one place in a project where polymorphism could replace repeated type checking.
A: Ticket pricing uses overriding instead of if-else checks. (Path: src/service/Cart.java)
Q3: Explain how redesigning that part with overriding or interfaces would make the code cleaner.
A: Each class handles its behavior, reducing conditional logic. (Path: src/service/Ticket.java)

## Set 41
Q1: What is the difference between a general class and a specialized subclass?
A: General class defines shared behavior; subclass adds specific behavior. (Path example: src/service/Ticket.java)
Q2: Show one general idea and one specialized idea from your project that fit a parent-child design.
A: Ticket is general; PremiumTicket is specialized. (Path: src/service/PremiumTicket.java)
Q3: Explain how to decide whether specialization should be modeled with inheritance or handled another way.
A: If it is a true is-a and shares behavior, use inheritance; otherwise use composition. (Path example: src/service/Cart.java)

## Set 42
Q1: What does it mean for an object to be responsible for its own behavior?
A: The class that owns the data should implement methods that operate on it. (Path example: src/service/Customer.java)
Q2: Show one method in your project that belongs inside the class because it uses or protects that object?s own data.
A: Customer.deductBalance belongs inside Customer because it protects balance rules. (Path: src/service/Customer.java)
Q3: Explain why moving that behavior outside the class would weaken encapsulation or create duplication.
A: External code would repeat rules and could bypass validation. (Path: src/service/Customer.java)

## Set 43
Q1: What is the relationship between constructors and object validity?
A: Constructors set required fields so objects start in a valid state. (Path example: src/model/Movie.java)
Q2: Explain one important rule that should always be true for an object in your project.
A: Customer balance should never be negative. (Path: src/service/Customer.java)
Q3: Show how constructors and state-changing methods can help guarantee that rule remains true.
A: Constructor and setBalance prevent negative values. (Path: src/service/Customer.java)

## Set 44
Q1: Why is it useful to think about ?who should do this work? when designing OOP classes?
A: It keeps responsibilities clear and avoids bloated classes. (Path example: src/App.java)
Q2: Show one feature in your project and explain which class should own that logic.
A: Receipt creation should be in ReceiptGenerator, not App. (Path: src/service/ReceiptGenerator.java)
Q3: Explain a case where putting the logic in the wrong class would hurt readability, cohesion, or reuse.
A: If App handled receipt writing, it would mix UI and IO logic. (Path: src/App.java)

## Set 45
Q1: Why should object state and behavior stay logically connected?
A: Behavior depends on state; keeping them together prevents mismatches. (Path example: src/service/Customer.java)
Q2: Show one class in your project where fields and methods clearly belong together.
A: Customer fields and balance methods belong together. (Path: src/service/Customer.java)
Q3: Explain what design problem appears if methods change data that belongs conceptually to another class.
A: It creates hidden coupling and breaks encapsulation. (Path: src/App.java)

## Set 46
Q1: What is the difference between designing for current needs and designing for extension?
A: Current needs solve today?s requirements; extension allows future changes with minimal edits. (Path example: src/service/Ticket.java)
Q2: Show one part of your project that may need new object types or new behaviors later.
A: Ticket types may expand beyond Standard and Premium. (Path: src/service/Ticket.java)
Q3: Explain how your current design supports or fails to support that future extension.
A: Polymorphism in Ticket subclasses supports new types with minimal changes. (Path: src/service/Cart.java)

## Set 47
Q1: What is the difference between extending a system and modifying existing code everywhere?
A: Extension adds new classes; modification touches many old files. (Path example: src/service/Ticket.java)
Q2: Show one area in your project where adding a new object type should require minimal changes.
A: New Ticket subclass should work with Cart automatically. (Path: src/service/Cart.java)
Q3: Explain which OOP principles help reduce the need to edit many old classes when a new feature is added.
A: Polymorphism and encapsulation reduce change scope. (Path: src/service/Ticket.java)

## Set 48
Q1: Why is testing easier when classes have clear inputs, outputs, and responsibilities?
A: Clear responsibility makes unit tests simple and focused. (Path example: src/service/Customer.java)
Q2: Show one class or method in your project that would be easy to test and explain why.
A: Customer.deductBalance is easy to test with simple inputs. (Path: src/service/Customer.java)
Q3: Explain how bad coupling or unclear responsibilities make OOP code harder to test and debug.
A: Mixed responsibilities require large setup and make failures harder to isolate. (Path example: src/App.java)

## Set 49
Q1: What are common signs that an OOP design needs refactoring?
A: Very large classes, duplicated logic, or too many responsibilities. (Path example: src/App.java)
Q2: Point to one part of your project that could be improved in structure, naming, access control, or class responsibility.
A: App.java is large and mixes UI and logic. (Path: src/App.java)
Q3: Propose a refactoring for that part and explain why the new design would be better.
A: Split App into role controllers and services to improve cohesion. (Path: src/App.java)

## Set 50
Q1: Define the three main pillars of OOP used in this course: encapsulation, inheritance, and polymorphism.
A: Encapsulation hides data with controlled access; inheritance shares behavior; polymorphism lets one reference work with many types. (Path examples: src/service/Customer.java, src/service/Ticket.java, src/service/Cart.java)
Q2: Point to one concrete example of each of these three pillars in your project.
A: Encapsulation: Customer private fields. Inheritance: StandardTicket extends Ticket. Polymorphism: Cart stores Ticket subclasses. (Path: src/service/Customer.java)
Q3: Evaluate your project design: what is one strong OOP decision you made, and what is one weak part you would improve next?
A: Strong: Ticket polymorphism for pricing. Weak: App.java is too large and should be refactored. (Path: src/service/Ticket.java)
