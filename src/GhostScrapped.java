import java.awt.*;
import java.util.*;
import java.util.List;

public class GhostScrapped {
    private Point position;
    private Point targetPosition;
    private List<Point> path;
    private int speed = 1;

    private int map_width;

    private int map_height;

    private int[][] map;

    private Color color;

    private int size;
    public GhostScrapped(int x, int y, Color color, int[][]map) {
        position = new Point(x, y);
        targetPosition = null;
        path = new ArrayList<>();
        this.map_width = map[0].length;
        this.map_height = map.length;
        this.map = map;
        this.color = color;
        this.size = 20;
    }

    public void draw(Graphics2D g2d){
        int size = 20;
        // Body
        g2d.setColor(this.color);
        g2d.fillArc(position.x, position.y, size, size, 0, 180);
        g2d.fillRect(position.x, position.y + size/2, size, size/2);
        // eyes
        int eyeSize = size / 3;
        int eyeOffset = size / 5;
        g2d.setColor(Color.WHITE);
        g2d.fillOval(position.x + eyeOffset/2, position.y + eyeOffset, eyeSize, eyeSize);
        g2d.fillOval(position.x + size - eyeOffset/2 - eyeSize, position.y + eyeOffset, eyeSize, eyeSize);
        g2d.setColor(Color.BLACK);
        g2d.fillOval(position.x + eyeOffset/2 + eyeSize / 4, position.y + eyeOffset + eyeSize / 4, eyeSize / 2, eyeSize / 2);
        g2d.fillOval(position.x + size - eyeOffset/2 - eyeSize + eyeSize / 4, position.y + eyeOffset + eyeSize / 4, eyeSize / 2, eyeSize / 2);
    }

    public void setPosition(int x, int y) {
        this.position.x = x;
        this.position.y = y;
    }
    public void setTargetPosition(Point targetPosition) {
        this.targetPosition = targetPosition;
        path = findPath(targetPosition);
    }

    private List<Point> findPath(Point targetPosition) {
        // Perform a breadth-first search (BFS) to find the shortest path
        boolean[][] visited = new boolean[map_height][map_width];
        Queue<Point> queue = new LinkedList<>();
        Map<Point, Point> parentMap = new HashMap<>();

        queue.offer(position);
        visited[position.x/size][position.y/size] = true;

        while (!queue.isEmpty()) {
            Point current = queue.poll();

            // If the current position is the target position, reconstruct the path and return it
            if (current.equals(targetPosition)) {
                return reconstructPath(current, parentMap);
            }

            // Check neighboring positions
            for (Point neighbor : getNeighbors(current)) {
                if (!visited[neighbor.x/size][neighbor.y/size]) {
                    visited[neighbor.x/size][neighbor.y/size] = true;
                    queue.offer(neighbor);
                    parentMap.put(neighbor, current);
                }
            }
        }

        return new ArrayList<>(); // No path found
    }

    private List<Point> reconstructPath(Point current, Map<Point, Point> parentMap) {
        List<Point> path = new ArrayList<>();
        while (parentMap.containsKey(current)) {
            path.add(0, current);
            current = parentMap.get(current);
        }
        return path;
    }

    private List<Point> getNeighbors(Point position) {
        List<Point> neighbors = new ArrayList<>();

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // Up, Down, Left, Right

        for (int[] direction : directions) {
            int dx = direction[0];
            int dy = direction[1];
            int newX = position.x + dx;
            int newY = position.y + dy;

            // Check if the neighboring position is within the map boundaries and not a wall
            if (newX >= 0 && newX < map_width && newY >= 0 && newY < map_height
                    && map[newX][newY] <= 1) {
                neighbors.add(new Point(newX, newY));
            }
        }

        return neighbors;
    }

    private Point getPreviousPosition(Point position, boolean[][] visited) {
        // Find the previous position in the path by checking neighboring positions
        for (Point neighbor : getNeighbors(position)) {
            if (visited[neighbor.x][neighbor.y]) {
                return neighbor;
            }
        }

        return null;
    }

    public void move() {
        // If the path is empty, stop moving
        if (path.isEmpty()) {
            return;
        }

        // Move towards the next position in the path
        Point nextPosition = path.get(0);
        int dx = Integer.compare(nextPosition.x, position.x);
        int dy = Integer.compare(nextPosition.y, position.y);

        position.translate(dx * speed, dy * speed);

        // Check if the ghost has reached
    }
}