import java.util.*;

public class Game {
    private final LinkedList<Grid> gridHistory;
    private Grid grid;
    private String previousGridState;
    private Grid firstGrid;
    private int numberOfMoves;

    public Game(Grid initialGrid) {
        this.grid = initialGrid;
        this.numberOfMoves = 0;
        this.firstGrid = initialGrid.deepCopyFirst();
        this.gridHistory = new LinkedList<>();
        this.gridHistory.add(this.grid);
        this.previousGridState = getGridHash();
    }

    public void initGame() {
        Scanner scanner = new Scanner(System.in);

        // Step 1: Ask for grid dimensions
        System.out.print("Enter the number of rows: ");
        int rows = scanner.nextInt();
        System.out.print("Enter the number of columns: ");
        int cols = scanner.nextInt();

        // Step 2: Create an empty configuration array based on rows and cols
        String[][] config = new String[rows][cols];

        // Step 3: Ask the user to input the grid configuration row by row
        scanner.nextLine(); // consume the newline after the int input
        for (int i = 0; i < rows; i++) {
            System.out.println("Enter row " + (i + 1) + ": ");
            String rowInput = scanner.nextLine();
            String[] row = rowInput.split("\\s+"); // Split by spaces
            if (row.length == cols) {
                config[i] = row;
            } else {
                System.out.println("Invalid input. The number of columns doesn't match.");
                i--; // Re-enter the same row
            }
        }

        // Step 4: Initialize the grid with the entered configuration
        this.grid = new Grid(rows, cols, config);
        this.firstGrid=this.grid.deepCopyFirst();
        System.out.println("Grid initialized successfully!");
    }


    public Grid getGrid() {
        return this.grid;
    }

    public boolean canMove(String direction, Cell cell) {
        return cell instanceof ColoredCell && cell.isMovable() && cell.getNeighbor(direction) != null && cell.getNeighbor(direction).isMovable();
    }

    public void makeMove(String direction) {
        this.numberOfMoves++;
        gridHistory.add(this.grid.deepCopy());
        while (true) {
            String currentGridState = getGridHash();
            move(direction);
            String newGridState = getGridHash();

            if (newGridState.equals(currentGridState)) {

                break;
            }
            previousGridState = newGridState;  // Update previous state to current
        }

        this.result();

    }

    public void printGridHistory() {
        System.out.println(this.gridHistory.toString());
    }

    public void move(String direction) {
        int startRow = 0, startCol = 0, rowEnd = grid.getRows(), colEnd = grid.getCols();
        int rowStep = 1, colStep = 1;

        switch (direction) {
            case "up":
                startRow = 0;
                rowEnd = grid.getRows();
                rowStep = 1;
                startCol = 0;
                colEnd = grid.getCols();
                colStep = 1;
                break;
            case "down":
                startRow = grid.getRows() - 1;
                rowEnd = -1;
                rowStep = -1;
                startCol = 0;
                colEnd = grid.getCols();
                colStep = 1;
                break;
            case "left":
                startRow = 0;
                rowEnd = grid.getRows();
                rowStep = 1;
                startCol = 0;
                colEnd = grid.getCols();
                colStep = 1;
                break;
            case "right":
                startRow = 0;
                rowEnd = grid.getRows();
                rowStep = 1;
                startCol = grid.getCols() - 1;
                colEnd = -1;
                colStep = -1;
                break;
        }

        for (int i = startRow; i != rowEnd; i += rowStep) {
            for (int j = startCol; j != colEnd; j += colStep) {
                Cell currentCell = grid.getCells().get(i + "," + j);
                if (canMove(direction, currentCell)) {
                    Cell neighborCell = currentCell.getNeighbor(direction);
                    moveCell(currentCell, neighborCell);
                }
            }
        }
        grid.linkNeighbors();

    }

    private void moveCell(Cell currentCell, Cell targetCell) {
        if (currentCell instanceof ColoredCell) {
            ColoredCell coloredCell = (ColoredCell) currentCell;

            if (targetCell instanceof SpaceCell) {
                // Swap cells
                grid.getCells().put(targetCell.getPosition(), coloredCell);
                grid.getCells().put(currentCell.getPosition(), new SpaceCell(currentCell.getRow(), currentCell.getCol()));
                coloredCell.setPosition(targetCell.getRow(), targetCell.getCol());
            } else if (targetCell instanceof ColoredCell) {
                ColoredCell neighborColoredCell = (ColoredCell) targetCell;
                if (coloredCell.getColor().equalsIgnoreCase(neighborColoredCell.getColor())) {
                    // Merge cells of the same color
                    neighborColoredCell.mergeWith(coloredCell);
                    grid.getCells().put(currentCell.getPosition(), new SpaceCell(currentCell.getRow(), currentCell.getCol()));
                }
            }
        }


    }

    private String getGridHash() {
        StringBuilder gridState = new StringBuilder();
        for (int i = 0; i < grid.getRows(); i++) {
            for (int j = 0; j < grid.getCols(); j++) {
                Cell cell = grid.getCells().get(i + "," + j);
                gridState.append(cell.getType()).append(cell.getPosition()).append(";");
            }
        }
        return Integer.toString(Objects.hash(gridState.toString()));
    }


    public void revertToInitialState() {
        this.numberOfMoves+=1;
        this.grid = this.firstGrid.deepCopyFirst();
        this.gridHistory.clear();
        this.gridHistory.add(this.firstGrid.deepCopyFirst());

    }

//    public String[][] gridTo2DArray(Grid grid) {
//        int rows = grid.getRows();
//        int cols = grid.getCols();
//        String[][] gridArray = new String[rows][cols];  // Initialize a 2D array to hold cell types
//
//        for (int i = 0; i < rows; i++) {
//            for (int j = 0; j < cols; j++) {
//                Cell cell = grid.getCells().get(i + "," + j);  // Access cell at (i, j)
//                gridArray[i][j] = cell.getType();  // Use getType() to get the cell's type as a string
//            }
//        }
//
//        return gridArray;
//    }

    public boolean isFinished() {
        Set<String> seenColors = new HashSet<>();

        for (int i = 0; i < this.grid.getRows(); i++) {
            for (int j = 0; j < this.grid.getCols(); j++) {
                Cell cell = this.grid.getCells().get(i + "," + j);

                if (cell instanceof ColoredCell) {
                    ColoredCell coloredCell = (ColoredCell) cell;
                    String color = coloredCell.getColor();

                    if (seenColors.contains(color)) {
                        return false;
                    }
                    seenColors.add(color);
                }
            }
        }

        return seenColors.size() > 1;
    }


    public void result() {
        if (this.numberOfMoves >= 100) {
            System.out.println("You loose , you exceeded the allowed number of moves and your number of moves is: " + this.numberOfMoves);
            this.printCurrentGrid();
            System.out.println();
            System.exit(0);
        }
        if (this.isFinished()) {
            System.out.println("You Win!! and your number of moves is: " + this.numberOfMoves);
            this.printCurrentGrid();
            System.out.println();
            System.exit(0);
        }

    }

    public void undo() {
        if (this.gridHistory.size() > 1) {
            this.numberOfMoves++;
            this.grid = gridHistory.pollLast().deepCopyFirst();
        }
    }

    public void printCurrentGrid() {
        System.out.println("NumberOfMoves: " + this.numberOfMoves);
        grid.printGrid(this.grid);
        System.out.println("------------------------------");
    }
}








