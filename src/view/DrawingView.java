package view;

import java.awt.Point;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import model.TicTacToeGame;


public class DrawingView extends BorderPane implements Observer {

	private TicTacToeGame theGame;
	public boolean endGame;
	private Label stateButton;
	private Image imgO;
	private Image imgX;
	private ArrayList<ArrayList<Point>> squares;
	private GraphicsContext gc;
	
	public DrawingView(TicTacToeGame TicTacToeGame)
	{
		theGame = TicTacToeGame;
		endGame = false;
		
		// Define squares based on coordinates on canvas
		ArrayList<Point> squareOne = new ArrayList<>();
		Point p1_a = new Point(0,0);
		Point p1_b = new Point(66,66);
		squareOne.add(p1_a);
		squareOne.add(p1_b);
		
		ArrayList<Point> squareTwo = new ArrayList<>();
		Point p2_a = new Point(66,0);
		Point p2_b = new Point(132,66);
		squareTwo.add(p2_a);
		squareTwo.add(p2_b);
		
		ArrayList<Point> squareThree = new ArrayList<>();
		Point p3_a = new Point(132,0);
		Point p3_b = new Point(200,66);
		squareThree.add(p3_a);
		squareThree.add(p3_b);
		
		ArrayList<Point> squareFour = new ArrayList<>();
		Point p4_a = new Point(0,66);
		Point p4_b = new Point(66,132);
		squareFour.add(p4_a);
		squareFour.add(p4_b);
		
		ArrayList<Point> squareFive = new ArrayList<>();
		Point p5_a = new Point(66,66);
		Point p5_b = new Point(132,132);
		squareFive.add(p5_a);
		squareFive.add(p5_b);
		
		ArrayList<Point> squareSix = new ArrayList<>();
		Point p6_a = new Point(132,66);
		Point p6_b = new Point(200,132);
		squareSix.add(p6_a);
		squareSix.add(p6_b);
		
		ArrayList<Point> squareSeven = new ArrayList<>();
		Point p7_a = new Point(0,132);
		Point p7_b = new Point(66,200);
		squareSeven.add(p7_a);
		squareSeven.add(p7_b);
		
		ArrayList<Point> squareEight = new ArrayList<>();
		Point p8_a = new Point(66,132);
		Point p8_b = new Point(132,200);
		squareEight.add(p8_a);
		squareEight.add(p8_b);
		
		ArrayList<Point> squareNine = new ArrayList<>();
		Point p9_a = new Point(132,132);
		Point p9_b = new Point(200,200);
		squareNine.add(p9_a);
		squareNine.add(p9_b);
		
		squares = new ArrayList<>();
		squares.add(squareOne);
		squares.add(squareTwo);
		squares.add(squareThree);
		squares.add(squareFour);
		squares.add(squareFive);
		squares.add(squareSix);
		squares.add(squareSeven);
		squares.add(squareEight);
		squares.add(squareNine);
		
		Group root = new Group();
    	Canvas canvas = new Canvas(200,200);
    	EventHandler<MouseEvent> click = new CanvasHandler();
    	canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, click);
    	gc = canvas.getGraphicsContext2D();
    	root.getChildren().add(canvas);
		
    	// draw board on canvas
        gc.setStroke(Color.web("#0076a3"));
        gc.setLineWidth(2);
        gc.strokeRoundRect(0, 0, 200, 200, 0, 0);
    	gc.strokeLine(66,0,66,200);
    	gc.strokeLine(132, 0, 132, 200);
    	gc.strokeLine(0, 66, 200, 66);
    	gc.strokeLine(0, 132, 200, 132);
    	
    	// import X and O images
    	imgO = new Image("file:images/o.png", false);
    	imgX = new Image("file:images/x.png", false);
    	

    	
    	// set canvas
        BorderPane.setAlignment(canvas, Pos.CENTER);
        BorderPane.setMargin(canvas, new Insets(0, 0, 35, 0));
        this.setCenter(canvas);
        	
		// set Make Move text
		stateButton = new Label("Make Move");
	    stateButton.setFont(new Font("Arial", 14));
	    stateButton.setTextFill(Color.web("#0076a3"));
	    BorderPane.setAlignment(stateButton, Pos.TOP_CENTER);
	    BorderPane.setMargin(stateButton, new Insets(35, 0, 35, 0));
	    this.setTop(stateButton);

	}
	
	@Override
	public void update(Observable o, Object arg1) {

		if (arg1 != null && arg1.equals("startNewGame()"))
		{
			endGame = false;
			stateButton.setText("Make move");
			
			// clear and re-draw board
			gc.clearRect(0, 0, 200, 200);
	        gc.strokeRoundRect(0, 0, 200, 200, 0, 0);
	    	gc.strokeLine(66,0,66,200);
	    	gc.strokeLine(132, 0, 132, 200);
	    	gc.strokeLine(0, 66, 200, 66);
	    	gc.strokeLine(0, 132, 200, 132);
		}
		
		// Do not draw on board if game has already ended
		if ((theGame.stillRunning() == false) && (endGame == true))
			return;
		
		char[][] board = theGame.getTicTacToeBoard();
		
		// list of squares with coordinates for drawing in center of square
		// square one is first entry, top left of board
		int[][] squareCoords = {{18, 18}, {84, 18}, {150, 18},
								{18, 84}, {84,84}, {150, 84},
								{18,150}, {84,150}, {150,150}};
    	
		char symbol;
		int squareCoordsIdx = -1;
		int xCoord;
		int yCoord;
		
		// draw Xs and Os on canvas to match theGame board
		for (int r = 0; r < 3; r++)
		{
			for (int c = 0; c < 3; c++)
			{
				squareCoordsIdx++;
				if (!theGame.available(r, c))
				{
					symbol = board[r][c];
					xCoord = squareCoords[squareCoordsIdx][0];
					yCoord = squareCoords[squareCoordsIdx][1];
					if (symbol == 'X')
						gc.drawImage(imgX, xCoord, yCoord);
					else
						gc.drawImage(imgO, xCoord, yCoord);
				}
			}
		}
		
		if (theGame.tied())
		{
			stateButton.setText("Tie");
			endGame = true;
		}
		
		if (theGame.didWin('X'))
		{
			stateButton.setText("X wins");
			endGame = true;
		}
		
		if (theGame.didWin('O'))
		{
			stateButton.setText("O wins");
			endGame = true;
		}
		
	}
	
	private class CanvasHandler implements EventHandler<MouseEvent> 
	{

		@Override
		public void handle(MouseEvent e) {
			
			// Do not draw on theGame if game has already ended
			if ((theGame.stillRunning() == false) && (endGame == true))
				return;
			
			int clickX = (int) e.getX();
			int clickY = (int) e.getY();
			
			// 2D int array where pos num is square on board
			int[][] squaresRowCol = {{0,0}, {0,1}, {0,2},
									{1,0}, {1,1}, {1,2},
									{2,0}, {2,1}, {2,2}};
			
			// find square that corresponds with mouse click
			int squareNum = 0;
			ArrayList<Point> tempPoint;
			Point upperLeft;
			Point lowerRight;
			int x1, y1;
			int x2, y2;
			
			for (int i = 0; i < squares.size(); i++)
			{
				tempPoint = squares.get(i);
				upperLeft = tempPoint.get(0);
				lowerRight = tempPoint.get(1);
				x1 = (int) upperLeft.getX();
				y1 = (int) upperLeft.getY();
				x2 = (int) lowerRight.getX();
				y2 = (int) lowerRight.getY();
				
				if ((clickX >= x1 && clickY >= y1) && (clickX < x2 && clickY < y2))
				{
					squareNum = i;
					break;
				}
				
			}
			
			int row = squaresRowCol[squareNum][0];
			int col = squaresRowCol[squareNum][1];
			
			theGame.humanMove(row, col, false);
			
		}
		
	}

}
