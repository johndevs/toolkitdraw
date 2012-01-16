package fi.jasoft.toolkitdraw.demos;

import java.awt.Color;


import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import fi.jasoft.flashcanvas.FlashCanvas;
import fi.jasoft.flashcanvas.FlashCanvas.ClickListener;
import fi.jasoft.flashcanvas.FlashCanvas.Graphics;
import fi.jasoft.flashcanvas.enums.CacheMode;

public class TicTacToe extends Window implements ClickListener {

	private static final long serialVersionUID = 1L;

	private FlashCanvas canvas;
	
	private int[][] board = new int[3][3];
		
	public TicTacToe(){
		super("Tic-Tac-Toe Game");
		setWidth("400px");
		setHeight("400px");		
		setModal(true);			
		setResizable(false);		
		
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		
		//Create a paintcanvas 
		canvas = new FlashCanvas("300px","300px",  new Color(34,34,34));	
		
		//Disable drawing on the canvas
		canvas.setInteractive(false);
		
		//Disable caching
		canvas.setCacheMode(CacheMode.NONE);		
		
		//Enable batch mode
		canvas.getGraphics().setBatchMode(true);
								
		//Add clicklistener
		canvas.addListener(this);
		
		layout.addComponent(canvas);
		layout.setComponentAlignment(canvas, com.vaadin.ui.Alignment.MIDDLE_CENTER);
		
		setContent(layout);
		
		render();
	}
	
	private void render(){
		
		Graphics gc = canvas.getGraphics();
		
		//Remove previous renderings		
		gc.clear();
		
		//Draw the board
		
		gc.drawLine(100, 10, 100, 290, Color.RED);
		gc.drawLine(200, 10, 200, 290, Color.RED);
		gc.drawLine(10, 100, 290, 100, Color.RED);
		gc.drawLine(10, 200, 290, 200, Color.RED);
		
		//Draw the marks
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				int x = 50+i*100;
				int y = 50+j*100;
				if(board[i][j] == 1){
					int[] xarr = new int[]{ x-25, x-20, x ,  x+20, x+25, x+5, x+25,    };
					int[] yarr = new int[]{ y-30, y-20, y-5, y-20, y-30, y,   y+30,  };
					
					gc.drawPolygon(xarr, yarr, Color.BLACK, Color.GREEN);
				} else if(board[i][j] == 2){
					gc.drawEllipse(x, y, 50, 50, Color.BLACK, Color.RED);
				}
			}
		}	
		
		//Send the new drawing instructions
		gc.sendBatch();
	}
	
	private void analyze(){
		boolean player_won = false;
		boolean comp_won = false;
		
		if(board[0][0] == 1 && board[0][1] == 1 && board[0][2] == 1) player_won = true;
		else if(board[1][0] == 1 && board[1][1] == 1 && board[1][2] == 1) player_won = true;
		else if(board[2][0] == 1 && board[2][1] == 1 && board[2][2] == 1) player_won = true;
		else if(board[0][0] == 1 && board[1][0] == 1 && board[2][0] == 1) player_won = true;
		else if(board[0][1] == 1 && board[1][1] == 1 && board[2][1] == 1) player_won = true;
		else if(board[0][2] == 1 && board[1][2] == 1 && board[2][2] == 1) player_won = true;
		else if(board[0][2] == 1 && board[1][1] == 1 && board[2][0] == 1) player_won = true;
		else if(board[0][0] == 1 && board[1][1] == 1 && board[2][2] == 1) player_won = true;
		
		if(board[0][0] == 2 && board[0][1] == 2 && board[0][2] == 2) comp_won = true;
		else if(board[1][0] == 2 && board[1][1] == 2 && board[1][2] == 2) comp_won = true;
		else if(board[2][0] == 2 && board[2][1] == 2 && board[2][2] == 2) comp_won = true;
		else if(board[0][0] == 2 && board[1][0] == 2 && board[2][0] == 2) comp_won = true;
		else if(board[0][1] == 2 && board[1][1] == 2 && board[2][1] == 2) comp_won = true;
		else if(board[0][2] == 2 && board[1][2] == 2 && board[2][2] == 2) comp_won = true;
		else if(board[0][2] == 2 && board[1][1] == 2 && board[2][0] == 2) comp_won = true;
		else if(board[0][0] == 2 && board[1][1] == 2 && board[2][2] == 2) comp_won = true;
		
		if(player_won){
			System.out.println("Player won");
			board = new int[3][3];
			this.showNotification("Game won!", "You have won!!");
		} else if(comp_won){
			System.out.println("Computer won");
			board = new int[3][3];
			this.showNotification("Game lost!", "You lost, ha-haa!");
		}		
	}

	public void onClick(Component component, int x, int y) {
		
		int i = (int)Math.floor((float)x/100f);
		int j = (int)Math.floor((float)y/100f);
		
		//Check that the choice is not taken
		if(board[i][j] != 0) return;
		
		//Mark the users choice		
		board[i][j] = 1;
				
		//Computer players
		boolean done = false;
		int counter = 0;
		while(!done){
			counter = 0;
			for(i=0; i<3; i++){
				for(j=0; j<3; j++){
					if(board[i][j] != 0) continue;
					counter++;
					if(Math.random() < 0.5){
						done = true;
						board[i][j] = 2;
						break;
					}
				}
				if(done) break;
			}
			if(counter == 0) break;
		}
		
		//Analyze the board for a win situation
		analyze();
		
		//Render
		render();		
	}
}
