package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

/**
 * This TTT strategy tries to prevent the opponent from winning by checking
 * for a space where the opponent is about to win. If none found, it randomly
 * picks a place to win, which an sometimes be a win even if not really trying.
 * 
 * @author David Weinflash
 */
public class IntermediateAI implements TicTacToeStrategy {

  @Override
  // Precondition: During testing the AI is associated with the 'O', the odd number move.
  public Point desiredMove(TicTacToeGame theGame) {
    // Return a Point that would win, if not possible to block,
    // if not possible to block return an available Point in the corners of the board
	// if corners occupied, return any available point
	  
	char[][] board = theGame.getTicTacToeBoard();
	  
	// Add row/column to list if 2 'Xs' found in row/column
	ArrayList<Integer> rowsToBlock = new ArrayList<>();
	ArrayList<Integer> colsToBlock = new ArrayList<>();
	
	// Upper Left to Lower Right
	boolean ULLRtoBlock = false;
	// Upper Right to Lower Left
	boolean URLLtoBlock = false;
	
	int xFound;
	int oFound;
	
	// search for row win
	int openColumn;
	for (int r = 0; r < 3; r++)
	{
		xFound = 0;
		oFound = 0;
		for (int c = 0; c < 3; c++)
		{
			if (board[r][c] == 'O')
				oFound++;
			if (board[r][c] == 'X')
				xFound++;
			if (oFound == 2)
			{
				openColumn = this.findRowSpot(theGame, r);
				if(openColumn != -1)
					return new Point(r, openColumn);
			}
			if (xFound == 2)
				rowsToBlock.add(r);
		}
	}
	
	// search for column win
	int openRow;
	for (int c = 0; c < 3; c++)
	{
		xFound = 0;
		oFound = 0;
		for (int r = 0; r < 3; r++)
		{
			if (board[r][c] == 'O')
				oFound++;
			if (board[r][c] == 'X')
				xFound++;
			if (oFound == 2)
			{
				openRow = this.findColumnSpot(theGame, c);
				if (openRow != -1)
					return new Point(openRow, c);
			}
			if (xFound == 2)
				colsToBlock.add(c);
		}
	}
	
	// search for ULLR diagonal win
	xFound = 0;
	oFound = 0;
	int openDiag;
	for (int d = 0; d < 3; d++)
	{
		if (board[d][d] == 'O')
			oFound++;
		if (board[d][d] == 'X')
			xFound++;
		if (oFound == 2)
		{
			openDiag = this.findULLR(theGame);
			if (openDiag != -1)
				return new Point(openDiag, openDiag);
		}
		if (xFound == 2)
			ULLRtoBlock = true;
	}
	  
	// search for URLL diagonal win
	xFound = 0;
	oFound = 0;
	for (int d = 2; d >= 0; d--)
	{
		if (board[3-d-1][d] == 'O')
			oFound++;
		if (board[3-d-1][d] == 'X')
			xFound++;
		if (oFound == 2)
		{
			openDiag = this.findURLL(theGame);
			if (openDiag != -1)
				return new Point(3-openDiag-1, openDiag);
		}
		if (xFound == 2)
			URLLtoBlock = true;
	}
	
	// search for row to block
	int row = 0;
	for (int i = 0; i < rowsToBlock.size(); i++)
	{
		row = rowsToBlock.get(i);
		openColumn = this.findRowSpot(theGame, row);
		if (openColumn != -1)
			return new Point(row, openColumn);
	}
	
	// search for column to block
	int column = 0;
	for (int i = 0; i < colsToBlock.size(); i++)
	{
		column = colsToBlock.get(i);
		openRow = this.findColumnSpot(theGame, column);
		if (openRow != -1)
			return new Point(openRow, column);
	}
	
	// search for ULLR to block
	int d;
	if (ULLRtoBlock == true)
	{
		d = this.findULLR(theGame);
		if (d != -1)
			return new Point(d,d);
	}
	
	// search for URLL to block
	if (URLLtoBlock == true)
	{
		d = this.findURLL(theGame);
		if (d != -1)
			return new Point(3-d-1,d);
	}
	
	
	// search for available corner on board
	// begin upper right corner, searching clockwise
	
	int[] rows = {0, 2, 2, 0};
	int[] cols = {2, 2, 0, 0};
	
	// find available corner
	for (int i = 0; i < 4; i++)
	{
		row = rows[i];
		column = cols[i];
		if (theGame.available(row, column))
			return new Point(row,column);
	}
	
	// choose any available spot
	for (int r = 0; r < 3; r++)
	{
		outerloop:
		for (int c = 0; c < 3; c++)
		{
			row = r;
			column = c;
			if (theGame.available(r, c))
				break outerloop;
		}
	}
	
	return new Point(row, column);
	
  }
  
  private int findURLL(TicTacToeGame theGame)
  {
	  // run this method when 2 'Os' or 'Xs' found in URLL diagonal
	  // return available diagonal spot, else return -1 if none exist
	  
	  for (int d = 2; d >= 0; d--)
	  {
		  if (theGame.available(3-d-1, d))
			  return d;
	  }
	  
	  return -1;
  }
  
  private int findULLR(TicTacToeGame theGame)
  {
	  // run this method when 2 'Os' or 'Xs' found in ULLR diagonal
	  // return available diagonal spot, else return -1 if none exist
	  
	  for (int d = 0; d < 3; d++)
	  {
		  if (theGame.available(d, d))
			  return d;
	  }
	  
	  return -1;
  }
  
  private int findColumnSpot(TicTacToeGame theGame, int column)
  {
	  // run this method when 2 'Os' or 'Xs' found in column
	  // return available row num, else return -1 if none exist
	  
	  for (int row = 0; row < 3; row++)
	  {
		  if (theGame.available(row, column))
			  return row;
	  }
	  
	  return -1;
  }
  
  private int findRowSpot(TicTacToeGame theGame, int row)
  {
	  // run this method when 2 'Os' or 'Xs' found in row
	  // return available column num, else return -1 if none exist
	  
	  for (int column = 0; column < 3; column++)
	  {
		  if (theGame.available(row, column))
			  return column;
	  }
	  
	  return -1;
  }
  
}