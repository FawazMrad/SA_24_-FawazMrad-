import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Grid {
    final String[][] config;
    private Grid currentGrid;
    private int rows, cols;
    private Map<String, Cell> cells;


    public Grid(int rows, int cols, String[][] config) {
        this.config = config;
        this.rows = rows;
        this.cols = cols;
        this.cells = new HashMap<>();
        initializeGrid(config);
        linkNeighbors();

    }


    public int getRows() {
        return this.rows;
    }

    public int getCols() {
        return this.cols;
    }

    public Map<String, Cell> getCells() {
        return this.cells;
    }

    // Initialize cells based on the configuration
    private void initializeGrid(String[][] config) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                String cellType = config[i][j].toLowerCase();
                Cell cell;

                switch (cellType) {
                    case "space":
                        cell = new SpaceCell(i, j);
                        break;
                    case "fixed":
                        cell = new FixedCell(i, j);
                        break;
                    default:
                        cell = new ColoredCell(cellType, i, j);
                        break;
                }

                cells.put(i + "," + j, cell);
            }
        }
    }

    // Link each cell to its neighbors
    public void linkNeighbors() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Cell cell = cells.get(i + "," + j);
                if (cell != null) {
                    cell.setNeighbor("up", cells.get((i - 1) + "," + j));
                    cell.setNeighbor("down", cells.get((i + 1) + "," + j));
                    cell.setNeighbor("left", cells.get(i + "," + (j - 1)));
                    cell.setNeighbor("right", cells.get(i + "," + (j + 1)));
                }
            }
        }
    }

    public Grid deepCopyFirst() {
        Grid copy = new Grid(this.rows, this.cols, this.config);
        for (Map.Entry<String, Cell> entry : this.cells.entrySet()) {
            Cell cell = entry.getValue();
            copy.cells.put(entry.getKey(), cell);
        }
        copy.linkNeighbors();
        return copy;
    }


    public Grid deepCopy() {
        Grid copy = new Grid(this.rows, this.cols, this.config);
        for (Map.Entry<String, Cell> entry : this.cells.entrySet()) {
            Cell cell = entry.getValue();
            copy.cells.put(entry.getKey(), cell.deepCopy());
        }
        copy.linkNeighbors();
        return copy;
    }


    public void printGrid(Grid newCurrentGrid) {
        this.currentGrid = newCurrentGrid;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Cell cell = currentGrid.cells.get(i + "," + j);
                System.out.print(cell.display() + " ");
            }
            System.out.println();
        }
    }
}
