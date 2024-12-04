import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Grid grid = new Grid(0, 0, new String[0][0]);
        Game game = new Game(grid);
        game.initGame();

        while (true) {
            Game newGame = new Game(game.getGrid().deepCopyFirst());
            displaySolveOptions(newGame);

            if (newGame.isFinished()) {
                System.out.println("The game is finished! Redisplay solve options...");
            }
        }
    }

    public static void displaySolveOptions(Game game) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose an option:");
        System.out.println("Press 1 if you want to solve the game by Yourself.");
        System.out.println("Press 2 if you want the system to solve the game by DFS.");
        System.out.println("Press 3 if you want the system to solve the game by BFS.");
        System.out.println("Press 4 if you want the system to solve the game by RecDFS.");
        System.out.println("Press 5 if you want the system to solve the game by HillClimbing.");
        System.out.println("Press 6 if you want the system to solve the game by UCS.");
        System.out.println("Press 7 if you want the system to solve the game by A*.");
        System.out.println("Press 100 to Exit.");
        System.out.println("------------------------------------------------");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                if (initTheGame(game)) {
                    return;
                }
                break;
            case 2:
                game.solveWithDFS();
                break;
            case 3:
                game.solveWithBFS();
                break;
            case 4:
                Stack<Game> stack = new Stack<>();
                Set<String> visitedStates = new HashSet<>();
                List<String> currentPath = new ArrayList<>();
                int counter = 0;
                int totalSteps = 0;
                boolean[] foundSolution = {false};
                long start=0;
                game.solveWithRecDFS(stack, visitedStates, counter, currentPath, totalSteps, foundSolution,start);

                break;
            case 5:
                game.solveWithHillClimbing();
                break;
            case 6:
                game.solveWithUCS();
                break;
            case 7:
                game.solveWithAStar();
                break;
            case 100:
                System.out.println("Exiting the program. Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please select a valid option.");
        }

        if (game.isFinished()) {
            System.out.println("Congratulations! The game is finished!");
            System.out.println("------------------------------------------------");
        }
    }

    public static boolean initTheGame(Game game) {
        JFrame frame = new JFrame("Game");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        boolean[] gameFinished = {false};

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();

                switch (keyCode) {
                    case KeyEvent.VK_UP:
                        game.makeMove("up");
                        System.out.println("Up move");
                        break;
                    case KeyEvent.VK_DOWN:
                        game.makeMove("down");
                        System.out.println("Down move");
                        break;
                    case KeyEvent.VK_LEFT:
                        game.makeMove("left");
                        System.out.println("Left move");
                        break;
                    case KeyEvent.VK_RIGHT:
                        game.makeMove("right");
                        System.out.println("Right move");
                        break;
                    case KeyEvent.VK_U:
                        game.undo();
                        System.out.println("Move undone.");
                        break;
                    case KeyEvent.VK_R:
                        game.revertToInitialState();
                        System.out.println("Game reset to initial state.");
                        break;
                    default:
                        break;
                }

                game.printCurrentGrid();
                if (game.isFinished()) {
                    System.out.println("Congratulations! You've completed the game.");
                    gameFinished[0] = true;
                    frame.dispose();
                }
            }
        });

        frame.setVisible(true);
        game.printCurrentGrid();


        while (!gameFinished[0]) {
            try {
                Thread.sleep(100); // Prevent busy-waiting
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        return gameFinished[0];
    }
}
