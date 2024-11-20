import java.util.*;

public class Game {
    private LinkedList<Grid> gridHistory;
    private Grid grid;
    private String previousGridState;
    private Grid firstGrid;
    private int numberOfMoves;
    private int estimatedCost;


    public Game(Grid initialGrid) {
        this.grid = initialGrid;
        this.numberOfMoves = 0;
        this.firstGrid = initialGrid.deepCopyFirst();
        this.gridHistory = new LinkedList<>();
        this.gridHistory.add(this.grid);
        this.previousGridState = getGridHash();
    }

    public int getEstimatedCost() {
        return this.estimatedCost;
    }

    public void setEstimatedCost(int cost) {
        this.estimatedCost = cost;
    }

//    public LinkedList<Grid> getGridHistory() {
//        return this.gridHistory;
//    }

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
        this.firstGrid = this.grid.deepCopyFirst();
        System.out.println("Grid initialized successfully!");
    }

    public void solveWithDFS() {
        Stack<Game> stack = new Stack<>();
        Set<String> visitedStates = new HashSet<>();

        stack.push(this);
        visitedStates.add(getGridHash());

        System.out.println("Starting DFS to solve the game...\n");

        while (!stack.isEmpty()) {
            Game currentGame = stack.pop();
            System.out.println("Current Depth: " + currentGame.numberOfMoves);
            System.out.println("Current Grid State:");
            currentGame.printCurrentGrid(true);
            System.out.println("------------------------------");
            for (String direction : new String[]{"up", "down", "left", "right"}) {
                System.out.println("Current Moves In this Depth : " + currentGame.numberOfMoves);
                Game newGameState = new Game(currentGame.grid.deepCopy());
                newGameState.gridHistory.addAll(currentGame.gridHistory);

                newGameState.makeMove(direction, true);
                String newGridHash = newGameState.getGridHash();
                if (!visitedStates.contains(newGridHash)) {
                    if (newGameState.isFinished()) {
                        currentGame.numberOfMoves++;
                        System.out.println("Attempting move: " + direction);
                        System.out.println("\nSolution found in depth " + currentGame.numberOfMoves + " !");
                        newGameState.printCurrentGrid(true);
                        System.exit(0);
                        return;
                    }
                    newGameState.numberOfMoves = currentGame.numberOfMoves + 1;
                    visitedStates.add(newGridHash);
                    stack.push(newGameState);
                    System.out.println("Attempting move: " + direction);
                    newGameState.printCurrentGrid(true);
                    System.out.println("------------------------------");
                } else {
                    System.out.println("Move " + direction + " leads to a previously visited state. Skipping.");
                }
            }
        }

        System.out.println("No solution found with DFS.");
    }

    public void solveWithRecDFS(Stack<Game> stack, Set<String> visitedStates, int counter) {
        if (counter == 0) {
            stack.push(this);
            visitedStates.add(getGridHash());
            counter++;
        }

        if (stack.isEmpty()) {
            System.out.println("No solution found with RecDFS.");
            System.exit(0);
        }

        Game currentGame = stack.pop();
        System.out.println("Current Depth: " + currentGame.numberOfMoves);
        System.out.println("Current Grid State:");
        currentGame.printCurrentGrid(true);
        System.out.println("------------------------------");

        for (String direction : new String[]{"up", "down", "left", "right"}) {
            System.out.println("Current Moves In this Depth: " + currentGame.numberOfMoves);
            Game newGameState = new Game(currentGame.grid.deepCopy());
            newGameState.gridHistory.addAll(currentGame.gridHistory);

            newGameState.makeMove(direction, true);
            String newGridHash = newGameState.getGridHash();

            if (!visitedStates.contains(newGridHash)) {
                if (newGameState.isFinished()) {
                    System.out.println("Attempting move: " + direction);
                    System.out.println("\nSolution found in depth " + (currentGame.numberOfMoves + 1) + "!");
                    newGameState.printCurrentGrid(true);
                    System.exit(0);
                    return;
                }
                newGameState.numberOfMoves = currentGame.numberOfMoves + 1;
                visitedStates.add(newGridHash);
                stack.push(newGameState);
                System.out.println("Attempting move: " + direction);
                newGameState.printCurrentGrid(true);
                System.out.println("------------------------------");
            } else {
                System.out.println("Move " + direction + " leads to a previously visited state. Skipping.");
            }
        }

        solveWithRecDFS(stack, visitedStates, counter);
    }

    public void solveWithBFS() {
        Queue<Game> queue = new LinkedList<>();
        Set<String> visitedStates = new HashSet<>();

        queue.add(this);
        visitedStates.add(getGridHash());

        System.out.println("Starting BFS to solve the game...\n");

        while (!queue.isEmpty()) {
            Game currentGame = queue.poll();
            System.out.println("Current Depth: " + currentGame.numberOfMoves);
            System.out.println("Current Grid State:");
            currentGame.printCurrentGrid(true);
            System.out.println("------------------------------");
            for (String direction : new String[]{"up", "down", "left", "right"}) {
                Game newGameState = new Game(currentGame.grid.deepCopy());
                newGameState.gridHistory.addAll(currentGame.gridHistory);

                newGameState.makeMove(direction, true);
                if (newGameState.isFinished()) {
                    currentGame.numberOfMoves++;
                    System.out.println("---------------------------------------------------------------------");
                    System.out.println("Attempting move: " + direction);
                    System.out.println("\n Solution found in depth" + currentGame.numberOfMoves + " !");
                    newGameState.printCurrentGrid(true);
                    System.exit(0);
                    return;
                }
                String newGridHash = newGameState.getGridHash();

                if (!visitedStates.contains(newGridHash)) {
                    newGameState.numberOfMoves = currentGame.numberOfMoves + 1;
                    visitedStates.add(newGridHash);
                    queue.add(newGameState);
                    System.out.println("Attempting move: " + direction);
                    newGameState.printCurrentGrid(true);
                    System.out.println("------------------------------");
                } else {
                    System.out.println("Move " + direction + " leads to a previously visited state. Skipping.");
                }
            }
        }

        System.out.println("No solution found with BFS.");
    }

    public void solveWithHillClimbing() {
        System.out.println("Starting Hill Climbing...");
        while (true) {
            String bestMove = selectBestMove();
            if (bestMove == null) {
                System.out.println("No better moves available. Stopping.");
                break;
            }
            String currentGridState = getGridHash();
            System.out.println("Performing move: " + bestMove);
            this.makeMove(bestMove);
            this.printCurrentGrid();
            String newGridState = getGridHash();
            if (newGridState.equals(currentGridState)) {
                break;
            }
            if (isFinished()) {
                System.out.println("Solution found!");
                this.printCurrentGrid();
                return;
            }
        }
        System.out.println("No solution found with Hill Climbing.");
    }

    public void solveWithUCS() {
        PriorityQueue<Game> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(g -> g.numberOfMoves));
        Set<String> visitedStates = new HashSet<>();

        priorityQueue.add(this);
        visitedStates.add(this.getGridHash());

        System.out.println("Starting Uniform Cost Search...");

        while (!priorityQueue.isEmpty()) {
            Game currentGame = priorityQueue.poll();

            System.out.println("Exploring state with cost: " + currentGame.numberOfMoves);
            currentGame.printCurrentGrid();

            for (String direction : new String[]{"up", "down", "left", "right"}) {
                Game newGameState = currentGame.simulateMove(direction);

                // Correctly set the cost for the new state
                newGameState.numberOfMoves = currentGame.numberOfMoves + 1;

                if (newGameState.isFinished()) {
                    System.out.println("Attempting move: " + direction);
                    System.out.println("\nSolution found in " + newGameState.numberOfMoves + " moves!");
                    newGameState.printCurrentGrid(true);
                    return; // End the search
                }

                String newGridHash = newGameState.getGridHash();

                if (!visitedStates.contains(newGridHash)) {
                    visitedStates.add(newGridHash);
                    priorityQueue.add(newGameState);
                    System.out.println("Queued new state with move: " + direction);
                }
            }
        }

        System.out.println("No solution found with Uniform Cost Search.");
    }

    public void solveWithAStar() {

//        PriorityQueue<Game> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(g -> g.getEstimatedCost()));

        Comparator<Game> comparator = (g1, g2) -> {
            int f1 = g1.getEstimatedCost();
            int f2 = g2.getEstimatedCost();
            if (f1 != f2) {
                return Integer.compare(f1, f2);
            }
            return Integer.compare(g2.grid.countColoredCells(), g1.grid.countColoredCells());
        };

        PriorityQueue<Game> priorityQueue = new PriorityQueue<>(comparator);
        Set<String> visitedStates = new HashSet<>();

        this.setEstimatedCost(this.numberOfMoves + this.grid.countColoredCells());
        priorityQueue.add(this);
        visitedStates.add(this.getGridHash());

        System.out.println("Starting A* Search with tie-breaking...");

        while (!priorityQueue.isEmpty()) {
            Game currentGame = priorityQueue.poll();

            System.out.println("Exploring state with cost: " + currentGame.numberOfMoves + ", heuristic: " + currentGame.grid.countColoredCells() + ", total: " + currentGame.getEstimatedCost());
            currentGame.printCurrentGrid();

            if (currentGame.isFinished()) {
                System.out.println("\n*** Solution found in " + currentGame.numberOfMoves + " moves! ***");
                currentGame.printCurrentGrid(true);
                return;
            }

            for (String direction : new String[]{"up", "down", "left", "right"}) {
                Game newGameState = currentGame.simulateMove(direction);

                newGameState.numberOfMoves = currentGame.numberOfMoves + 1;

                String newGridHash = newGameState.getGridHash();

                if (!visitedStates.contains(newGridHash)) {
                    visitedStates.add(newGridHash);

                    int heuristic = newGameState.grid.countColoredCells();
                    newGameState.setEstimatedCost(newGameState.numberOfMoves + heuristic);

                    priorityQueue.add(newGameState);

                    System.out.println("Queued new state with move: " + direction + ", heuristic: " + heuristic + ", total cost: " + newGameState.getEstimatedCost());
                }
            }
        }

        System.out.println("No solution found with A* Search.");
    }



    public Grid getGrid() {
        return this.grid;
    }


    public boolean canMove(String direction, Cell cell) {
        return cell instanceof ColoredCell && cell.isMovable() && cell.getNeighbor(direction) != null && cell.getNeighbor(direction).isMovable();
    }

    public void makeMove(String direction) {
        makeMove(direction, false);
    }

    public void makeMove(String direction, boolean isSystemPlays) {
        this.numberOfMoves++;
        gridHistory.add(this.grid.deepCopy());
        while (true) {
            String currentGridState = getGridHash();
            move(direction);
            String newGridState = getGridHash();

            if (newGridState.equals(currentGridState)) {

                break;
            }
            previousGridState = newGridState;
        }
        if (!isSystemPlays) {
            this.result();
        }
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
        this.numberOfMoves += 1;
        this.grid = this.firstGrid.deepCopyFirst();
        this.grid.linkNeighbors();
        this.gridHistory.clear();
        this.gridHistory.add(this.grid);
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
        if (this.numberOfMoves >= 100000) {
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

    public void printCurrentGrid(boolean isSystemPlays) {
        if (!isSystemPlays) {
            System.out.println("NumberOfMoves: " + this.numberOfMoves);
        }
        grid.printGrid(this.grid);
        System.out.println("------------------------------");
    }

    public void printCurrentGrid() {
        printCurrentGrid(false);
    }

    public String selectBestMove() {
        String[] directions = {"up", "down", "left", "right"};
        String bestDirection = null;
        int minColoredCells = Integer.MAX_VALUE;

        for (String direction : directions) {
            Game simulatedGame = this.simulateMove(direction);
            int coloredCells = simulatedGame.grid.countColoredCells();
            if (coloredCells < minColoredCells) {
                minColoredCells = coloredCells;
                bestDirection = direction;
            }
        }
        return bestDirection;
    }

    public Game simulateMove(String direction) {
        Grid copiedGrid = this.grid.deepCopy();
        Game simulatedGame = new Game(copiedGrid);
        simulatedGame.makeMove(direction, true);
        return simulatedGame;
    }


}








