package model;

import java.awt.Point;
import java.util.Random;

/**
 * This strategy selects the first available move at random.  It is easy to beat
 * 
 * @throws IGotNowhereToGoException whenever asked for a move that is impossible to deliver
 * 
 * @author David Weinflash
 */
public class RandomAI implements TicTacToeStrategy {

  /**
  * Find an open spot while ignoring possible wins and stops
  */
  @Override
  public Point desiredMove(TicTacToeGame theGame) {
    // Throw IGotNowhereToGoException if no move available
	if (theGame.maxMovesRemaining() == 0)
		throw new IGotNowhereToGoException("I got nowhere to go!");
	
	// Return a random available Point
	
	Random random = new Random();
	  
	int randRow =  random.nextInt(3);
	int randColumn = random.nextInt(3);
	
	// find random, available spot
	while (!theGame.available(randRow, randColumn))
	{
		randRow = random.nextInt(3);
		randColumn = random.nextInt(3);
	}
	
	// random available point
    return new Point(randRow, randColumn);
  }
}