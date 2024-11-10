import java.util.HashMap;
import java.util.Map;

public class ColoredCell implements Cell {
    protected int row;
    protected int col;
    private String color;
    private Map<String, Cell> neighbors;

    public ColoredCell(String color, int row, int col) {
        this.color = color;
        this.neighbors = new HashMap<>();
        this.row = row;
        this.col = col;
    }

    public ColoredCell deepCopy() {
        return new ColoredCell(this.color, this.row, this.col);
    }

    public String getType() {
        return "colored";
    }

//    public Cell clone() {
//        return new ColoredCell(this.color, this.row, this.col);
//    }

    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    @Override
    public String display() {
        return color;
    }

    @Override
    public boolean isMovable() {
        return true;
    }

    @Override
    public void setNeighbor(String direction, Cell neighbor) {
        neighbors.put(direction, neighbor);
    }

    @Override
    public Cell getNeighbor(String direction) {
        return neighbors.get(direction);
    }

    // Merge logic when colliding with another `ColoredCell` of the same color
    public void mergeWith(ColoredCell other) {
        // System.out.println("Merging two " + color + " cells");
        // Define merge behavior here, e.g., removing `other` or incrementing a count.
    }

    public String getColor() {
        return color;
    }

    public String getPosition() {
        return row + "," + col;
    }
}
