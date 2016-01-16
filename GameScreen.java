/**
 * Game where the actual game is played
 * @author Tony Li, Charles Lei
 */
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.*;
public class GameScreen extends JFrame implements MouseListener, MouseMotionListener
{
	//variables so that we can easily change board size and dimensions
	public static final int BOARD_W = 8;
	public static final int BOARD_H = 8;
	public static final int SQUARE_SIZE = 50;
	public static final int EDGE_SPACE = 75;
	
	int prevMove = 0;
	public static final int COMP_TIME = 100;
	
	static Piece[][] board;
	
	//player instances for computer and human player
	static AI comp;
	static Player human;
	
	DetectAction da;
	
	Timer tm;
	ActionListener al;
	BoardPanel boardPnl;
	
	JButton btnMenu;
	JButton btnOptions;
	JPanel everything;
	JLabel message;
	
	//variables to store the pixel and board coordinates of the mouse
	int mouseX = -9999;
	int mouseY = -9999;
	int mouseSqX;
	int mouseSqY;
	
	/**
	 *
	 * @author Tony Li
	 */
	public GameScreen(int windowX, int windowY)
	{
		super("RTC");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try
		{
			this.setIconImage(ImageIO.read(new File("Piece Images/1Knight.png")));
		}
		catch(Exception e)
		{
		}
		
		//set layout and add contents
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		
		Box b1 = Box.createHorizontalBox();
		boardPnl = new BoardPanel();
		boardPnl.addMouseListener(this);
		boardPnl.addMouseMotionListener(this);
		b1.add(boardPnl);
		
		Box b2 = Box.createHorizontalBox();
		btnMenu = new JButton("Quit");
		btnMenu.setPreferredSize(new Dimension(150, 50));
		btnMenu.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				da.gameOver();
			}
		});
		btnOptions = new JButton("pause/options");
		btnOptions.setPreferredSize(new Dimension(150, 50));
		final GameScreen this_ = this;
		btnOptions.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new OptionsWindow(this_);
				tm.stop();
			}
		});
		b2.add(btnMenu);
		b2.add(btnOptions);
		
		this.add(b1);
		this.add(b2);
		
		//timer for animation
		al = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				AI.Move.clearDanger();
				//update all possible moves for players and pieces
				updateMoves();
				//clear the danger!
				
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
				}
				prevMove++;
				if(prevMove >= COMP_TIME)
				{
					comp.makeMove(human);
					prevMove = 0;
				}
			}
		};
		//adds new timer
		tm = new Timer(10, al);
		tm.start();
		
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
		}
		
		//white pieces
		board[0][BOARD_H - 1] = 			new Piece(1, 0, BOARD_H - 1, Piece.ROOK);
		board[BOARD_W - 1][BOARD_H - 1] = 	new Piece(1, BOARD_W - 1, BOARD_H - 1, Piece.ROOK);
		board[1][BOARD_H - 1] = 			new Piece(1, 1, BOARD_H - 1, Piece.KNIGHT);
		board[BOARD_W - 2][BOARD_H - 1] = 	new Piece(1, BOARD_W - 2, BOARD_H - 1, Piece.KNIGHT);
		board[2][BOARD_H - 1] = 			new Piece(1, 2, BOARD_H - 1, Piece.BISHOP);
		board[BOARD_W - 3][BOARD_H - 1] = 	new Piece(1, BOARD_W - 3, BOARD_H - 1, Piece.BISHOP);
		board[3][BOARD_H - 1] = 			new Piece(1, 3, BOARD_H - 1, Piece.KING);
		board[4][BOARD_H - 1] = 			new Piece(1, 4, BOARD_H - 1, Piece.QUEEN);
		for(int x = 0; x < BOARD_W; x++){
			board[x][BOARD_H - 2] = 		new Piece(1, x, BOARD_H - 2, Piece.PAWN);
		}
		
		human = new Player(board, 1);
		comp = new AI(board, 2, 1);
		
		changeSize(windowX, windowY);
		
		this.pack();
		this.updateMoves();
	}//end default constructor GameScreen()
	
	//handle mouse actions
	public void mouseClicked(MouseEvent e)
	{
	}

	public void mouseEntered(MouseEvent e)
	{
	}

	public void mouseExited(MouseEvent e)
	{
	}

	public void mousePressed(MouseEvent e)
	{
	}

	/**
	 *
	 * @author Tony Li
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
		}
	}
	
	/**
	 *
	 * @author Tony Li
	 */
	public void mouseDragged(MouseEvent e)
	{
		this.mouseX = e.getX();
		this.mouseY = e.getY();
	}

	/**
	 *
	 * @author Tony Li
	 */
	public void mouseMoved(MouseEvent e)
	{
		this.mouseX = e.getX();
		this.mouseY = e.getY();
	}
	
	/**
	 *
	 * @author Tony Li
	 */
	public void addDetectAction(DetectAction d)
	{
		this.da = d;
	}
	
	/**
	 * updates all the moves (for all pieces)
	 * @author Charles Lei
	 */
	public static void updateMoves()
	{
		human.updateMoves();
		comp.updateMoves();
	}//end static member updateMoves
	
	public void changeSize(int newX, int newY)    //////////////////////////////////////
	{
		
	}
	
	/**
	 * class where the actual board is drawn
	 * @author Tony Li
	 */
	class BoardPanel extends JPanel{
		Color brown = new Color(139, 69, 19);
		//store the graphics object so we can use it in other methods
		Graphics grfx;
		//String for a message displayed
		String message = "";
		
		/**
		 *
		 */
		public BoardPanel()
		{
			this.setPreferredSize(new Dimension(SQUARE_SIZE * (BOARD_W + 2) + 2 * EDGE_SPACE, SQUARE_SIZE * BOARD_H + 2 * EDGE_SPACE));
		}//end default constructor ()
		
		/**
		 *
		 * @author Tony Li
		 */
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			//set the stored graphics variable
			grfx = g;
			
			//draw the message string
			grfx.drawString(message, 50, 10);
			
			//draws the side labels
			for(int x = 0; x < BOARD_H; x++)
			{
				g.drawString(-x + BOARD_H + "", EDGE_SPACE / 2, (int) (SQUARE_SIZE * (x + 0.5)) + EDGE_SPACE);
			}
			for(int x = 0; x < BOARD_W; x++)
			{
				g.drawString((char) (x + 'A') + "", (int) (SQUARE_SIZE * (x + 0.5)) + EDGE_SPACE, EDGE_SPACE / 2);
			}
			
			//loop through every square on the board
			for(int x = 0; x < BOARD_W; x++)
			{
				for(int y = 0; y < BOARD_H; y++)
				{
					//alternates between brown and white squares
					if((x + y) % 2 == 0)
					{
						g.setColor(Color.white);
					}
					else
					{
						g.setColor(brown);
					}
					
					//draw the squares on the board
					g.fillRect(x * SQUARE_SIZE + EDGE_SPACE, y * SQUARE_SIZE + EDGE_SPACE, SQUARE_SIZE, SQUARE_SIZE);
				}
			}
			//draw blue square, highlighting
			paintSelection(g);
			g.setColor(Color.red);
			//draws the pieces
			for(int x = 0; x < BOARD_W; x++)
			{
				for(int y = 0; y < BOARD_H; y++)
				{
					if(board[x][y] != null)
					{
						double ratio = (double)board[x][y].coolDown / Piece.COOL_DOWN;
						g.fillRect(x * SQUARE_SIZE + EDGE_SPACE, (int)((double)(y + 1 - ratio) * SQUARE_SIZE + EDGE_SPACE), SQUARE_SIZE, (int)(SQUARE_SIZE * ratio + 0.5));
						//draws the piece
						g.drawImage(board[x][y].img, x * SQUARE_SIZE + EDGE_SPACE, y * SQUARE_SIZE + EDGE_SPACE, SQUARE_SIZE, SQUARE_SIZE, null);
					}
					//g.setColor(Color.red);
					//g.drawString("{" + x + ", " + y + "}", x * SQUARE_SIZE + EDGE_SPACE + 20, y * SQUARE_SIZE + EDGE_SPACE + 20);
				}
			}
			for (int e = 0; e < human.captured.size(); e++)
			{
				grfx.drawImage(human.captured.get(e).img, EDGE_SPACE + (BOARD_W + 1) * SQUARE_SIZE, EDGE_SPACE -12 + SQUARE_SIZE / 2 * e, SQUARE_SIZE, SQUARE_SIZE, null);
			}
			for (int e = 0; e < comp.captured.size(); e++)
			{
				grfx.drawImage(comp.captured.get(e).img, EDGE_SPACE + BOARD_W * SQUARE_SIZE, EDGE_SPACE -12 + SQUARE_SIZE / 2 * e, SQUARE_SIZE, SQUARE_SIZE, null);
			}
			
			//update mouse location
			//adds edge space and subtracts 1 to prevent bug where the mouse was outside but the green square was still drawn because it rounded negative decimals to 0
			mouseSqX = (mouseX + SQUARE_SIZE - EDGE_SPACE) / SQUARE_SIZE - 1;
			mouseSqY = (mouseY + SQUARE_SIZE - EDGE_SPACE) / SQUARE_SIZE - 1;
		}//end member paintComponent
		
		/**
		 * selected piece has its highlighting applied to board's visuals
		 * @author Charles Lei
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
				}
			}
			
			//draw a blue square to indicate the selected piece
			if (human.selected != null)
			{
				grfx.setColor(Color.blue);
				grfx.fillRect(human.selected.xPos * SQUARE_SIZE + EDGE_SPACE, human.selected.yPos * SQUARE_SIZE + EDGE_SPACE, SQUARE_SIZE, SQUARE_SIZE);
			}
			
			//draws green square around the selected square
			grfx.setColor(Color.green);
			//checks if the mouse is inside the board
			if(mouseSqX >= 0 && mouseSqX < BOARD_W && mouseSqY >= 0 && mouseSqY < BOARD_H)
			{
				grfx.drawRect(mouseSqX * SQUARE_SIZE + EDGE_SPACE, mouseSqY * SQUARE_SIZE + EDGE_SPACE, SQUARE_SIZE, SQUARE_SIZE);	
			}
			/*
			for(int x = 0; x < 8; x++)
							for(int y = 0; y < 8; y++)
								if(AI.danger[x][y])
					{
						grfx.setColor(Color.orange);
						grfx.fillRect(x * SQUARE_SIZE + EDGE_SPACE, y * SQUARE_SIZE + EDGE_SPACE, SQUARE_SIZE, SQUARE_SIZE);
					}
					*/
		}//end member paintSelection
	}//end BoardPanel
}//end GameScreen