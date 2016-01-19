/**
 * Game where the actual game is played
 * Handles drawing of the board and for processing mouse/keyboard input
 * @author Tony Li, Charles Lei
 */
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;
import javax.imageio.ImageIO;
import java.awt.image.*;
public class GameScreen extends JFrame implements MouseListener, MouseMotionListener, KeyListener
{
	//variables so that we can easily change board size and dimensions
	public static final int BOARD_W = 8;
	public static final int BOARD_H = 8;
	public static final int SQUARE_SIZE = 50;
	public static final int EDGE_SPACE = 75;
	
	//variable to track how much time since AI last made a move
	int prevMove = 0;
	
	//the time between moves of the AI
	public static int COMP_TIME;
	
	static Piece[][] board;
	
	//player instances for computer and human player
	static AI comp;
	static AI human;
	
	DetectAction da;
	
	Timer tm;
	ActionListener al;
	BoardPanel boardPnl;
	
	JButton btnMenu;
	JButton btnOptions;
	JLabel message;
	
	//variables to store the pixel and board coordinates of the mouse
	int mouseX = -9999;
	int mouseY = -9999;
	int mouseSqX;
	int mouseSqY;
	
	/**
	 * stores all the keyboard controls, saved in a file
	 * the indexes correspond with the char that must be typed as follows:
	 * 0 -> pause/options
	 * 1 -> select next pawn
	 * 2 -> select next bishop
	 * 3 -> select next knight
	 * 4 -> select next rook
	 * 5 -> select queen
	 * 6 -> select king
	 * 7 -> take a piece with the one selected, if possible
	 */
	char[] hotkeyControls;
	
	/**
	 * initializes the game screen
	 * @param boolean swap - true to make the user start as black
	 * @param int difficulty - integer difficulty level of AI
	 * @author Tony Li
	 *    #Tony
	 */
	public GameScreen(boolean swap, int difficulty)
	{
		super("RTC");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//#error
		//get window bar icon
		try
		{
			this.setIconImage(ImageIO.read(new File("Piece Images/1Knight.png")));
		}
		catch(Exception e)
		{
		}//end try catch
		
		//#error
		//#read
		//get hotkey configuration from file
		try
		{
			Scanner reading = new Scanner(new File("hotkeys.txt"));
			hotkeyControls = reading.nextLine().toCharArray();
			//make sure there are 8 hotkeys
			int assert_ = hotkeyControls[7];
			//make sure none of them are repeated
			for (int e = 0; e < 8; e++)
			{
				for (int ee = e + 1; ee < 8; ee++)
				{
					if (hotkeyControls[e] == hotkeyControls[ee])
					{
						//throw an exception
						throw new ArrayIndexOutOfBoundsException();
					}//end if
				}//end for
			}//end for
		}
		catch (Exception ex)
		{
			System.out.println("hotkey file may be modified or corrupted; it has been reset to defaults");
			try
			{
				PrintWriter reset = new PrintWriter("hotkeys.txt");
				//default sequence of characters
				reset.println(" asdfwet");
				reset.flush();
				reset.close();
				
				hotkeyControls = " asdfwet".toCharArray();
			}
			catch (IOException exc)
			{
				
			}//end try catch
		}//end try catch
		
		//adjust difficulty
		COMP_TIME = (15 - difficulty) * 10;
		Piece.COOL_DOWN = (20 - difficulty) * 15;
		
		//set layout and add contents
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		
		//boardPnl is the chess board graphic
		Box b1 = Box.createHorizontalBox();
		boardPnl = new BoardPanel();
		
		//add listeners
		boardPnl.addMouseListener(this);
		boardPnl.addMouseMotionListener(this);
		this.addKeyListener(this);
		
		b1.add(boardPnl);
		
		//buttonPanel is the background panel for buttons
		JPanel buttonPanel = new JPanel();
		
		//quit button
		btnMenu = new JButton("Quit");
		btnMenu.setFocusable(false);
		btnMenu.setPreferredSize(new Dimension(150, 35));
		btnMenu.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//stop the game loop
				tm.stop();
				da.gameOver();
			}//end actionPerformed method
		});
		
		//pause button
		btnOptions = new JButton("pause/options");
		btnOptions.setFocusable(false);
		btnOptions.setPreferredSize(new Dimension(150, 35));
		final GameScreen this_ = this;
		btnOptions.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new OptionsWindow(this_);
				tm.stop();
			}//end actionPerformed method
		});
		buttonPanel.add(btnMenu);
		buttonPanel.add(btnOptions);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 0));
		
		this.add(b1);
		this.add(buttonPanel);
		
		//timer for animation
		al = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//clear the danger!
				AI.Move.clearDanger();
				
				//update all possible moves for players and pieces
				updateMoves();
				
				boardPnl.repaint();
				boardPnl.revalidate();
				//check for either player having won
				if (human.hasLost())
				{
					boardPnl.message = "computer won";
					tm.stop();
					//disable the options menu because otherwise the timer can be restarted through there
					btnOptions.setEnabled(false);
				}
				else if (comp.hasLost())
				{
					boardPnl.message = "player won";
					tm.stop();
					btnOptions.setEnabled(false);
				}//end if
				prevMove++;
				//System.out.println(e.getSource());
				if (prevMove >= COMP_TIME)
				{
					comp.makeMove();
					prevMove = 0;
				}//end if
			}//end actionPerformed method
		};
		//adds new timer
		tm = new Timer(10, al);
		tm.start();
		
		//Piece class handles the swap (changing colours of the pieces)
		if (swap)
		{
			Piece.swap = true;
		}
		else
		{
			Piece.swap = false;
		}//end if
		
		board = new Piece[BOARD_W][BOARD_H];
		//black pieces
		board[0][0] = 						new Piece(2, 0, 0,Piece.ROOK);
		board[BOARD_W - 1][0] = 			new Piece(2, BOARD_W - 1, 0, Piece.ROOK);
		board[1][0] = 						new Piece(2, 1, 0, Piece.KNIGHT);
		board[BOARD_W - 2][0] = 			new Piece(2, BOARD_W - 2, 0, Piece.KNIGHT);
		board[2][0] = 						new Piece(2, 2, 0,Piece.BISHOP);
		board[BOARD_W - 3][0] = 			new Piece(2, BOARD_W - 3, 0, Piece.BISHOP);
		board[3][0] = 						new Piece(2, 3, 0, Piece.QUEEN);
		board[4][0] = 						new Piece(2, 4, 0, Piece.KING);
		for(int x = 0; x < BOARD_W; x++){
			board[x][1] = 					new Piece(2, x, 1, Piece.PAWN);
		}//end for
		
		//white pieces
		board[0][BOARD_H - 1] = 			new Piece(1, 0, BOARD_H - 1, Piece.ROOK);
		board[BOARD_W - 1][BOARD_H - 1] = 	new Piece(1, BOARD_W - 1, BOARD_H - 1, Piece.ROOK);
		board[1][BOARD_H - 1] = 			new Piece(1, 1, BOARD_H - 1, Piece.KNIGHT);
		board[BOARD_W - 2][BOARD_H - 1] = 	new Piece(1, BOARD_W - 2, BOARD_H - 1, Piece.KNIGHT);
		board[2][BOARD_H - 1] = 			new Piece(1, 2, BOARD_H - 1, Piece.BISHOP);
		board[BOARD_W - 3][BOARD_H - 1] = 	new Piece(1, BOARD_W - 3, BOARD_H - 1, Piece.BISHOP);
		board[3][BOARD_H - 1] = 			new Piece(1, 3, BOARD_H - 1, Piece.QUEEN);
		board[4][BOARD_H - 1] = 			new Piece(1, 4, BOARD_H - 1, Piece.KING);
		for(int x = 0; x < BOARD_W; x++)
		{
			board[x][BOARD_H - 2] = 		new Piece(1, x, BOARD_H - 2, Piece.PAWN);
		}//end for
		
		//in case the "player starts as black" setting is enabled
		if (swap)
		{
			board[3][BOARD_H - 1] = 			new Piece(1, 3, BOARD_H - 1, Piece.KING);
			board[4][BOARD_H - 1] = 			new Piece(1, 4, BOARD_H - 1, Piece.QUEEN);
			board[3][0] = 						new Piece(2, 3, 0, Piece.KING);
			board[4][0] = 						new Piece(2, 4, 0, Piece.QUEEN);
		}//end if
		
		//initialize the human and AI players, and set the difficulty of the AI to the setting saved globally
		//note that the human has access to AI functionality too, for cheat mode    #cheat
		human = new AI(board, 1);
		comp = new AI(board, 2);
		
		//finish setting up the screen
		boardPnl.setFocusable(true);
		boardPnl.addKeyListener(this);
		boardPnl.requestFocusInWindow();
		
		this.pack();
		this.updateMoves();
	}//end constructor GameScreen (boolean, int)
	
	//#action
	//handle mouse actions
	public void mouseClicked(MouseEvent e)
	{
	}//end mouseClicked method

	public void mouseEntered(MouseEvent e)
	{
	}//end mouseEntered method

	public void mouseExited(MouseEvent e)
	{
	}//end mouseExited method

	public void mousePressed(MouseEvent e)
	{
	}//end mousePressed method

	/**
	 * Handles mouse released actions
	 * @param MouseEvent e - the event triggering the action
	 * @return void
	 * @author Tony Li
	 *    #Tony
	 */
	public void mouseReleased(MouseEvent e)
	{
		//if a piece is not selected, select one; otherwise, move it to the space
		if (human.selected == null)
		{
			human.select(mouseSqX, mouseSqY);
		}
		else
		{
			human.move(mouseSqX, mouseSqY);
			human.select(mouseSqX, mouseSqY);
		}//end if
	}//end mouseReleased method
	
	/**
	 * Handles mouse dragged actions
	 * @param MouseEvent e - the event triggering the action
	 * @return void
	 * @author Tony Li
	 *    #Tony
	 */
	public void mouseDragged(MouseEvent e)
	{
		//update mouse x and y
		this.mouseX = e.getX();
		this.mouseY = e.getY();
	}//end mouseDragged method

	/**
	 * Handles mouse moved actions
	 * @param MouseEvent e - the event triggering the action
	 * @return void
	 * @author Tony Li
	 *    #Tony
	 */
	public void mouseMoved(MouseEvent e)
	{
		//update mouse x and y
		this.mouseX = e.getX();
		this.mouseY = e.getY();
	}//end mouseMoved method
	
	
	//#action
	//keyboard events
	/**
	 * Handle hotkey actions
	 * @author Charles Lei
	*/
	public void keyTyped(KeyEvent ev)
	{
		//try to match the key character pressed
		char pressed = ev.getKeyChar();
		int takeAction = -1;
		for (int e = 0; e < 8; e++)
		{
			if (hotkeyControls[e] == pressed)
				takeAction = e;
		}//end for
		//determine which piece
		switch (takeAction)
		{
			case 0:
				//pause hotkey
				new OptionsWindow(this);
				tm.stop();
				break;
			case 1:
				//select next pawn
				human.nextPiece(Piece.PAWN);
				break;
			case 2:
				//select next bishop
				human.nextPiece(Piece.BISHOP);
				break;
			case 3:
				//select next knight
				human.nextPiece(Piece.KNIGHT);
				break;
			case 4:
				//select next rook
				human.nextPiece(Piece.ROOK);
				break;
			case 5:
				//select next queen
				human.nextPiece(Piece.QUEEN);
				break;
			case 6:
				//select next king
				human.nextPiece(Piece.KING);
				break;
			case 7:
				//take a piece
				human.takePiece();
				break;
			default:
				break;
		}//end switch
	}//end keyTyped method
	
	public void keyPressed(KeyEvent ev)
	{
	}//end keyPressed method
	
	public void keyReleased(KeyEvent ev)
	{	
	}//end keyReleased method
	
	/**
	 * Add detection to handle this JFrame
	 * @DetectAction d - the class to handle the JFrame
	 * @author Tony Li
	 * @return - void
	 *    #Tony
	 */
	public void addDetectAction(DetectAction d)
	{
		this.da = d;
		comp.setDifficulty(da.difficulty);
	}//end addDetectAction method
	
	/**
	 * updates all the moves (for all pieces)
	 * @author Charles Lei
	 *    #Charles
	 */
	public static void updateMoves()
	{
		human.updateMoves();
		comp.updateMoves();
	}//end static member updateMoves
	
	/**
	 * class where the actual board is drawn
	 * @author Tony Li, Charles Lei
	 */
	class BoardPanel extends JPanel
	{
		Color brown = new Color(139, 69, 19);
		//String for a message displayed
		String message = "";
		
		/**
		 * Default Constructor:
		 * @param none
		 * @return void
		 * @author Tony Li
		 *    #Tony
		 */
		public BoardPanel()
		{
			//make sure that there is enough space to draw the board
			this.setPreferredSize(new Dimension(SQUARE_SIZE * (BOARD_W + 2) + 2 * EDGE_SPACE, SQUARE_SIZE * BOARD_H + 2 * EDGE_SPACE));
		}//end default constructor ()
		
		/**
		 * @param Graphics g - the canvas to paint on
		 * @return void
		 * @author Tony Li
		 *    #Tony
		 */
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			
			//draws the side labels
			g.setFont(new Font("Trebuchet MS", Font.PLAIN, 16));
			for(int x = 0; x < BOARD_H; x++)
			{
				g.drawString(-x + BOARD_H + "", EDGE_SPACE / 2, (int) (SQUARE_SIZE * (x + 0.5)) + EDGE_SPACE);
			}//end for
			
			for(int x = 0; x < BOARD_W; x++)
			{
				g.drawString((char) (x + 'A') + "", (int) (SQUARE_SIZE * (x + 0.5)) + EDGE_SPACE, EDGE_SPACE / 2);
			}//end for
			
			//loop through every square on the board
			for(int x = 0; x < BOARD_W; x++)
			{
				for(int y = 0; y < BOARD_H; y++)
				{
					//alternates between brown and white squares
					if ((x + y) % 2 == 0)
					{
						g.setColor(Color.white);
					}
					else
					{
						g.setColor(brown);
					}//end if
					
					//draw the squares on the board
					g.fillRect(x * SQUARE_SIZE + EDGE_SPACE, y * SQUARE_SIZE + EDGE_SPACE, SQUARE_SIZE, SQUARE_SIZE);
				}//end for
			}//end for
			
			paintSelection(g);
			g.setColor(Color.red);
			//draws the pieces
			for(int x = 0; x < BOARD_W; x++)
			{
				for(int y = 0; y < BOARD_H; y++)
				{
					if (board[x][y] != null)
					{
						int plX = x * SQUARE_SIZE + EDGE_SPACE, plY = y * SQUARE_SIZE + EDGE_SPACE;
						//conditional drawing if animations are turned on (drawn part of the way to destination)
						if (!DetectAction.noAnimations && Piece.COOL_DOWN - board[x][y].coolDown < Piece.COOL_DOWN / 4)
						{
							double partway = 1 - (Piece.COOL_DOWN - board[x][y].coolDown) / (double) (Piece.COOL_DOWN / 4);
							plX -= (int)((x - board[x][y].lastPlace.x) * partway * SQUARE_SIZE);
							plY -= (int)((y - board[x][y].lastPlace.y) * partway * SQUARE_SIZE);
						}//end if
						
						//cooldown bar
						double ratio = (double)board[x][y].coolDown / Piece.COOL_DOWN;
						g.fillRect(plX, plY + (int)((1 - ratio) * SQUARE_SIZE), SQUARE_SIZE, SQUARE_SIZE - (int)((1 - ratio) * SQUARE_SIZE));
						
						//draws the piece
						g.drawImage(board[x][y].img, plX, plY, SQUARE_SIZE, SQUARE_SIZE, null);
					}//end if
				}//end for
			}//end for
			
			//draw captured pieces
			for (int e = 0; e < human.captured.size(); e++)
			{
				//if a king was captured, highlight it
				if (human.captured.get(e).pieceID == Piece.KING && !da.completeKillMode)
				{
					g.setColor(Color.orange);
					g.fillRect(
						EDGE_SPACE + (BOARD_W + 1) * SQUARE_SIZE,
						EDGE_SPACE - 12 + SQUARE_SIZE / 2 * e,
						SQUARE_SIZE,
						SQUARE_SIZE);
				}//end if
				g.drawImage(human.captured.get(e).img,
					EDGE_SPACE + (BOARD_W + 1) * SQUARE_SIZE,
					EDGE_SPACE - 12 + SQUARE_SIZE / 2 * e,
					SQUARE_SIZE,
					SQUARE_SIZE,
					null);
			}//end for
			
			for (int e = 0; e < comp.captured.size(); e++)
			{
				//same for AI
				if (comp.captured.get(e).pieceID == Piece.KING && !da.completeKillMode)
				{
					g.setColor(Color.orange);
					g.fillRect(
						EDGE_SPACE + BOARD_W * SQUARE_SIZE,
						EDGE_SPACE - 12 + SQUARE_SIZE / 2 * e,
						SQUARE_SIZE,
						SQUARE_SIZE
					);
				}//end if
				g.drawImage(comp.captured.get(e).img,
					EDGE_SPACE + BOARD_W * SQUARE_SIZE,
					EDGE_SPACE - 12 + SQUARE_SIZE / 2 * e,
					SQUARE_SIZE,
					SQUARE_SIZE,
					null);
			}//end for
			
			//draw the message string, in case someone won
			g.setFont(new Font("Trebuchet MS", Font.BOLD, 60));
			g.setColor(Color.red);
			g.drawString(message, 80, 525);
			
			//update mouse location
			//adds edge space and subtracts 1 to prevent bug where the mouse was outside but the green square was still drawn because it rounded negative decimals to 0
			mouseSqX = (mouseX + SQUARE_SIZE - EDGE_SPACE) / SQUARE_SIZE - 1;
			mouseSqY = (mouseY + SQUARE_SIZE - EDGE_SPACE) / SQUARE_SIZE - 1;
		}//end member paintComponent
		
		/**
		 * selected piece has its highlighting applied to board's visuals
		 * @author Charles Lei
		 *    #Charles
		 */
		public void paintSelection(Graphics grfx)
		{
			//highlight squares the selected piece can move to
			if (human.selected != null)
			{
				for (Coord e : human.selected.moves)
				{
					grfx.setColor(Color.yellow);
					grfx.fillRect(e.x * SQUARE_SIZE + EDGE_SPACE, e.y * SQUARE_SIZE + EDGE_SPACE, SQUARE_SIZE, SQUARE_SIZE);
					grfx.setColor(Color.black);
					grfx.drawRect(e.x * SQUARE_SIZE + EDGE_SPACE, e.y * SQUARE_SIZE + EDGE_SPACE, SQUARE_SIZE, SQUARE_SIZE);
				}//end for
			}//end if
			
			//draw a blue square to indicate the selected piece
			if (human.selected != null)
			{
				int xx = human.selected.xPos, yy = human.selected.yPos;
				grfx.setColor(Color.blue);
				grfx.fillRect(xx * SQUARE_SIZE + EDGE_SPACE, yy * SQUARE_SIZE + EDGE_SPACE, SQUARE_SIZE, SQUARE_SIZE);
			}//end if
			
			//draws green square around the selected square
			grfx.setColor(Color.green);
			//checks if the mouse is inside the board
			if (mouseSqX >= 0 && mouseSqX < BOARD_W && mouseSqY >= 0 && mouseSqY < BOARD_H)
			{
				grfx.drawRect(mouseSqX * SQUARE_SIZE + EDGE_SPACE, mouseSqY * SQUARE_SIZE + EDGE_SPACE, SQUARE_SIZE, SQUARE_SIZE);	
			}//end if
			
			//special AI assist: highlight recommended moves if the player is in cheat mode, according to the game's AI
			if (DetectAction.cheat)
			{
				if (human.getAllMoves().size() > 0)
				{
					//get the best move, if there is one
					AI.Move bestMove = human.getSortedMoves()[0];
					int x1 = bestMove.consider.xPos, y1 = bestMove.consider.yPos, x2 = bestMove.targetX, y2 = bestMove.targetY;
					
					//set the colour to a random pulsing shade of green, then paint the two squares of the recommended move
					grfx.setColor(
						new Color((int)(Math.random() * 50), (int)(Math.random() * 100) + 150, (int)(Math.random() * 50))
						);
					grfx.fillRect(x1 * SQUARE_SIZE + EDGE_SPACE, y1 * SQUARE_SIZE + EDGE_SPACE, SQUARE_SIZE, SQUARE_SIZE);
					grfx.fillRect(
						x2 * SQUARE_SIZE + EDGE_SPACE + SQUARE_SIZE / 8, 
						y2 * SQUARE_SIZE + EDGE_SPACE + SQUARE_SIZE / 8,
						SQUARE_SIZE * 3 / 4,
						SQUARE_SIZE * 3 / 4);
				}//end if
			}//end if
		}//end member paintSelection
	}//end BoardPanel
}//end GameScreen