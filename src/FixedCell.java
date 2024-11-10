import java.util.HashMap;
import java.util.Map;

public class FixedCell implements Cell {
    protected int row;
    protected int col;
    private Map<String, Cell> neighbors = new HashMap<>();

    public FixedCell(int row, int col) {
        this.row = row;
        this.col = col;
    }

//    public Cell clone() {
//        return new FixedCell(this.row, this.col);
//    }

    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public FixedCell deepCopy() {
        return new FixedCell(this.row, this.col);
    }

    public String getType() {
        return "fixed";
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    @Override
    public String display() {
        return "#";
    }

    @Override
    public boolean isMovable() {
        return false;
    }

    @Override
    public void setNeighbor(String direction, Cell neighbor) {
        neighbors.put(direction, neighbor);
    }

    @Override
    public Cell getNeighbor(String direction) {
        return neighbors.get(direction);
    }

    public String getPosition() {
        return row + "," + col;
    }
}
