import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        displaySolveOptions();
    }

    public static void displaySolveOptions() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose an option:");
        System.out.println("Press 1 if you want to solve the game yourself.");
        System.out.println("Press 2 if you want the system to solve the game by DFS.");
        System.out.println("Press 3 if you want the system to solve the game by BFS.");
        int choice = scanner.nextInt();

        if (choice == 1) {
            initTheGame(); // Proceed with the game for the user to solve
        } else if (choice == 2) {
            Grid grid = new Grid(0, 0, new String[0][0]); // Initialize an empty grid
            Game game = new Game(grid);
            game.initGame();
            System.out.println("Solving the game using DFS...");
            game.solveWithDFS();
        } else if (choice == 3) {
            Grid grid = new Grid(0, 0, new String[0][0]);
            Game game = new Game(grid);
            game.initGame();
            System.out.println("Solving the game using BFS...");
            game.solveWithBFS();
        } else {
            System.out.println("Invalid choice. Please restart and select a valid option.");
        }
    }

    public static void initTheGame() {
        // Create Grid and Game instance
        Grid grid = new Grid(0, 0, new String[0][0]); // Empty grid for initialization
        Game game = new Game(grid);

        // Initialize the game
        game.initGame(); // Let the user input the grid dimensions and configuration

        // Create JFrame to capture key events
        JFrame frame = new JFrame("Game");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add KeyListener to capture arrow keys and other actions
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();

                // Convert key events to directions and call makeMove
                switch (keyCode) {
                    case KeyEvent.VK_UP:
                        game.makeMove("up");
                        System.out.println("Up move");
                        break;
                    case KeyEvent.VK_DOWN:
                        game.makeMove("down");
                        System.out.println("down move");
                        break;
                    case KeyEvent.VK_LEFT:
                        game.makeMove("left");
                        System.out.println("left move");
                        break;
                    case KeyEvent.VK_RIGHT:
                        game.makeMove("right");
                        System.out.println("right move");

                        break;
                    case KeyEvent.VK_U: // Press 'U' to undo
                        game.undo();
                        System.out.println("Move undone.");
                        break;
                    case KeyEvent.VK_R: // Press 'R' to reset
                        System.out.println("Game reset to initial state.");
                        game.revertToInitialState();
                        System.out.println("------------------------------");
                        break;
                    default:
                        break;
                }

                // Print the updated grid after the move or action
                game.printCurrentGrid();
                // Display prompt for undo/reset options
                System.out.println("Press 'U' to undo the last move or 'R' to reset the game.");
                System.out.println("------------------------------");
            }
        });

        // Show the frame
        frame.setVisible(true);
        game.printCurrentGrid();

    }
}
