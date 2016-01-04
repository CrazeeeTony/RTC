/**
 * Game where the actual game is played
 * @author Tony Li
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
	
	static Piece[][] board;
	
	//player instances for computer and human player(not used yet)
	Player comp;
	Player human;
	
	DetectAction da;
	
	Timer tm;
	ActionListener al;
	BoardPanel boardPnl;
	
	JButton btnMenu;
	JButton btnB;
	
	Piece selectedPiece;
	
	//variables to store the pixel and board coordinates of the mouse
	int mouseX = -9999;
	int mouseY = -9999;
	int mouseSqX;
	int mouseSqY;
	
	public GameScreen()
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
		btnMenu = new JButton("Return to Menu");
		btnMenu.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				da.returnToMenu();
			}
		});
		btnB = new JButton("B");
		b2.add(btnMenu);
		b2.add(btnB);
		
		this.add(b1);
		this.add(b2);
		
		//timer for animation
		al = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				boardPnl.repaint();
				boardPnl.revalidate();
			}
		};
		//adds new timer
		tm = new Timer(10, al);
		tm.start();
		board = new Piece[BOARD_W][BOARD_H];
		board[0][0] = new Piece(2, 0, 0,Piece.ROOK);
		board[BOARD_W - 1][0] = new Piece(2, BOARD_W - 1, 0, Piece.ROOK);
		board[1][0] = new Piece(2, 1, 0, Piece.KNIGHT);
		board[BOARD_W - 2][0] = new Piece(2, BOARD_W - 2, 0, Piece.KNIGHT);
		board[2][0] = new Piece(2, 2, 0,Piece.BISHOP);
		board[BOARD_W - 3][0] = new Piece(2, BOARD_W - 3, 0, Piece.BISHOP);
		board[3][0] = new Piece(2, 3, 0, Piece.QUEEN);
		board[4][0] = new Piece(2, 4, 0, Piece.KING);
		for(int x = 0; x < BOARD_W; x++){
			board[x][1] = new Piece(2, x, 1, Piece.PAWN);
		}
		
		board[0][BOARD_H - 1] = new Piece(1, 0, BOARD_H - 1, Piece.ROOK);
		board[BOARD_W - 1][BOARD_H - 1] = new Piece(1, BOARD_W - 1, BOARD_H - 1, Piece.ROOK);
		board[1][BOARD_H - 1] = new Piece(1, 1, BOARD_H - 1, Piece.KNIGHT);
		board[BOARD_W - 2][BOARD_H - 1] = new Piece(1, BOARD_W - 2, BOARD_H - 1, Piece.KNIGHT);
		board[2][BOARD_H - 1] = new Piece(1, 2, BOARD_H - 1, Piece.BISHOP);
		board[BOARD_W - 3][BOARD_H - 1] = new Piece(1, BOARD_W - 3, BOARD_H - 1, Piece.BISHOP);
		board[3][BOARD_H - 1] = new Piece(1, 3, BOARD_H - 1, Piece.KING);
		board[4][BOARD_H - 1] = new Piece(1, 4, BOARD_H - 1, Piece.QUEEN);
		for(int x = 0; x < BOARD_W; x++){
			board[x][BOARD_H - 2] = new Piece(1, x, BOARD_H - 2, Piece.PAWN);
		}
		
		/*for(int x = 0; x < BOARD_W; x++){
			for(int y = 0; y < BOARD_H; y++){
				if(board[x][y] != null && (board[x][y].xPos != x || board[x][y].xPos != x)){
					System.out.println(x + " " + y);
				}
			}
		}*/
		this.pack();
		this.updateMoves();
	}
	
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

	public void mouseReleased(MouseEvent e)
	{
		//if a piece is not selected, select that piece
		if(selectedPiece == null)
		{
			if(mouseSqX >= 0 && mouseSqX < BOARD_W && mouseSqY >= 0 && mouseSqY < BOARD_H)
			{
				selectedPiece = board[mouseSqX][mouseSqY];
			}
		}
		//else move piece
		else
		{
			if(mouseSqX >= 0 && mouseSqX < BOARD_W && mouseSqY >= 0 && mouseSqY < BOARD_H)
			{
				//request move here
				selectedPiece.moveTo(mouseSqX, mouseSqY);
				selectedPiece = null;
			}
		}
	}
	
	public void mouseDragged(MouseEvent e)
	{
		this.mouseX = e.getX();
		this.mouseY = e.getY();
	}

	public void mouseMoved(MouseEvent e)
	{
		this.mouseX = e.getX();
		this.mouseY = e.getY();
	}
	
	public void addDetectAction(DetectAction d)
	{
		this.da = d;
	}
	
	public static void updateMoves(){
		for(int x = 0; x < BOARD_W; x++){
			for(int y = 0; y < BOARD_H; y++){
				if(board[x][y] != null){
					board[x][y].updateMoves();
				}
			}
		}
	}
	
	//class where the actual board is drawn
	class BoardPanel extends JPanel{
		Color brown = new Color(139, 69, 19);
		public BoardPanel()
		{
			this.setPreferredSize(new Dimension(SQUARE_SIZE * BOARD_W + 2 * EDGE_SPACE, SQUARE_SIZE * BOARD_H + 2 * EDGE_SPACE));
		}
		
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			
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
			
			//highlights squares where you can move to
			if(selectedPiece!=null){
				for(int c = 0; c < selectedPiece.moves.size(); c++)
				{
					g.setColor(Color.yellow);
					g.fillRect(selectedPiece.moves.get(c).x * SQUARE_SIZE + EDGE_SPACE, selectedPiece.moves.get(c).y * SQUARE_SIZE + EDGE_SPACE, SQUARE_SIZE, SQUARE_SIZE);
					//draws black border
					g.setColor(Color.black);
					g.drawRect(selectedPiece.moves.get(c).x * SQUARE_SIZE + EDGE_SPACE, selectedPiece.moves.get(c).y * SQUARE_SIZE + EDGE_SPACE, SQUARE_SIZE, SQUARE_SIZE);
				}
			}
			
			//draws blue square under selected piece
			if(selectedPiece != null)
			{
				g.setColor(Color.blue);
				g.fillRect(selectedPiece.xPos * SQUARE_SIZE + EDGE_SPACE, selectedPiece.yPos * SQUARE_SIZE + EDGE_SPACE, SQUARE_SIZE, SQUARE_SIZE);
			}
			
			//draws the pieces
			for(int x = 0; x < BOARD_W; x++)
			{
				for(int y = 0; y < BOARD_H; y++)
				{
					if(board[x][y] != null)
					{
						//draws the piece
						g.drawImage(board[x][y].img, x * SQUARE_SIZE + EDGE_SPACE, y * SQUARE_SIZE + EDGE_SPACE, SQUARE_SIZE, SQUARE_SIZE, null);

					}
					g.setColor(Color.red);
					g.drawString("{" + x + ", " + y + "}", x * SQUARE_SIZE + EDGE_SPACE + 20, y * SQUARE_SIZE + EDGE_SPACE + 20);
				}
			}
			
			//update mouse location
			//adds edge space and subtracts 1 to prevent bug where the mouse was outside but the green square was still drawn because it rounded negative decimals to 0
			mouseSqX = (mouseX + SQUARE_SIZE - EDGE_SPACE) / SQUARE_SIZE - 1;
			mouseSqY = (mouseY + SQUARE_SIZE - EDGE_SPACE) / SQUARE_SIZE - 1;
			//System.out.println(mouseSqX + " " + mouseSqY);
			
			//draws green square around the selected square
			g.setColor(Color.green);
			//checks if the mouse is inside the board
			if(mouseSqX >= 0 && mouseSqX < BOARD_W && mouseSqY >= 0 && mouseSqY < BOARD_H)
			{
				g.drawRect(mouseSqX * SQUARE_SIZE + EDGE_SPACE, mouseSqY * SQUARE_SIZE + EDGE_SPACE, SQUARE_SIZE, SQUARE_SIZE);	
			}
		}
	}
}