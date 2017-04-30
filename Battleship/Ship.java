public class Ship {
  // List of the possible types of ships to avoid string comparing
  public enum ShipTypes {
    BATTLESHIP,
    CARRIER,
    DESTROYER,
    SUBMARINE,
    PATROL
  }
  // Type of the current ship
  private ShipTypes ship_type;
  // Health of the current ship
  private int health;
  // Constructor
  public Ship(ShipTypes ship_type) {
    // Sets the type of current ship
    this.ship_type = ship_type;
    // Sets the initial health based on the ship type
    this.health = initial_health();
  }

  /** Reduces the health of the ship by 1, as it was hit. */
  public void hit() {
    this.health = this.health - 1;
  }

  /**
   * Checks if the ship's health is 0, aka it is dead.
   *
   * @return - true if the ship is dead, false otherwise.
   */
  public boolean is_dead() {
    return this.health == 0;
  }

  /**
   * Converts the current ship's ShipType enum to a string to use for printing.
   *
   * @return - a String that is the type of the ship.
   */
  public String type() {
    return ship_type.toString();
  }

  /**
   * Gets the abbreviation of this type of ship based on the ship type to be used for printing in
   * the board.
   *
   * @return - a String that is the 1 letter abbreviation of the type of the ship.
   */
  public String abbreviation() {
    switch (this.ship_type) {
      case BATTLESHIP:
        return "B";
      case CARRIER:
        return "C";
      case DESTROYER:
        return "D";
      case SUBMARINE:
        return "S";
      case PATROL:
        return "P";
    }
    return " ";
  }

  /**
   * Determines what the initial health of the ship is based on the type of ship.
   *
   * @return - an integer that is the initial health value of the type of ship.
   */
  public int initial_health() {
    switch (this.ship_type) {
      case BATTLESHIP:
        return 6;
      case CARRIER:
        return 5;
      case DESTROYER:
        return 4;
      case SUBMARINE:
        return 3;
      case PATROL:
        return 2;
      default:
        System.out.println("Invalid Ship Type");
        break;
    }
    return -1;
  }
}
