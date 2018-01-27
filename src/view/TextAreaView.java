package view;

import java.util.Observable;
import java.util.Observer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import model.TicTacToeGame;

public class TextAreaView extends BorderPane implements Observer {
   
  private TicTacToeGame theGame;
  private GridPane topGrid;
  private TextField rowField;
  private TextField columnField;
  private Button button;
  private TextArea textArea;

  public TextAreaView(TicTacToeGame TicTacToeGame) {
    theGame = TicTacToeGame;
    
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
    
	// row label and text box
	Label row = new Label("row");
	topGrid.add(row, 1, 0);
	
	rowField = new TextField();
	rowField.setMaxWidth(60);
	topGrid.add(rowField, 0, 0);
	
	// column label and text box
	Label column = new Label("column");
	topGrid.add(column, 1, 1);
	
	columnField = new TextField();
	topGrid.add(columnField, 0, 1);
	columnField.setMaxWidth(60);
	
	// button
	Font myFont = new Font("Courier", 11);
	button = new Button();
	button.setFont(myFont);
	button.setText("Make move");
	topGrid.add(button,0,2);
  }

  @Override
  public void update(Observable o, Object arg) {
    // TODO Auto-generated method stub
	textArea.setText(theGame.toString());
    System.out.println("\nIn TextAreaView.update() \n" + o);
  }
}