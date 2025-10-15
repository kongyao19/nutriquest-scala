# NutriQuest 🍎

A 2D educational game built with Scala 3 and ScalaFX that promotes nutrition literacy through interactive gameplay. Collect healthy foods to earn points while avoiding junk food, learning about nutritional categories in the process.

## Features

- **Dynamic Food Collection**: Collect healthy foods from various nutritional categories (Fruits, Vegetables, Proteins, Grains, Dairy)
- **Real-time Scoring**: Points awarded/deducted based on food type
- **Time Challenge**: Race against the clock with visual countdown indicators
- **Smooth Controls**: Responsive keyboard input (Arrow keys or WASD)
- **Pause Functionality**: Pause/resume gameplay at any time (Spacebar)
- **Persistent Leaderboard**: Top 10 scores saved with player names and timestamps
- **Intuitive Menu System**: Easy navigation with FXML-based UI
- **Database Integration**: Embedded Apache Derby for score persistence

## Tech Stack

- **Language**: Scala 3
- **UI Framework**: ScalaFX + JavaFX 21
- **Database**: Apache Derby (embedded)
- **ORM**: ScalikeJDBC
- **Build Tool**: SBT
- **Core Concepts Applied**:
  - Object-oriented design (inheritance, polymorphism, encapsulation, abstraction)
  - MVC architecture pattern
  - Traits and type-safe enumerations
  - Generics and type safety

## How to Run

### Prerequisites
Ensure you have the following installed:
- Java Development Kit (JDK) 11 or higher
- Scala 3.x
- SBT (Scala Build Tool)

Check if SBT is installed:
```bash
sbt --version
```

### Clone the repository:
```bash
git clone https://github.com/kongyao19/nutriquest.git
cd nutriquest
```

### Method 1: Run with SBT
```bash
sbt run
```

### Method 2: Build and Run JAR
```bash
sbt assembly
java -jar target/scala-3.x/nutriquest-assembly.jar
```

## How to Play

### Objective
Collect as many healthy foods as possible before time runs out while avoiding junk food!

### Controls
- **Arrow Keys / WASD**: Move player
- **Spacebar**: Pause/Resume
- **ESC**: Quit to menu

### Scoring
- **Healthy Foods**: +10 to +50 points (varies by type)
- **Junk Foods**: -20 points
- **Bonus**: Collect from all 5 nutritional categories for extra points

### Game Rules
1. Navigate the game area and collect healthy foods
2. Avoid junk foods that deduct points
3. Watch the timer - it changes color as time runs low (green → orange → red)
4. Submit your name at game over to save your score
5. Try to beat the top 10 leaderboard!

## Learning Outcomes

This project demonstrates practical application of Object-Oriented Programming concepts in Scala:

- **Inheritance**: Abstract `GameEntity` base class extended by `Player`, `HealthyFood`, and `UnhealthyFood`
- **Polymorphism**: Runtime method dispatch for `onCollect()` behavior across different food types
- **Encapsulation**: Database operations confined to utility object, controlled field access
- **Abstraction**: Clear separation between Model, View, and Controller layers
- **Traits**: `Movable` trait mixed into `Player` class for modular behavior
- **Type Safety**: Leveraging Scala's generics with collections and JavaFX components

The challenges faced during development, particularly with scene transitions and collision detection, provided valuable experience in software architecture and game development.

## Credits

**Developer**: Kong Yao (Student ID: 22051239)  
**Institution**: Sunway University, Faculty of Engineering and Technology, BSc (Hons) Computer Science  
**Course**: PRG2104 - Object Oriented Programming  
**Academic Session**: April 2025

---

*This project was developed as part of an object oriented programming course, showcasing the practical application of OOP principles in game development.*
