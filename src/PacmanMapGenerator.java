import java.util.Random;

public class PacmanMapGenerator {
    private int width;
    private int height;
    private int[][] maze;

    public PacmanMapGenerator(int width, int height) {
        this.width = width;
        this.height = height;
        this.maze = new int[height][width];
    }

    public void generate() {
        // Initialize maze to all walls
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                maze[i][j] = 1;
            }
        }

        // Generate maze starting at cell (0, 0)
        Random random = new Random();
        generateMaze(random.nextInt(height), random.nextInt(width), random);
    }

    private void generateMaze(int row, int col, Random random) {
        // Mark cell as visited
        maze[row][col] = 0;

        // Get list of unvisited neighbors
        int[][] neighbors = getUnvisitedNeighbors(row, col);
        shuffle(neighbors, random);

        // Visit each unvisited neighbor recursively
        for (int[] neighbor : neighbors) {
            int r = neighbor[0];
            int c = neighbor[1];
            if (maze[r][c] == 1) {
                // Remove wall between current cell and neighbor
                if (r == row) {
                    maze[row][(col + c) / 2] = 0;
                } else {
                    maze[(row + r) / 2][col] = 0;
                }
                generateMaze(r, c, random);
            }
        }
    }

    private int[][] getUnvisitedNeighbors(int row, int col) {
        int[][] neighbors = new int[4][2];
        int count = 0;

        if (row > 1 && maze[row - 2][col] == 1) {
            neighbors[count++] = new int[]{row - 2, col};
        }
        if (row < height - 2 && maze[row + 2][col] == 1) {
            neighbors[count++] = new int[]{row + 2, col};
        }
        if (col > 1 && maze[row][col - 2] == 1) {
            neighbors[count++] = new int[]{row, col - 2};
        }
        if (col < width - 2 && maze[row][col + 2] == 1) {
            neighbors[count++] = new int[]{row, col + 2};
        }

        int[][] result = new int[count][2];
        System.arraycopy(neighbors, 0, result, 0, count);
        return result;
    }

    private void shuffle(int[][] array, Random random) {
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int[] temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    public int[][] getMaze() {
        return maze;
    }
}
