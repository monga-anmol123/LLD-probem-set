# LLD Problem Statements - Complete Collection

This folder contains **30 Low-Level Design problems** organized sequentially for interview preparation.

## Folder Structure

Each problem folder contains:
- **README.md** - Problem statement with requirements
- **SOLUTION.md** - Design patterns and implementation details
- **src/** - Complete Java implementation

```
01-Parking-Lot/
├── README.md       # Problem statement
├── SOLUTION.md     # Design patterns & explanations
└── src/            # Java implementation
    ├── enums/
    ├── model/
    ├── service/
    ├── strategy/
    └── Main.java
```

---

## Problem List (Sequential Order)

### Week 1: Foundations (Problems 01-06)
1. **[Parking Lot](01-Parking-Lot/)** - Singleton, Factory, Strategy
2. **[Library Management](02-Library-Management/)** - Observer, Strategy, State
3. **[ATM System](03-ATM-System/)** - State, Factory, Singleton
4. **[Vending Machine](04-Vending-Machine/)** - State, Factory, Strategy
5. **[Coffee Machine](05-Coffee-Machine/)** - Builder, Factory, Strategy
6. **[Deck of Cards](06-Deck-of-Cards/)** - Factory, Template Method

### Week 2: Intermediate Part 1 (Problems 07-14)
7. **[Traffic Signal](07-Traffic-Signal/)** - State, Observer
8. **[Banking System](08-Banking-System/)** - Strategy, Observer, Factory
9. **[Hotel Booking](09-Hotel-Booking/)** - Builder, Strategy, Observer
10. **[Movie Ticket Booking](10-Movie-Ticket-Booking/)** - Factory, Strategy, Observer
11. **[Restaurant Management](11-Restaurant-Management/)** - Observer, Strategy, Factory
12. **[Car Rental](12-Car-Rental/)** - Factory, Strategy, Observer
13. **[Snake and Ladder](13-Snake-And-Ladder/)** - Factory, Strategy
14. **[Elevator System](14-Elevator-System/)** - Strategy, State

### Week 3: Intermediate Part 2 (Problems 15-22)
15. **[Meeting Scheduler](15-Meeting-Scheduler/)** - Strategy, Observer
16. **[Ride Sharing](16-Ride-Sharing/)** - Factory, Strategy, Observer
17. **[Food Delivery](17-Food-Delivery/)** - Factory, Strategy, Observer
18. **[Shopping Cart](18-Shopping-Cart/)** - Strategy, Decorator, Observer
19. **[Splitwise](19-Splitwise/)** - Factory, Strategy, Observer
20. **[Twitter Feed](20-Twitter-Feed/)** - Observer, Factory, Strategy
21. **[Chess Game](21-Chess-Game/)** - Factory, Strategy, State, Command
22. **[Stock Trading](22-Stock-Trading/)** - Observer, Strategy, Singleton

### Week 4: Advanced & Algorithms (Problems 23-30)
23. **[LRU Cache](23-LRU-Cache/)** - Strategy, Decorator
24. **[Logging Framework](24-Logging-Framework/)** - Singleton, Factory, Strategy
25. **[File Storage](25-File-Storage/)** - Strategy, Composite, Factory
26. **[API Rate Limiter](26-API-Rate-Limiter/)** - Strategy, Factory
27. **[URL Shortener](27-URL-Shortener/)** - Strategy, Factory
28. **[Notification Service](28-Notification-Service/)** - Observer, Strategy, Factory
29. **[Task Scheduler](29-Task-Scheduler/)** - Observer, Strategy, Factory
30. **[Distributed Cache](30-Distributed-Cache/)** - Strategy, Singleton, Factory

---

## How to Use

### For Learning:
1. Read **README.md** first - understand the problem
2. Try to design it yourself (use LLD-1 framework from `/LLD/` folder)
3. Read **SOLUTION.md** - understand design patterns used
4. Study the **src/** implementation
5. Try to code it from scratch

### For Practice:
1. Time yourself (45-60 minutes per problem)
2. Code without looking at solution
3. Compare your design with SOLUTION.md
4. Identify gaps in your approach

### For Interview Prep:
- **Easy (01-06):** 45 minutes
- **Medium (07-22):** 45-60 minutes  
- **Hard (23-30):** 60 minutes

---

## Compiling & Running

Each problem can be compiled and run independently:

```bash
cd 01-Parking-Lot/src
javac enums/*.java model/*.java service/*.java strategy/*.java factory/*.java Main.java
java Main
```

Or use your IDE (IntelliJ IDEA, VS Code, Eclipse) to open and run.

---

## Design Patterns Coverage

| Pattern | Problems |
|---------|----------|
| **Singleton** | 01, 03, 22, 24, 30 |
| **Factory** | 01, 02, 03, 04, 05, 06, 08, 10, 11, 12, 16, 17, 19, 20, 21, 24, 25, 26, 27, 28, 29, 30 |
| **Strategy** | 01, 02, 04, 05, 08, 09, 10, 11, 12, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30 |
| **Observer** | 02, 07, 08, 09, 10, 11, 12, 15, 16, 17, 18, 19, 20, 22, 28, 29 |
| **State** | 02, 03, 04, 07, 14, 21 |
| **Builder** | 05, 09 |
| **Decorator** | 18, 23 |
| **Template Method** | 06 |
| **Command** | 21 |
| **Composite** | 25 |

---

## Problem Categories

### By Domain
- **E-Commerce:** 18 (Shopping Cart)
- **Transportation:** 01 (Parking), 12 (Car Rental), 16 (Ride Sharing), 17 (Food Delivery)
- **Finance:** 08 (Banking), 19 (Splitwise), 22 (Stock Trading)
- **Entertainment:** 06 (Cards), 10 (Movies), 13 (Snake & Ladder), 21 (Chess)
- **Hospitality:** 09 (Hotel), 11 (Restaurant), 05 (Coffee)
- **Systems:** 03 (ATM), 04 (Vending), 07 (Traffic), 14 (Elevator)
- **Software:** 24 (Logging), 25 (File Storage), 26 (Rate Limiter), 27 (URL Shortener), 28 (Notifications), 29 (Task Scheduler), 30 (Cache)

### By Difficulty
- **Easy:** 01-06 (Foundations)
- **Medium:** 07-22 (Real-world systems)
- **Hard:** 23-30 (Algorithms + Design)



## Contributing

Found an issue or have a better solution? 
- Open an issue
- Submit a pull request
- Suggest improvements



*Last Update: 20th January 2026  By Anmol Monga*
