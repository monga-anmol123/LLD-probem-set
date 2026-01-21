# Problem 6: Deck of Cards

## 📋 Problem Statement

Design a **Deck of Cards** system that models a standard 52-card deck with operations like shuffle, deal, and reset. The system should support multiple players, custom decks (multiple decks combined into a "shoe"), and be extensible to implement various card games like Blackjack.

**Difficulty:** ⭐⭐⭐ Medium

**Interview Frequency:** High (Asked at Amazon, Microsoft, Google, Facebook)

---

## 🎯 Functional Requirements

### Core Features

1. **Card Representation**
   - Immutable Card class
   - Suit: Hearts (♥), Diamonds (♦), Clubs (♣), Spades (♠)
   - Rank: A, 2-10, J, Q, K
   - Value calculation (for games like Blackjack)

2. **Deck Operations**
   - Create standard 52-card deck
   - Shuffle deck (Fisher-Yates algorithm)
   - Deal single card
   - Deal multiple cards
   - Reset deck to original state
   - Check remaining cards

3. **Hand Management**
   - Player hand to hold cards
   - Add cards to hand
   - Calculate hand value
   - Display hand
   - Clear hand

4. **Multiple Decks (Shoe)**
   - Combine multiple decks
   - Common in casino games
   - Support 1-8 decks

5. **Game Implementation**
   - Implement Blackjack game
   - Support multiple players
   - Dealer logic
   - Win/loss determination
   - Betting system (optional)

---

## 🚫 Non-Functional Requirements

1. **Immutability**
   - Card objects should be immutable
   - Thread-safe by design

2. **Efficiency**
   - Shuffle in O(n) time
   - Deal in O(1) time
   - Memory efficient

3. **Extensibility**
   - Easy to add new games
   - Support custom card values
   - Support special cards (Jokers)

4. **Usability**
   - Clear card representation (e.g., "A♠", "K♥")
   - Intuitive API
   - Good error handling

---

## 🎨 Design Patterns to Use

1. **Factory Pattern** - Create different types of decks (standard, with jokers, multiple decks)
2. **Template Method Pattern** - Abstract game logic with concrete implementations
3. **Singleton Pattern** (Optional) - Card instances can be cached

---

## 💡 Hints & Approach

### Class Structure

```
06-Deck-Of-Cards/
├── enums/
│   ├── Suit.java              # HEARTS, DIAMONDS, CLUBS, SPADES
│   └── Rank.java              # ACE, TWO, ..., KING
├── model/
│   ├── Card.java              # Immutable card
│   ├── Deck.java              # Standard deck with operations
│   ├── Hand.java              # Player's hand
│   └── Player.java            # Player with hand and name
├── factory/
│   └── DeckFactory.java       # Create different deck types
├── game/
│   ├── Game.java              # Abstract game template
│   └── BlackjackGame.java     # Concrete Blackjack implementation
└── Main.java                  # Demo with 6+ scenarios
```

### Key Algorithms

1. **Fisher-Yates Shuffle**
```
for i from n-1 down to 1:
    j = random integer from 0 to i
    swap cards[i] with cards[j]
```
- Time: O(n)
- Space: O(1)
- Unbiased shuffle

2. **Blackjack Hand Value**
```
sum = 0
aces = 0
for each card:
    if card is Ace: aces++, sum += 11
    else if card is face card: sum += 10
    else: sum += card rank value
    
while sum > 21 and aces > 0:
    sum -= 10  // Count ace as 1 instead of 11
    aces--
```

### Edge Cases to Handle

- Empty deck (cannot deal)
- Dealing more cards than available
- Blackjack with multiple aces
- Resetting shuffled deck
- Multiple players with same hand value

---

## 🧪 Test Scenarios

### Scenario 1: Basic Deck Operations
```
- Create standard 52-card deck
- Display all cards
- Shuffle deck
- Deal 5 cards
- Show remaining cards (47)
- Reset deck
```

### Scenario 2: Fisher-Yates Shuffle Verification
```
- Create deck
- Shuffle multiple times
- Verify randomness (cards in different positions)
- Verify all cards present after shuffle
```

### Scenario 3: Multiple Players
```
- Create deck
- Deal 5 cards to each of 4 players
- Display each player's hand
- Verify 32 cards remaining
```

### Scenario 4: Shoe (Multiple Decks)
```
- Create shoe with 6 decks (312 cards)
- Shuffle shoe
- Deal cards
- Verify card count
```

### Scenario 5: Blackjack Game - Player Wins
```
- Player gets 21 (Blackjack)
- Dealer gets 19
- Player wins
```

### Scenario 6: Blackjack Game - Dealer Wins
```
- Player busts (goes over 21)
- Dealer wins without playing
```

### Scenario 7: Blackjack Game - Push (Tie)
```
- Both player and dealer get 20
- Push - bet returned
```

### Scenario 8: Blackjack with Multiple Aces
```
- Player gets A, A, 9 = 21 (not bust)
- Ace value adjustment (11 → 1)
```

---

## 📊 Expected Output Format

```
================================================================================
  DECK OF CARDS SYSTEM DEMO
================================================================================

--------------------------------------------------------------------------------
SCENARIO 1: Basic Deck Operations
--------------------------------------------------------------------------------

✅ Created standard 52-card deck

📋 All cards in deck:
  A♠ 2♠ 3♠ 4♠ 5♠ 6♠ 7♠ 8♠ 9♠ 10♠ J♠ Q♠ K♠
  A♥ 2♥ 3♥ 4♥ 5♥ 6♥ 7♥ 8♥ 9♥ 10♥ J♥ Q♥ K♥
  A♦ 2♦ 3♦ 4♦ 5♦ 6♦ 7♦ 8♦ 9♦ 10♦ J♦ Q♦ K♦
  A♣ 2♣ 3♣ 4♣ 5♣ 6♣ 7♣ 8♣ 9♣ 10♣ J♣ Q♣ K♣

🔀 Shuffling deck...

🎴 Dealing 5 cards:
  7♦ K♣ 3♠ A♥ 9♣

📊 Remaining cards: 47

🔄 Resetting deck...
📊 Cards in deck: 52

--------------------------------------------------------------------------------
SCENARIO 2: Multiple Players
--------------------------------------------------------------------------------

✅ Dealing to 4 players (5 cards each)...

Player 1: 5♠ J♥ 2♦ 8♣ K♠
Player 2: A♠ 9♥ 4♦ 7♣ Q♠
Player 3: 3♠ 10♥ 6♦ J♣ 2♠
Player 4: 8♠ K♥ 5♦ 9♣ A♦

📊 Remaining cards: 32

--------------------------------------------------------------------------------
SCENARIO 3: Blackjack Game
--------------------------------------------------------------------------------

🎰 Starting Blackjack Game...

Player: [A♠ K♥] = 21 (BLACKJACK!)
Dealer: [10♦ 9♣] = 19

🎉 Player wins with Blackjack!

...
```

---

## 🎓 Learning Objectives

After implementing this problem, you should understand:

1. **Immutability** - Benefits of immutable objects
2. **Fisher-Yates Shuffle** - Unbiased random shuffling
3. **Factory Pattern** - Creating object families
4. **Template Method** - Abstract game logic
5. **Enum Usage** - Type-safe constants
6. **Collections** - List operations and manipulation

---

## 🏢 Real-World Applications

- **Online Casino Games** - Poker, Blackjack, Baccarat
- **Card Game Apps** - Solitaire, Hearts, Bridge
- **Educational Software** - Teaching card games
- **Game Engines** - Card game frameworks
- **Simulation Software** - Probability calculations

---

## 🚀 Extension Ideas

1. **Jokers** - Add joker cards to deck
2. **Wild Cards** - Support wild card rules
3. **Poker Hands** - Detect poker hand rankings
4. **Multiple Games** - Poker, Rummy, Go Fish
5. **Card Counting** - Track dealt cards (Blackjack)
6. **Animations** - Visual card dealing
7. **Network Play** - Multiplayer over network

---

## 📚 Related Problems

- Problem 11: Restaurant Management (State management)
- Problem 29: Task Scheduler (Template method pattern)

---

**Companies that frequently ask this:** Amazon, Microsoft, Google, Facebook, Adobe

**Difficulty Level:** Medium (45-60 minute problem)

**Key Concepts:** Immutability, Factory Pattern, Template Method, Algorithms (Fisher-Yates)
