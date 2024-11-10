import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Main {
    public static void main(String[] args) {
        initTheGame();
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
                        System.out.println("Up move");
                        game.makeMove("up");
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
                        break;
                    default:
                        break;
                }

                // Print the updated grid after the move or action
                game.printCurrentGrid();

                // Display prompt for undo/reset options
                System.out.println("Press 'U' to undo the last move or 'R' to reset the game.");
            }
        });

        // Show the frame
        frame.setVisible(true);

        // Initial grid printout (after user input)
        game.printCurrentGrid();
        System.out.println("Press arrow keys to move, 'U' to undo, or 'R' to reset.");
    }
}
