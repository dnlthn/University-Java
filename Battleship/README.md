# Battleship board game over a network
**This is currently a work in progress.**
## Goal
Coordinates a game of battleship with another classmate over a network. Packets can be sent any way we like as long as we both choose the same way to communicate effectively. 10% of all packets must be _lost_.
## Currently Functional
- `BattleshipGame.java`
	- Lets player set up the game board 'visually' through the console and handles checking for attacks from enemy.
- `Ship.java`
	- Template for each ship to be used in the `BattleshipGame.java`.
## TODO
- Modify `EchoClient.java` and `EchoServer.java` to work with classmate.
- Integrate the `BattleshipGame.java` class with `EchoClient.java` and `EchoServer.java`.