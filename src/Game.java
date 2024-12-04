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

        System.out.print("Enter the number of rows: ");
        int rows = scanner.nextInt();
        System.out.print("Enter the number of columns: ");
        int cols = scanner.nextInt();

        String[][] config = new String[rows][cols];

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
        List<List<String>> allPaths = new ArrayList<>();
        List<String> solutionPath = null;

        stack.push(this);
        visitedStates.add(getGridHash());
        allPaths.add(new ArrayList<>());

        int totalSteps = 0;

        System.out.println("Starting DFS to solve the game...\n");
        long start = 0;
        while (!stack.isEmpty()) {
            start = System.nanoTime();
            Game currentGame = stack.pop();
            List<String> currentPath = allPaths.removeLast();

            for (String direction : new String[]{"up", "down", "left", "right"}) {
                totalSteps++;
                Game newGameState = new Game(currentGame.grid.deepCopy());
                newGameState.gridHistory.addAll(currentGame.gridHistory);
                newGameState.makeMove(direction, true);

                String newGridHash = newGameState.getGridHash();

                if (!visitedStates.contains(newGridHash)) {
                    List<String> newPath = new ArrayList<>(currentPath);
                    newPath.add(direction);

                    if (newGameState.isFinished()) {
                        long finish = System.nanoTime();
                        long timeElapsed = finish - start;
                        solutionPath = newPath;
                        System.out.println("Solution found!");
                        System.out.println("Solution Path: " + solutionPath);
                        System.out.println("Depth (steps in solution path): " + solutionPath.size());
                        System.out.println("Total Steps Explored: " + totalSteps);
                        newGameState.printCurrentGrid(true);
                        System.out.println("Elapsed Time:" + timeElapsed);
                        return;
                    }

                    visitedStates.add(newGridHash);
                    stack.push(newGameState);
                    allPaths.add(newPath);
                }
            }
        }
        long finish = System.nanoTime();
        long timeElapsed = finish - start;
        System.out.println("No solution found with DFS.");
        System.out.println("Elapsed Time:" + timeElapsed);
    }

    public void solveWithRecDFS(Stack<Game> stack, Set<String> visitedStates, int counter, List<String> currentPath, int totalSteps, boolean[] foundSolution, long start) {
        if (counter == 0) {
            start = System.nanoTime();
            System.out.println("Solving the game using RecDFS...");
            stack.push(this);
            visitedStates.add(getGridHash());
            counter++;
        }
        if (stack.isEmpty() || foundSolution[0]) {
            if (!foundSolution[0]) {
                long finish = System.nanoTime();
                long timeElapsed = finish - start;
                System.out.println("No solution found with RecDFS.");
                System.out.println("Total Steps Explored: " + totalSteps);
                System.out.println("Elapsed Time:" + timeElapsed);
            }
            return;
        }

        Game currentGame = stack.pop();

        for (String direction : new String[]{"up", "down", "left", "right"}) {
            if (foundSolution[0]) return;

            totalSteps++;

            Game newGameState = new Game(currentGame.grid.deepCopy());
            newGameState.gridHistory.addAll(currentGame.gridHistory);
            newGameState.makeMove(direction, true);
            String newGridHash = newGameState.getGridHash();

            if (!visitedStates.contains(newGridHash)) {

                List<String> newPath = new ArrayList<>(currentPath);
                newPath.add(direction);


                if (newGameState.isFinished()) {
                    long finish = System.nanoTime();
                    long timeElapsed = finish - start;
                    foundSolution[0] = true; // Mark that a solution has been found.
                    System.out.println("\nSolution found!");
                    System.out.println("Solution Path: " + newPath); // Show the path to the solution.
                    System.out.println("Depth (steps in solution path): " + newPath.size()); // Depth (steps in the path).
                    System.out.println("Total Steps Explored: " + totalSteps); // Total number of steps (all moves attempted).
                    newGameState.printCurrentGrid(true);
                    System.out.println("Elapsed Time:" + timeElapsed);
                    return;
                }


                visitedStates.add(newGridHash);
                stack.push(newGameState);
                solveWithRecDFS(stack, visitedStates, counter + 1, newPath, totalSteps, foundSolution, start);
            }
        }
    }

    public void solveWithBFS() {
        Queue<Game> queue = new LinkedList<>();
        Queue<List<String>> pathQueue = new LinkedList<>();
        Set<String> visitedStates = new HashSet<>();

        queue.add(this);
        pathQueue.add(new ArrayList<>());
        visitedStates.add(getGridHash());

        int totalSteps = 0;

        System.out.println("Starting BFS to solve the game...\n");

        long start = 0;
        while (!queue.isEmpty()) {
            start = System.nanoTime();
            Game currentGame = queue.poll();
            List<String> currentPath = pathQueue.poll();

            for (String direction : new String[]{"up", "down", "left", "right"}) {
                totalSteps++;

                Game newGameState = new Game(currentGame.grid.deepCopy());
                newGameState.gridHistory.addAll(currentGame.gridHistory);
                newGameState.makeMove(direction, true);

                String newGridHash = newGameState.getGridHash();

                if (!visitedStates.contains(newGridHash)) {
                    visitedStates.add(newGridHash);

                    queue.add(newGameState);
                    List<String> newPath = new ArrayList<>(currentPath);
                    newPath.add(direction);
                    pathQueue.add(newPath);
                }
                if (newGameState.isFinished()) {
                    long finish = System.nanoTime();
                    long timeElapsed = finish - start;
                    List<String> solutionPath = new ArrayList<>(currentPath);
                    solutionPath.add(direction);
                    System.out.println("Solution found!");
                    System.out.println("Solution Path: " + solutionPath);
                    System.out.println("Depth (steps in solution path): " + solutionPath.size());
                    System.out.println("Total Steps Explored: " + totalSteps);
                    newGameState.printCurrentGrid(true);
                    System.out.println("Elapsed Time:" + timeElapsed);

                    return;
                }
            }
        }
        long finish = System.nanoTime();
        long timeElapsed = finish - start;
        System.out.println("Elapsed Time:" + timeElapsed);
        System.out.println("No solution found with BFS.");
        System.out.println("Total Steps Explored: " + totalSteps);
    }

    public void solveWithHillClimbing() {
        System.out.println("Starting Hill Climbing...");
        long start = 0;
        while (true) {
            start = System.nanoTime();
            String bestMove = selectBestMove();
            if (bestMove == null) {
                System.out.println("No better moves available. Stopping.");
                break;
            }
            String currentGridState = getGridHash();
            System.out.print(bestMove + " ");
            this.makeMove(bestMove, true);
            String newGridState = getGridHash();
            if (newGridState.equals(currentGridState)) {
                break;
            }
            if (isFinished()) {
                long finish = System.nanoTime();
                long timeElapsed = finish - start;
                System.out.println();
                System.out.println("Solution found!");
                System.out.println("Number of moves:" + this.numberOfMoves);
                this.printCurrentGrid(true);
                System.out.println("Elapsed Time:" + timeElapsed);

                return;
            }
        }
        long finish = System.nanoTime();
        long timeElapsed = finish - start;

        System.out.println();
        System.out.println("Elapsed Time:" + timeElapsed);
        System.out.println("No solution found with Hill Climbing.");
        System.out.println("Number of moves:" + this.numberOfMoves);
    }

    public void solveWithUCS() {
        int totalMoves = 0;

        PriorityQueue<Game> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(g -> g.numberOfMoves));

        Map<String, List<String>> pathMap = new HashMap<>(); // Tracks paths for each game state.
        Set<String> visitedStates = new HashSet<>(); // Tracks visited states to avoid revisiting.

        // Initialize the search.
        priorityQueue.add(this);
        pathMap.put(this.getGridHash(), new ArrayList<>());
        visitedStates.add(this.getGridHash());
        this.numberOfMoves = 0;

        System.out.println("Starting Uniform Cost Search...\n");
        long start = 0;
        while (!priorityQueue.isEmpty()) {
            start = System.nanoTime();
            Game currentGame = priorityQueue.poll();
            List<String> currentPath = pathMap.get(currentGame.getGridHash());

            // Explore all possible moves.
            for (String direction : new String[]{"up", "down", "left", "right"}) {
                int moveCost = switch (direction) {
                    case "up" -> 1;
                    case "down" -> 2;
                    case "left" -> 3;
                    case "right" -> 4;
                    default -> 0;
                };
                totalMoves++;
                Game newGameState = currentGame.simulateMove(direction);
                String newGridHash = newGameState.getGridHash();

                if (!visitedStates.contains(newGridHash)) {
                    visitedStates.add(newGridHash);

                    List<String> newPath = new ArrayList<>(currentPath);
                    newPath.add(direction);
                    newGameState.numberOfMoves = currentGame.numberOfMoves + moveCost;
                    pathMap.put(newGridHash, newPath);
                    priorityQueue.add(newGameState);
                }
                if (newGameState.isFinished()) {
                    long finish = System.nanoTime();
                    long timeElapsed = finish - start;
                    System.out.println("Solution found!");
                    System.out.println("Solution Path: " + pathMap.get(newGameState.getGridHash()));
                    System.out.println("Number of Moves: " + currentPath.size());
                    System.out.println("Total Cost  : " + (newGameState.numberOfMoves));
                    System.out.println("Total Moves" + totalMoves);
                    newGameState.printCurrentGrid(true);
                    System.out.println("Elapsed Time:" + timeElapsed);
                    return;
                }

            }
        }
        long finish = System.nanoTime();
        long timeElapsed = finish - start;
        System.out.println("No solution found with Uniform Cost Search.");
        System.out.println("Elapsed Time:" + timeElapsed);
    }

    public void solveWithAStar() {
        // Comparator to prioritize based on f(n) = g(n) + h(n), with tie-breaking on h(n)
        Comparator<Game> comparator = (g1, g2) -> {
            int f1 = g1.getEstimatedCost();
            int f2 = g2.getEstimatedCost();
            if (f1 != f2) {
                return Integer.compare(f1, f2);
            }
            int h1 = g1.grid.countColoredCells();
            int h2 = g2.grid.countColoredCells();
            return Integer.compare(h2, h1);
        };

        PriorityQueue<Game> priorityQueue = new PriorityQueue<>(comparator);
        Set<String> visitedStates = new HashSet<>();
        Map<String, List<String>> pathMap = new HashMap<>();

        this.setEstimatedCost(this.numberOfMoves + this.grid.countColoredCells());
        priorityQueue.add(this);
        visitedStates.add(this.getGridHash());
        pathMap.put(this.getGridHash(), new ArrayList<>());

        System.out.println("Starting A* Search ...\n");
        int totalMoves = 0;
        long start = 0;
        while (!priorityQueue.isEmpty()) {
            start = System.nanoTime();

            // Get the state with the lowest cost
            Game currentGame = priorityQueue.poll();
            List<String> currentPath = pathMap.get(currentGame.getGridHash());


            for (String direction : new String[]{"up", "down", "left", "right"}) {
                int moveCost = switch (direction) {
                    case "up" -> 1;
                    case "down" -> 2;
                    case "left" -> 3;
                    case "right" -> 4;
                    default -> 0;
                };
                totalMoves++;
                Game newGameState = currentGame.simulateMove(direction);
                String newGridHash = newGameState.getGridHash();

                if (!visitedStates.contains(newGridHash)) {
                    visitedStates.add(newGridHash);

                    int heuristic = newGameState.grid.countColoredCells();
                    newGameState.setEstimatedCost(currentGame.numberOfMoves + moveCost + heuristic);
                    newGameState.numberOfMoves = currentGame.numberOfMoves + moveCost;

                    List<String> newPath = new ArrayList<>(currentPath);
                    newPath.add(direction);
                    pathMap.put(newGridHash, newPath);

                    priorityQueue.add(newGameState);

                }
                if (newGameState.isFinished()) {
                    long finish = System.nanoTime();
                    //long finish = System.currentTimeMillis();
                    long timeElapsed = finish - start;
                    System.out.println("\n*** Solution found! ***");
                    System.out.println("Solution Path: " + pathMap.get(newGameState.getGridHash()));
                    System.out.println("Number of Moves: " + currentPath.size());
                    System.out.println("Total Cost: " + newGameState.numberOfMoves);
                    System.out.println("Total Moves: " + totalMoves);
                    newGameState.printCurrentGrid(true);
                    System.out.println("Elapsed Time:" + timeElapsed);
                    return;
                }
            }
        }
        long finish = System.nanoTime();
        long timeElapsed = finish - start;
        System.out.println("No solution found with A* Search.");
        System.out.println("Elapsed Time:" + timeElapsed);

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








