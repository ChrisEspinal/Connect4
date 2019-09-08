import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;



import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class Connect4 extends JFrame{
		private JPanel jpMain;
		C4Player player1;
		C4Player player2;
		C4Player currPlayer;
		Connect4Board connect4Board;
		ScoreBoard scoreBoard;
		
		public Connect4() {
			jpMain = new JPanel();
			jpMain.setLayout(new BorderLayout());
			
			String player1Name = JOptionPane.showInputDialog(null, "Enter Player1 Name:");
			String player2Name = JOptionPane.showInputDialog(null, "Enter Player2 Name:");
			player1 = new C4Player(player1Name, " ");
			player2 = new C4Player(player2Name, " 	");
			currPlayer = player1;
			
			scoreBoard = new ScoreBoard();
			connect4Board = new Connect4Board();
			
			jpMain.add(scoreBoard, BorderLayout.NORTH);
			jpMain.add(connect4Board, BorderLayout.CENTER);
			add(jpMain);
			pack();
			setSize(700,500);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setVisible(true);
		}
		
		
		
		private class ScoreBoard extends JPanel{
			private JLabel lblChamp, lblChampTemp,lblLastWinner, lblLastWinnerTemp, 
			lblPlaceHolder, lblPlayerNames, 
			lblPlayerNumWins,lblPlayer1Name,lblPlayer2Name,
			lblPlayer1NumWins,lblPlayer2NumWins, lblCurrentPlayer, lblCurrentPlayerName;
			
			JPanel jpScoreInfo, jpPlayerScoreInfo, jpCurrentPlayerInfo;
			
			public ScoreBoard(){
				setLayout(new BorderLayout());
				jpScoreInfo = new JPanel();
				jpScoreInfo.setLayout(new GridLayout(2,2));
				jpScoreInfo.setBackground(Color.YELLOW);
				lblChamp = new JLabel("Champion");
				lblChampTemp = new JLabel("----------");
				lblLastWinner = new JLabel("Last Winner");
				lblLastWinnerTemp = new JLabel("----------");
				jpScoreInfo.add(lblChamp);
				jpScoreInfo.add(lblChampTemp);
				jpScoreInfo.add(lblLastWinner);
				jpScoreInfo.add(lblLastWinnerTemp);
				
				jpPlayerScoreInfo = new JPanel();
				jpPlayerScoreInfo.setLayout(new GridLayout(3,3));
				jpPlayerScoreInfo.setBackground(Color.CYAN);
				lblPlaceHolder = new JLabel(" ");
				lblPlayerNames  = new JLabel("Name");
				lblPlayerNumWins = new JLabel("Score");
				lblPlayer1Name = new JLabel(player1.getName());
				lblPlayer2Name = new JLabel(player2.getName());
				lblPlayer1NumWins = new JLabel(""+player1.getNumWins());
				lblPlayer2NumWins = new JLabel(""+player2.getNumWins());

				jpPlayerScoreInfo.add(lblPlaceHolder);
				jpPlayerScoreInfo.add(new JLabel("Player 1"));
				jpPlayerScoreInfo.add(new JLabel("Player 2"));
				jpPlayerScoreInfo.add(lblPlayerNames);
				jpPlayerScoreInfo.add(lblPlayer1Name);
				jpPlayerScoreInfo.add(lblPlayer2Name);
				jpPlayerScoreInfo.add(lblPlayerNumWins);
				jpPlayerScoreInfo.add(lblPlayer1NumWins);
				jpPlayerScoreInfo.add(lblPlayer2NumWins);
				
				jpCurrentPlayerInfo = new JPanel();
				jpCurrentPlayerInfo.setLayout(new GridLayout(1,2));
				jpCurrentPlayerInfo.setBackground(Color.GRAY);
				lblCurrentPlayer = new JLabel("Current Player");
				lblCurrentPlayerName = new JLabel(player1.getName());
				jpCurrentPlayerInfo.add(lblCurrentPlayer);
				jpCurrentPlayerInfo.add(lblCurrentPlayerName);
				
				
				
				add(jpScoreInfo, BorderLayout.NORTH);
				add(jpPlayerScoreInfo, BorderLayout.CENTER);
				add(jpCurrentPlayerInfo, BorderLayout.SOUTH);
				
			}
		}
		
		private class Connect4Board extends JPanel implements ActionListener {
			private JButton [][] board;
			private static final int NUM_COLS = 7;
			
			public Connect4Board(){
				setLayout(new GridLayout(6,7));
				displayBoard();
				
			}

			public void clearBoard() {
				for(int row=0; row<board.length; row++){
					for(int col=0; col<board[row].length; col++){
						board[row][col].setText("");
						board[row][col].setBackground(Color.WHITE);
						board[row][col].setEnabled(true);
					}
				}
			}

			public void displayBoard() {
				board = new JButton[6][7];
				for(int row=0; row<board.length; row++){
					for(int col=0; col<board[row].length; col++){
						board[row][col] = new JButton("");
						board[row][col].setBackground(Color.WHITE);
						Font bigF = new Font(Font.SANS_SERIF, Font.BOLD, 30);
						board[row][col].setFont(bigF);
						board[row][col].addActionListener(this);
						board[row][col].setEnabled(true);
						add(board[row][col]);
					}
				}				
			}
			
			public int getColumn(Object c) {
				int column = -1;
				for(int row=0; row<board.length; row++){
					for(int col=0; col<board[row].length; col++){
						if(c.equals(board[row][col])) {
							column = col;
						}
					}
				}
				return column;
			}
			
			public void placeMarker(JButton[][] jbArr, int column) {
				int col = column;
				while(col<=jbArr.length) {
					for(int row = jbArr.length-1; row>=0; row--) {
						if(jbArr[row][col].getText().equals("")) {
							jbArr[row][col].setText(currPlayer.getMarker());
							if(jbArr[row][col].getText() == player1.getMarker()) {
								board[row][col].setBackground(Color.CYAN);
								board[row][col].setEnabled(false);
							}else {
								board[row][col].setBackground(Color.YELLOW);
								board[row][col].setEnabled(false);
							}
							break;
						}
					}break;
				}
			}
			
			public void takeTurn() {
				if(currPlayer.equals(player1)){
					currPlayer = player2;
					scoreBoard.lblCurrentPlayerName.setText(currPlayer.getName());
				}
				else{
					currPlayer = player1;
					scoreBoard.lblCurrentPlayerName.setText(currPlayer.getName());
				}
			}

			public void displayWinner() {
				int playAgain = JOptionPane.showConfirmDialog(null, "Do you want to play again?", "Winner is " + currPlayer.getName() + "!", JOptionPane.YES_NO_OPTION);
				if(playAgain == JOptionPane.YES_OPTION) {
				}else{
					currPlayer.addWin();
					writeResultsToFile_Exit();
					System.exit(0);		
				}
				currPlayer.addWin();
				scoreBoard.lblLastWinnerTemp.setText(currPlayer.getName());
				
				int player1Ws = player1.getNumWins();
				int player2Ws = player2.getNumWins();
				if(player1Ws > player2Ws) {
					scoreBoard.lblChampTemp.setText(player1.getName());
				}else if (player1Ws < player2Ws){
					scoreBoard.lblChampTemp.setText(player2.getName());
				}else {
					scoreBoard.lblChampTemp.setText("----------");
				}
				
				if(currPlayer.equals(player1)){
					scoreBoard.lblPlayer1NumWins.setText(""+player1.getNumWins());
				}else{
					scoreBoard.lblPlayer2NumWins.setText(""+player2.getNumWins());
				}
				
			}

			public boolean isFull() {
				for(int row=0; row<board.length; row++){
					for(int col=0; col<board[row].length; col++){
						if(board[row][col].getText().equals("")){
							return false;
						}
					}
				}
				return true;
			}

			public boolean isWinner(String currPlayerSymbol) {
				if(isWinnerInRow() || isWinnerInCol() || isWinnerInDiagLeft() || isWinnerInDiagRight()){
					return true;

				}
				return false;
			}

			public boolean isWinnerInRow(){
				for(int row=0; row<board.length; row++){
					int numMatchesInRow = 0;
					for(int col=0; col<board[row].length; col++){
						if(board[row][col].getText().equals(currPlayer.getMarker())){
							numMatchesInRow++;
							if(numMatchesInRow == 4){
								return true;
							}
						}else{
							numMatchesInRow = 0;
						}	
					}		
				}
				return false;
			}		
				
			public boolean isWinnerInCol(){
				for(int col=0; col<NUM_COLS; col++){
					int numMatchesInCol = 0;
					for(int row=0; row<board.length; row++){
							if(board[row][col].getText().equals(currPlayer.getMarker())){
								numMatchesInCol++;
								if(numMatchesInCol == 4){
									return true;
								}
							}else{
								numMatchesInCol = 0;
							}
					}
				}
				return false;
			}  
				
			public boolean isWinnerInDiagLeft(){			
				for(int row=5; row>2; row--){
					for(int col=6; col>2; col--){
							if(board[row][col].getText().equals(currPlayer.getMarker())){
								if(board[row-1][col-1].getText().equals(currPlayer.getMarker())){
									if(board[row-2][col-2].getText().equals(currPlayer.getMarker())){
										if(board[row-3][col-3].getText().equals(currPlayer.getMarker())){
											return true;
										}
									}
								}		
							}
					}
				}
				return false;
			}		
			

			public boolean isWinnerInDiagRight(){
				for(int row=5; row>2; row--){
					for(int col=0; col<4; col++){ 
							if(board[row][col].getText().equals(currPlayer.getMarker())){
								if(board[row-1][col+1].getText().equals(currPlayer.getMarker())){
									if(board[row-2][col+2].getText().equals(currPlayer.getMarker())){
										if(board[row-3][col+3].getText().equals(currPlayer.getMarker())){
											return true;
										}
									}
								}		
							}
					}
				}
				return false;
			}
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton btnClicked = (JButton)e.getSource();
				int column = getColumn(btnClicked);
				placeMarker(board, column);
				writeResultsToFile_Winner();

				if(isWinner(currPlayer.getMarker())){
					displayWinner();
					clearBoard();
					
				}else if(isFull()){
					int playAgain = JOptionPane.showConfirmDialog(null, "Play Again?", "DRAW", JOptionPane.YES_NO_OPTION);
					if(playAgain == JOptionPane.YES_OPTION) {
						writeResultsToFile_Draw();
						clearBoard();
					}else{
						writeResultsToFile_Draw();
						writeResultsToFile_Exit();
						System.exit(0);
					}
				}
				takeTurn();
			}
			
			
			public void writeResultsToFile_Winner(){
				PrintWriter outStream =null;
				try {
					FileOutputStream fileOut = new FileOutputStream("Connect4-Results.txt", true);
					outStream = new PrintWriter(fileOut);
 
					if(isWinner(currPlayer.getMarker())) {
						outStream.println("Winner: " + currPlayer.getName() + "!"); 
					}
					
				}catch (FileNotFoundException e) {
					e.printStackTrace();

				}
				finally{
					if(outStream != null){
						outStream.close(); 
					}
				}	
			}
			
			public void writeResultsToFile_Exit(){
				PrintWriter outStream = null;
				try {
					FileOutputStream fileOut = new FileOutputStream("Connect4-Results.txt", true);
					outStream = new PrintWriter(fileOut);
					outStream.println("Champion: " + currChampion() + "!!!!!!!");
					outStream.println(player1.getName() + " won " + player1.getNumWins() + " game/s this round.");
					outStream.println(player2.getName() + " won " + player2.getNumWins() + " game/s this round.");
				
				}catch (FileNotFoundException e) {
					e.printStackTrace();

				}
				finally{
					if(outStream != null){
						outStream.close();
					}
					System.out.println("All done with writeToNewFile method");
				}	
			}
			
			public void writeResultsToFile_Draw(){
				PrintWriter outStream = null;
				try {
					FileOutputStream fileOut = new FileOutputStream("Connect4-Results.txt", true);
					outStream = new PrintWriter(fileOut);
					outStream.println("Draw, no winner.");
					
				}catch (FileNotFoundException e) {
					e.printStackTrace();

				}
				finally{
					if(outStream != null){
						outStream.close();
					}
				}	
			}
			
		public String currChampion() {
			
			int player1Wins = player1.getNumWins();
			int player2Wins = player2.getNumWins();
			if(player1Wins > player2Wins) {
				return player1.getName();
			}else if (player1Wins < player2Wins){
				return player2.getName();
			}else {
				return "No current Champion.";
			}
		}
		
		
			
				
	}
		
}

final class C4Player {

	private String name;
	private String marker;
	private int numGames;
	private int numWins;
	private int numLosses;
	private int numDraws;
	
	public C4Player() {
		name = "John Doe";
		marker = "?";
		numGames = 0;
		numWins = 0;
		numLosses = 0;
		numDraws = 0;
	}	
	public C4Player(String name){
		this();
		this.name = name;
	}
	public C4Player(String name, String marker){
		this();
		this.name = name;
		this.marker = marker;
	}
	protected void addWin(){
		numGames++;
		numWins++;
	}
	protected void addLoss(){
		numGames++;
		numLosses++;
	}
	protected void addDraw(){
		numGames++;
		numDraws++;
	}
	public String getName() {
		return name;
	}
	public String getMarker() {
		return marker;
	}
	public int getNumGames() {
		return numGames;
	}
	public int getNumWins() {
		return numWins;
	}
	public int getNumLosses() {
		return numLosses;
	}
	public int getNumDraws() {
		return numDraws;
	}	
				
}

final class ShowConnect4GUI {
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater( new Runnable(){
			@Override
			public void run() {
				Connect4 gui = new Connect4();
			}
		} );
	}
}
