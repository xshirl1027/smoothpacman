# smoothpacman
non-pixelated version of pacman in 600 lines of code. 
this is something I made from scratch, completely rendered in java graphics, no external images used.

Uses:

- Object-Oriented programming
- 2D Array to store data for the map
- Randomness for ghost movements

## Requirements

- OpenJDK 11.0.12 64-Bit (or compatible Java version)
- org.json library (included in `lib/json-20220924.jar`)

## How to Run

### Option 1: Using Command Line

1. Clone or download this repository
2. Navigate to the project directory:

   ```bash
   cd smoothpacman
   ```

3. Compile the Java files with the JSON library in classpath:

   ```bash
   javac -cp "lib/json-20220924.jar" -d out src/*.java
   ```

4. Run the game:

   ```bash
   java -cp "out:src:lib/json-20220924.jar" Main
   ```

### Option 2: Using an IDE

1. Open the project in your preferred Java IDE (IntelliJ IDEA, Eclipse, etc.)
2. Add the `lib/json-20220924.jar` file to your project's classpath/build path
3. Set the `src` folder as the source directory
4. Run the `Main.java` class

### Controls

- Use arrow keys to move Pacman
- Eat all the dots while avoiding the ghosts!

### Notes

- The game window will automatically size based on the map dimensions
- Sound effects are included and should play during gameplay
- All graphics are rendered using Java's built-in graphics capabilities

## Demo

[Game Demo Video](https://github.com/xshirl1027/smoothpacman/assets/12800360/fd5341c2-6855-4c33-8f33-6e7dc59c9307)

Note: the sound and video are not synched in this recording for some reason. In the game they are.

