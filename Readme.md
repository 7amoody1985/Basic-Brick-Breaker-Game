# Brick Breaker Game

A fully-featured Brick Breaker game built with JavaFX. Control a paddle to keep the ball in play, destroy blocks across multiple levels, collect bonuses, and beat your high score — all wrapped in a polished UI with menus, sound, and save/load support.

---

## Features

- **Multiple levels** with increasing difficulty and ball speed
- **Bonus blocks** that drop collectibles for extra lives and score boosts
- **Gold blocks** that trigger a special invincible ball mode
- **Difficulty system** with configurable starting speed
- **Save & load** — progress is saved to your Documents folder
- **Full sound system** — background music and sound effects, both toggleable
- **Pause menu**, settings menu, between-level screen, game over screen, and win screen
- **FPS counter** and controls help overlay
- **Breaker speed boost** — hold Space while moving for faster paddle movement
- **Smooth physics** — consistent ball speed regardless of bounce angle

## Tech Stack

| | |
|---|---|
| **Language** | Java 17 |
| **UI Framework** | JavaFX 17 |
| **Build Tool** | Maven |
| **Testing** | JUnit 5 |

## Architecture

The codebase is split into focused packages:

| Package | Responsibility |
|---|---|
| `game` | Core loop, engine, and application entry point |
| `entities` | Game objects — Ball, Breaker (paddle), Block, Bonus |
| `managers` | Logic for blocks, bonuses, and collision detection |
| `ui` | All UI rendering, score display, and sound |
| `io` | Save/load and serialization |
| `enums` | Shared enumerations (block types, bounce directions) |

## Getting Started

**Prerequisites:** JDK 17+ and Maven 3.x

```bash
# Clone the repository
git clone https://github.com/7amoody1985/COMP2042_CW_hfymd1.git
cd COMP2042_CW_hfymd1

# Run (Maven required)
mvn javafx:run
```

No Maven installed? Use the included wrapper instead:

```bash
# macOS / Linux
./mvnw javafx:run

# Windows
mvnw.cmd javafx:run
```

## Running Tests

```bash
mvn test
```

## Controls

| Key | Action |
|---|---|
| `←` / `→` | Move paddle |
| `Space` (held) | Speed boost while moving |
| `Esc` | Pause |
| `S` | Save game |

## Project Background

This started as a refactor and extension of an existing open-source Brick Breaker codebase. The original code had significant reliability issues — the ball speed varied on every hit, bricks sometimes had no hitbox, and the game would crash when two blocks were destroyed simultaneously.

The project involved fixing over 40 bugs, reworking the physics, and extending the game with a full UI system (menus, sound, animations) and gameplay features. The codebase was also heavily refactored: the original monolithic structure was split into dedicated classes and packages, constants replaced magic numbers, and a proper game loop was implemented.

---

## Changelog

### Bug Fixes & Maintenance

- Fixed ball travelling at a different speed every time it hits the breaker
- Fixed bricks having no hitbox if the ball doesn't hit the paddle first
- Changed exception handling
- Fixed rows not getting filled with blocks
- Added thread synchronization
- Improved GameEngine reliability
- Fixed issues moving to next level at incorrect times
- Fixed block hit detection not registering, especially at high ball speeds
- Fixed crash when 2+ blocks get destroyed at once
- Fixed glitches with "+1" score graphics sometimes getting stuck on screen
- Optimized scene thread for better reliability
- Reworked ball physics — ball now always travels at a consistent speed regardless of angle after hitting the breaker
- Added speed variable to easily adjust starting ball speed
- Fixed breaker sometimes going beyond the scene border
- Improved ball hit detection
- Fixed ball sometimes passing through blocks
- Changed ball speed level increment
- Fixed breaker and ball collision edge detection
- Fixed unequal breaker collision zones
- Fixed OS-level delay causing sluggish initial breaker movement when holding arrow keys
- Fixed save game
- Fixed load game
- Fixed ball being visible in the top-left corner before the game starts
- Fixed ball spawn position at high levels
- Decreased level count to prevent blocks overflowing the screen
- Fixed floating-point error in ball position calculation
- Fixed program not always exiting when the window is closed
- Fixed possible crash when collecting two bonuses simultaneously

### Extensions & New Features

- Changed block spawning — level 1 now starts with 1 row instead of 2
- Smoothed breaker movement
- Ball now always spawns in the centre of the screen (including after each level) rather than at a random position
- Adjusted ball angle calculation when hitting either side of the breaker
- Implemented difficulty system
- Added breaker speed boost (hold Space while moving)
- Changed save game location to the user's Documents folder
- Implemented full UI system

**UI Additions**
- Applied background from an existing but unused image asset
- Added a new background when the gold ball is active
- Added a new start menu
- Restyled existing buttons
- Added heart gain/loss animation effects
- Added hit effect for bonus (chocolate) blocks
- Added hit effect for gold (star) blocks
- Imported and applied a new pixel font (Press Start 2P)
- Changed level label position
- Adjusted animation speeds
- Modified breaker sprite
- Added a custom application icon
- Added FPS counter
- Added exit button
- Added settings menu with sound and music toggles
- Added background music
- Added in-game sound effects
- Changed window title and icon
- Made window non-resizable
- Remade game over screen
- Reworked win screen
- Added fully functional pause menu
- Added between-level transition screen
- Added controls help overlay

### Refactoring

- General code cleanup and typo fixes
- Adjusted access modifiers throughout
- Moved all startup code from `Main` into a dedicated `Game` class
- Created `Ball` class
- Created `BounceDirection` enum
- Created `Breaker` class
- Created `BlockManager` class
- Created `BonusManager` class
- Created `CollisionManager` class
- Created `UI` class
- Created `Sound` class
- Moved save/load logic into `SaveGame` and `LoadSave` classes
- Moved `showGameOver` from `Score` to `UI`
- Removed dead code
- Split classes into packages
- Capitalized all `static final` constants
- Replaced magic values with enums (e.g. block types)
- Extracted long `if` conditions into named boolean-returning methods
