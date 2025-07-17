# Pepse Game

A Java-based 2D game framework featuring procedural terrain, animated characters, interactive objects, and a real-time day–night cycle.

---

## Overview

Pepse demonstrates core game-development concepts in Java:

- Procedural terrain generation  
- Sprite-based character animation  
- Dynamic day–night cycle  
- Interactive environment (trees grow fruit!)  

---

## Features

- **Procedural Terrain**: Randomized landscapes each playthrough.  
- **Animated Character**: Idle, running, and jumping sprites for smooth motion.  
- **Day–Night Cycle**: Real-time sky and lighting transitions.  
- **Interactive Trees & Fruit**: Trees grow fruit over time; collect them.  
- **Modular Code**: Clean package structure for easy extension.

---

## Requirements

- Java 11 or newer  
- External Library: `DanoGameLab.jar` (add to your IDE’s classpath)

---

## Installation

    git clone https://github.com/your-username/pepse-game.git
    cd pepse-game
    # Place DanoGameLab.jar in the project root or configure in your IDE

---

## How to Play

1. Open the project in IntelliJ IDEA (or your IDE).  
2. Add `DanoGameLab.jar` to the classpath.  
3. Run `PepseGameManager.java`.  
4. Explore the world, jump, collect fruit, and watch the day turn to night!

---

## Project Structure

    pepse/
    ├─ PepseGameManager.java        # Main entry point
    ├─ util/
    │  ├─ NoiseGenerator.java       # Terrain noise logic
    │  └─ ColorSupplier.java        # Color palettes
    └─ world/
       ├─ Sky.java
       ├─ Avatar.java
       ├─ Terrain.java
       ├─ Block.java
       ├─ Constants.java
       ├─ trees/
       │  ├─ Tree.java
       │  ├─ Flora.java
       │  └─ Fruit.java
       └─ daynight/
          ├─ Sun.java
          ├─ SunHalo.java
          ├─ Cloud.java
          └─ Night.java

    assets/
    ├─ run_*.png      # Running animations
    ├─ idle_*.png     # Idle animations
    └─ jump_*.png     # Jumping animations

    README.md         # This file
    LICENSE           # (if included)

---

## License

MIT License – see LICENSE for details.

---

## Acknowledgments

- **DanoGameLab.jar**: Core game framework  
- **Sprite Assets**: Created for academic use  
- Inspired by Java game exercises at Hebrew University of Jerusalem  
