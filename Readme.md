
# CW Brick Game

Please note that for refactoring and code changes, I cannot include all changes made due to the high number of changes made. I strongly recommend to check out the code.
## Compilation Instructions

To compile and run your JavaFX application, you can follow these steps:

1. Ensure you have the Java Development Kit (JDK) installed on your system. You can check this by running `java -version` in your terminal. If you don't have it installed, you can download it from the official Oracle website.

2. This project uses Maven as its build tool. If you don't have Maven installed, you can download and install it from the official Apache Maven website. After installation, you can verify it by running mvn -version in your terminal.

3. Navigate to the root directory of your project in the terminal.

4. Run the Maven compile command: `mvn compile`. This will compile your source code into the target directory.

5. To run your application, use the Maven exec plugin with the `java` goal: `mvn exec:java -Dexec.mainClass="game.Main"`.
## Maintenance

- Fixed ball different speed everytime ball hits break
- Fixed if ball doesn't hit paddle first, bricks have no hit box
- Changed exception handling
- Fixed rows not getting filled with blocks
- Added some thread synchronization
- Improved GameEngine reliability
- Fixed issues moving to next level at incorrect times
- Fixed block hit detection where sometimes it doesn't register, especially when ball speed is high
- Fixed crash when 2+ blocks get destroyed at once
- Fixed glitches with "+1" score graphics (sometimes gets stuck on screen)
- Optimized scene thread further for better reliability
- Reworked ball physics, now ball always has the same speed no matter than angle after hitting breaker
- Added speed variable to easily adjust starting ball speed
- Fixed breaker sometimes goes beyond scene border
- Improved ball hit detection
- Fixed Ball sometimes goes through block
- Changed ball speed level increment
- Fixed breaker and ball collision edge detection
- Fixed breaker zones not equal
- Fixed delay caused by operating system where if you hold the arrow keys initial breaker movement is delayed
- Fixed save game
- Fixed load game
- Fixed ball visible on top left corner before game starts
- Fixed ball spawn position in high levels
- Decreased level amount due to screen being full at high levels
- Fixed error in int ball location calculation
- Fixed program not always exiting when closing window
- Fixed possible crash when collecting 2 bonuses at once
## Extension

- Changed block spawning behaviour, now level 1 starts with 1 row of blocks instead of 2
- Changed paddle/break behaviour and made movement smoother
- Made ball always spawn in the middle of the screen instead of randomly
- Changed starting ball speed
- Break now is initialized in the middle of the screen
- Ball now spawns in the middle of the screen going straight down, including after each level
- Adjusted angle when ball hits either side of breaker
- Implemented difficulty system
- Added breaker speed control (hold space while moving breaker)
- Changed save game location to user's documents folder
- Implemented full UI system


UI Additions
- Applied background from existing but forgotten image
- Added new background when gold ball is active
- Added new start menu
- Changed existing buttons styling
- Added new effect when losing or gaining hearts
- Added new effect when hitting bonus (choco) block
- Added new effect when hitting gold (start) block
- Imported new font
- Changed global font
- Changed level label position
- Changed old effects speed
- Modified breaker image
- Added new application icon
- Added fps counter
- Added exit button
- Added settings menu
- Added sound toggle button
- Added music toggle button
- Added background music
- Added game sounds
- Changed window title and icon
- Made window not resizable
- Remade game over menu
- Reworked win menu
- Added fully functional pause menu
- Added simple menu between levels
- Added controls help

## Refactoring

- Code clean up
- Fixed typos
- Changed visibilities
- Moved all code in main to new Game class
- Created Ball class
- Created BounceDirection enum
- Created Breaker class
- Created BlockManager class
- Created BonusManager class
- Created CollisionManager class
- Created UI class
- Created Sound class
- Moved all save related code to SaveGame class
- Created LoadSave class
- Moved showGameOver method from Score class to UI class
- Removed unnecessary code
- Split classes into packages
- Capitalized static final variables
- Added enums instead of values (ex: block type)
- Added methods that return boolean value for if statements that are long