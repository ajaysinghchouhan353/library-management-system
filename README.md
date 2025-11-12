# Library Management System (in-memory)

This project is a simple in-memory Library Management System implemented in Java to demonstrate OOP, SOLID principles, and common design patterns.

Features
- Book management: add, remove, update, search by title/author/ISBN
- Patron management: register patrons and track borrowing history
- Lending: checkout and return books
- Reservation/Notification: Observer pattern used to notify patrons when reserved books are available
- Factory pattern: `LibraryFactory` to create domain objects
- Recommendation: Strategy pattern with a simple recommender implementation

How to run
1. Build with Maven (Java 17):

```powershell
mvn -q -DskipTests package
java -cp target\LibraryManagementSystem-1.0-SNAPSHOT.jar org.example.Main
```

Design notes
- Packages:
  - `org.example.model` — domain classes (`Book`, `Patron`, `Loan`)
  - `org.example.service` — `Library` service that orchestrates operations
  - `org.example.observer` — reservation manager and observer interface
  - `org.example.factory` — simple factory for creating `Book` and `Patron`
  - `org.example.recommendation` — strategy interface and simple implementation

Applied patterns
- Factory: `LibraryFactory` centralizes creation of `Book` and `Patron` objects.
- Observer: `ReservationManager` queues `BookObserver` instances and notifies next observer
- Strategy: Recommendation strategies implement `RecommendationStrategy` (a simple implementation included).

## Class Diagram

![Library Management System Class Diagram](diagrams/class_diagram.png)

The class diagram above shows the relationships between the core classes:
- **Book, Patron, Loan** — model classes representing domain entities
- **Library** — central service managing books, patrons, and loans
- **ReservationManager** — implements the Observer pattern for reservations and notifications
- **LibraryFactory** — creates Book and Patron instances (Factory pattern)
- **RecommendationStrategy** — interface for book recommendation logic (Strategy pattern)

For a higher-quality rendered version, you can generate a PNG from the PlantUML source:
- PlantUML source: `diagrams/class_diagram.puml`
- SVG version: `diagrams/class_diagram.svg`
- To generate PNG: `python svg_to_png.py` (requires cairosvg, ImageMagick, or Inkscape)

## Notes on SOLID
- Single Responsibility: Models only represent data; `Library` contains business logic; `ReservationManager` manages reservations.
- Open/Closed: New recommendation strategies can be added without modifying `Library`.
- Liskov/Interface Segregation/Dependency Inversion: `RecommendationStrategy` and `BookObserver` are small interfaces that `Library` depends on by abstraction.

Next steps / Optional extensions
- Multi-branch support with `Branch` objects and transfer operations
- Persistence (database) and REST API
- More sophisticated recommender (collaborative filtering)

License
This code is provided as-is for learning and demonstration purposes.
