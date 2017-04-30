public class BattleshipBoardTest {
  public static void main(String[] args) {
    // Creates a 10x10 board
    BattleshipGame board = new BattleshipGame(10);
    // Make sure the placing of all ships works
    board.setup_board();
    System.out.println("Board after setup:");
    board.print_board();
    // Test the attack method. It should only remove the index of the ship that is hit.
    String attack_result = board.attack(0, 0);
    System.out.println("Attack result: " + attack_result);
    System.out.println("Board after attack:");
    board.print_board();
  }
}
