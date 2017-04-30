import java.util.Random;
import java.util.Scanner;

public class BattleshipGame {
  // Holds the players current board
  private Ship[][] board;

  // Constructor to make boards of different sizes
  public BattleshipGame(int board_size) {
    board = new Ship[board_size][board_size];
  }

  /**
   * Attempts an attack on players board and updates the board.
   *
   * @param row - the index of the row on the board that is attacked.
   * @param column - the index of the column on the board that is attacked.
   * @return - a String containing the result of the attack. The first word is the result. This
   *     string will be sent in a packet over the network.
   */
  public String attack(int row, int column) {
    // Check if there is a ship in the attack location
    if (board[row][column] != null) {
      // Create a reference to the ship so that the location can be null'd
      Ship ship = board[row][column];
      // Remove the pointer to the ship from the board place
      board[row][column] = null;
      // Decrease the battleships hp
      ship.hit();
      // Check if the ship is now dead
      if (ship.is_dead()) {
        // If the sunken ship is the Ship, the game is over
        if (ship.type().equals("BATTLESHIP")) {
          return "DEFEAT - You sunk my Ship.";
        } else {
          return "SUNK - You sunk my " + ship.type() + ".";
        }
      } else {
        // Generates a random number to decide if the player is going to be honest or not
        Random rng = new Random();
        int troll_number = rng.nextInt() % 2;
        if (troll_number > 0) {
          return "HIT - You hit my " + ship.type() + ".";
        } else {
          return "HIT - You hit..something. Maybe the water?";
        }
      }
    } else {
      // Better luck next time
      return "MISS - A shoot.. and a miss.";
    }
  }

  /** Uses console input to set up each ship in the game. */
  public void setup_board() {
    // Scanner is passed in to the place_ship for positioning of ships
    Scanner input = new Scanner(System.in);
    // Place each ship on the board at a desired location
    place_ship(Ship.ShipTypes.BATTLESHIP, input);
    place_ship(Ship.ShipTypes.CARRIER, input);
    place_ship(Ship.ShipTypes.DESTROYER, input);
    place_ship(Ship.ShipTypes.SUBMARINE, input);
    place_ship(Ship.ShipTypes.PATROL, input);
    // Close the scanner
    input.close();
  }

  /**
   * Controls the placement of any ship type visually through the console.
   *
   * @param ship_type - the type of ship that will be placed on the board.
   * @param input - console scanner to get user input to move and place the pieces.
   * @todo - user KeyEvents instead of scanner for smoother input without pressing enter.
   */
  private void place_ship(Ship.ShipTypes ship_type, Scanner input) {
    // Creates the ship that will be placed to use it's attributes
    Ship ship = new Ship(ship_type);
    // Current position and orientation of the ship that is being placed
    int current_row = 0;
    int current_column = 0;
    boolean horizontal = true;
    // Controls when to stop looping
    boolean ship_placed = false;
    // Loop until the ship has been placed in a valid position
    while (!ship_placed) {
      System.out.println("Place your " + ship.type());
      // Prints the board with the current position of the ship. The print also returns if the position is valid.
      boolean valid_placing = print_position(current_row, current_column, ship, horizontal);
      // Instructions
      System.out.println("Enter your next attack: ");
      System.out.println("\tMove with the WASD.");
      System.out.println("\tRotate the ship with \'R\'.");
      System.out.println("\tPlace the ship with \'P\'");
      // Length of the ship when placed. Aliased to length for readability.
      int length = ship.initial_health();
      // Gets what the user wants to do with the current position of the ship from console. Made uppercase for easier handling.
      char move = input.next().toUpperCase().charAt(0);
      // Handle the move from the user
      switch (move) {
        case 'P':
          // Places the ship on the board if it is valid
          if (valid_placing) {
            ship_placed = true;
            // Handles the placing in different orientations
            if (horizontal) {
              for (int column = current_column; column < current_column + length; column++) {
                board[current_row][column] = ship;
              }
            } else {
              for (int row = current_row; row < current_row + length; row++) {
                board[row][current_column] = ship;
              }
            }
          }
          break;
        case 'W':
          // Moves the ship visually up (north) by 1, if possible
          if (current_row > 0) {
            current_row = current_row - 1;
          }
          break;
        case 'A':
          // Moves the ship visually left (west) by 1, if possible
          if (current_column > 0) {
            current_column = current_column - 1;
          }
          break;
        case 'S':
          // Moves the ship visually down (south) by 1, if possible.
          if (!horizontal && current_row + length - 1 < 9) {
            current_row = current_row + 1;
          } else if (horizontal && current_row < 9) {
            current_row = current_row + 1;
          }
          break;
        case 'D':
          // Moves the ship visually right (east) by 1, if possible
          if (horizontal && current_column + length - 1 < 9) {
            current_column = current_column + 1;
          } else if (!horizontal && current_column < 9) {
            current_column = current_column + 1;
          }
          break;
        case 'R':
          // Swaps the ship between vertical or horizontal orientation around the left, or top, most position, if possible
          if (!horizontal && current_column <= 10 - length) {
            horizontal = !horizontal;
          } else if (horizontal && current_row <= 10 - length) {
            horizontal = !horizontal;
          }
          break;
        default:
          break;
      }
      // Clears the terminal on a UNIX machine before re-printing for nicer output.
      System.out.print("\033[H\033[2J");
      System.out.flush();
    }
  }

  /**
   * Prints the current state of the board to the console. This print differs from the 0-parameter
   * print_board() because it prints a desired location for a new ship.
   *
   * @param position_row - current row index of the root (left/top-most index) of the ship to be
   *     placed.
   * @param position_column - current column index of the root (left/top-most index) of the ship to
   *     be placed.
   * @param ship - ship object that will be placed. Passed in to get attributes of the new ship.
   * @param horizontal - holds the current orientation of the new ship. True if horizontal, False if
   *     vertical
   * @return - True when the position of the new ship that was printed is a valid location. Mainly
   *     used for collisions with other ships.
   * @todo - This method is a little hairy. I don't like the fact that it is named "print_position"
   *     but returns if it is valid. Should probably be split into smaller sub-methods.
   */
  public boolean print_position(
      int position_row, int position_column, Ship ship, boolean horizontal) {
    // the return of the method. Placing is assumed valid until proven invalid.
    boolean valid_placing = true;
    // Subtracts 1 from the ship length to account for the 'root' (left/top-most index) of the ship
    int ship_length = ship.initial_health() - 1;
    // Loops through the entire board to print and check position validity
    for (int row = 0; row < 10; row++) {
      System.out.print("| ");
      for (int column = 0; column < 10; column++) {
        // Long conditional that checks if the current row and column are part of the new ship
        boolean part_of_ship =
            (position_row == row && position_column == column)
                || (!horizontal
                    && row >= position_row
                    && row <= position_row + ship_length
                    && column == position_column)
                || (horizontal
                    && column >= position_column
                    && column <= position_column + ship_length
                    && row == position_row);
        // Determines what to print in the index of the board
        if (part_of_ship) {
          // Prints X if there is a collision with another ship, otherwise prints the new ship's abbreviation
          if (board[row][column] != null) {
            System.out.print("X");
            // Invalid placing if there is a collision
            valid_placing = false;
          } else {
            System.out.print(ship.abbreviation());
          }
        } else if (board[row][column] != null) {
          // Prints the abbreviation of the ship that is already placed at this index
          System.out.print(board[row][column].abbreviation());
        } else {
          // Prints blank if there is not a ship there and it is not a desired location
          System.out.print(" ");
        }
        System.out.print(" | ");
      }
      System.out.println();
    }
    return valid_placing;
  }

  /** Prints the current state of the board to the console with somewhat decent formatting */
  public void print_board() {
    for (int row = 0; row < 10; row++) {
      System.out.print("| ");
      for (int column = 0; column < 10; column++) {
        if (board[row][column] != null) {
          // Print the abbreviation of the ship that is in the index
          System.out.print(board[row][column].abbreviation());
        } else {
          // Print blank is there is not a ship in the index
          System.out.print(" ");
        }
        System.out.print(" | ");
      }
      System.out.println();
    }
  }
}
