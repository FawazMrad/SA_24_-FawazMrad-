import java.util.Map;
import java.util.HashMap;

public interface Cell {
    String display();
    boolean isMovable();  // Indicates if the cell can be moved into by other cells
    void setNeighbor(String direction, Cell neighbor);  // Set neighbor cells
    Cell getNeighbor(String direction);// Get neighbor cell in a specific direction
    public String getPosition();
    public void setPosition(int row, int col);
    public int getRow();
    public int getCol();
    public String getType();
    //Cell clone();
    public  Cell deepCopy();
    }
