package model;

import java.awt.Point;
/**
 * This type models a 3x3 tic-tac-toe board in the well-known game
 */
import java.util.Observable;

public class TicTacToeGame extends Observable {
  private char[][] board;
  private int size;
  private ComputerPlayer computerPlayer;

  /**
   * Construct a Tic Toe Game that one human user can play against 
   * a Computer player with swappable strategies.
   */
  public TicTacToeGame() {
    size = 3;
    initializeBoard();
    computerPlayer = new ComputerPlayer();
    computerPlayer.setStrategy(new RandomAI());
    setChanged();
    notifyObservers();
  }

  // Called from constructor and strartNewGame();
  private void initializeBoard() {
    board = new char[size][size];
    for (int r = 0; r < size; r++) {
      for (int c = 0; c < size; c++) {
        board[r][c] = '_';
      }
    }
  }

  /**
   * Start a new game and tell all observers to draw an new game
   * with the string message startNewGame()
   */
  public void startNewGame() {
    initializeBoard();
    // The state of this model just changed so tell any observer to update themselves
    setChanged();
    notifyObservers("startNewGame()");
  }

  /**
   * Swap strategies for the ComputerPlayer
   * @param AI The algorithm use by the ComputerPlayer to choose the next move
   */
  public void setComputerPlayerStrategy(TicTacToeStrategy AI) {
    this.computerPlayer.setStrategy(AI);
  }

  /**
   * Get a reference to the one Computer Player and the Strategy
   * @return
   */
  public ComputerPlayer getComputerPlayer() {
    return computerPlayer;
  }

  /**
  * Select the given move and toggle between X and O player.
  * Precondition row and col are both are 0, 1 or  2
  *  
  * @param row The player's row
   * @param col The player's column
   * @param testing TODO
  * @return True if the move was available or false otherwise
  */
  public void humanMove(int row, int col, boolean testing) {
    if (board[row][col] != '_')
      return;
    else {
      board[row][col] = 'X';
      if (! testing && this.stillRunning()) {
        Point move = computerPlayer.desiredMove(this);
        computerMove(move.x, move.y);
      }
    }
    setChanged();
    notifyObservers();
  }

  public void computerMove(int row, int col) {
    if (board[row][col] != '_')
      return;

    board[row][col] = 'O';
    setChanged();
    notifyObservers();
  }

  /**
   * Provide a textual version of this tic tac toe board.
   */
  @Override
  public String toString() {
    String result = "";
    for (int r = 0; r < size; r++) {
      for (int c = 0; c < size; c++) {
        result += " " + board[r][c] + " ";
      }
      if (r == 0 || r == 1)
        result += "\n";
    }
    return result;
  }

  /**
   * Provide access to the selections '_' or 'O' or 'X' in a 2D array
   * @return
   */
  public char[][] getTicTacToeBoard() {
    return board;
  }

  public boolean tied() {
    return maxMovesRemaining() == 0 && !didWin('X') && !didWin('O');
  }

  public int maxMovesRemaining() {
    int result = 0;
    for (int r = 0; r < size; r++) {
      for (int c = 0; c < size; c++) {
        if (board[r][c] == '_')
          result++;
      }
    }
    return result;
  }

  public boolean available(int r, int c) {
    return board[r][c] == '_';
  }

  public boolean stillRunning() {
    return !tied() && !didWin('X') && !didWin('O');
  }

  /**
   * Decide if either of the two players have won
   * @param playerChar X or O
   * @return True if X or O wins
   */
  public boolean didWin(char playerChar) {
    return wonByRow(playerChar) || wonByCol(playerChar) || wonByDiagonal(playerChar);
  }

  private boolean wonByRow(char playerChar) {
    for (int r = 0; r < size; r++) {
      int rowSum = 0;
      for (int c = 0; c < size; c++)
        if (board[r][c] == playerChar)
          rowSum++;
      if (rowSum == size)
        return true;
    }
    return false;
  }

  private boolean wonByCol(char playerChar) {
    for (int c = 0; c < size; c++) {
      int colSum = 0;
      for (int r = 0; r < size; r++)
        if (board[r][c] == playerChar)
          colSum++;
      if (colSum == size)
        return true;
    }
    return false;
  }

  private boolean wonByDiagonal(char playerChar) {
    // Check Diagonal from upper left to lower right
    int sum = 0;
    for (int r = 0; r < size; r++)
      if (board[r][r] == playerChar)
        sum++;
    if (sum == size)
      return true;

    // Check Diagonal from upper right to lower left
    sum = 0;
    for (int r = size - 1; r >= 0; r--)
      if (board[size - r - 1][r] == playerChar)
        sum++;
    if (sum == size)
      return true;

    // No win on either diagonal
    return false;
  }
}