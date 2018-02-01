package view;

import java.util.Observable;
import java.util.Observer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import model.TicTacToeGame;

/**
 * This TextAreaView provides a text area for the player to input moves.
 * The view includes a text area, text fields and button to select a move.
 * Illegal moves will be notified in the button text.
 * 
 * @author David Weinflash
 */

public class TextAreaView extends BorderPane implements Observer {
   
  private TicTacToeGame theGame;
  private GridPane topGrid;
  private TextField rowField;
  private TextField columnField;
  private Button button;
  private TextArea textArea;
  private boolean endGame;

  public TextAreaView(TicTacToeGame TicTacToeGame) {
    theGame = TicTacToeGame;
    endGame = false;
    
	/**
	* Construct a grid and place on top of BorderPane.
	* Include text fields and button for player to select a move in grid.
	* Construct a text area and display the current game on bottom of BorderPane.
	*/
    
	// grid
	topGrid = new GridPane();
	topGrid.setAlignment(Pos.CENTER);
	topGrid.setHgap(2);
	topGrid.setVgap(10);
	
	// set alignment
    BorderPane.setAlignment(topGrid, Pos.TOP_LEFT);
    BorderPane.setMargin(topGrid, new Insets(30, 30, 0, 30));
    this.setTop(topGrid);
    
    // text area
    textArea = new TextArea();
    textArea.setMaxHeight(200);
    Font myFont = new Font("Courier", 40);
    textArea.setFont(myFont);
    
    this.setBottom(textArea);
    textArea.setText(theGame.toString());
    
    initializePane();
  }

  private void initializePane() {
    
	/**
	* Add the lables and text fields to the grid pane.
	* Add button handler to button to register moves.
	* Add key event handler to text fields to update button with 'Make Move'
	*/
	  
	// row label and text box
	Label row = new Label("row");
	topGrid.add(row, 1, 0);
	
	rowField = new TextField();
	rowField.setMaxWidth(60);
	topGrid.add(rowField, 0, 0);
	
	EventHandler<? super KeyEvent> handlerText = new TextFieldListener();
	rowField.setOnKeyPressed(handlerText);
	
	// column label and text box
	Label column = new Label("column");
	topGrid.add(column, 1, 1);
	
	columnField = new TextField();
	topGrid.add(columnField, 0, 1);
	columnField.setMaxWidth(60);
	
	columnField.setOnKeyPressed(handlerText);
	
	// button
	Font myFont = new Font("Courier", 11);
	button = new Button();
	button.setFont(myFont);
	button.setText("Make move");
	topGrid.add(button,0,2);
	
	ButtonListener handler = new ButtonListener();
	button.setOnAction(handler);
  }

  private class TextFieldListener implements EventHandler<KeyEvent> {

	/**
	* The button text will be set to 'Make Move' whenever a key is entered
	* in text box, so a user will never have to press 'Invalid Move' when making
	* a new move.
	*/
	  
	@Override
	public void handle(KeyEvent event) {
		if (theGame.stillRunning() == false)
			return;
			
		button.setText("Make move");
	}
	  
  }
  
  private class ButtonListener implements EventHandler<ActionEvent> {

	/**
	* Set button text to indicate if the player should make a new move.
	* If a player wins, indicate winner in button text.
	* If player enters an invalid move, indicate so in button text.
	*/
	  
	@Override
	public void handle(ActionEvent arg0) {
		
		int row;
		int column;
		
		if (theGame.stillRunning() == false)
			return;
		
		try // row and column must be integer
		{
			row = Integer.parseInt(rowField.getText());
			column = Integer.parseInt(columnField.getText());
		}
		catch (Exception e)
		{
			button.setText("Invalid choice");
			return;
		}
		
		// bounds of row and column 0 based
		if ((row < 0 || row > 2) || (column < 0 || column > 2))
		{
			button.setText("Invalid choice");
			return;
		}
		
		// spot not available
		if (!theGame.available(row, column))
		{
			button.setText("Invalid choice");
			return;
		}
		
		// valid input
		rowField.setText("");
		columnField.setText("");
		
		theGame.humanMove(row, column, false);
		
		if (theGame.tied())
		{
			button.setText("Tie");
			endGame = true;
			return;
		}
		
		if (theGame.didWin('X'))
		{
			button.setText("X wins");
			endGame = true;
			return;
		}
		
		if (theGame.didWin('O'))
		{
			button.setText("O wins");
			endGame = true;
			return;
		}
		
		button.setText("Make move");
		
	}
	  
  }
  
  @Override
  public void update(Observable o, Object arg) {
    
	/**
	* Once the game sends an update, check for 'new game' option or
	* if a player has won the game. Print the board to stdout for debugging
	* purposes.
	*/
	  
	textArea.setText(theGame.toString());
	
	if (arg != null && arg.equals("startNewGame()"))
	{
		endGame = false;
		button.setText("Make move");
		rowField.setText("");
		columnField.setText("");
	}

	if (theGame.tied())
		button.setText("Tie");
	
	if (theGame.didWin('X'))
		button.setText("X wins");

	if (theGame.didWin('O'))
		button.setText("O wins");
	
    System.out.println("\nIn TextAreaView.update() \n" + o);
  }
}