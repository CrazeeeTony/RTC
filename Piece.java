/**
 * Class to store piece information
 * @author Tony Li
 */
import java.awt.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;
import java.util.ArrayList;
public class Piece
{
	//variables to identify pieces
	public static final int KING = 0;
	public static final int QUEEN = 1;
	public static final int BISHOP = 2;
	public static final int KNIGHT = 3;
	public static final int ROOK = 4;
	public static final int PAWN = 5;
	private static BufferedImage[] imgs1;
	private static BufferedImage[] imgs2;
	
	//stores possible moves(not done)
	public ArrayList <Coord> moves;
	
	//load images(img1 contains white pieces and imgs2 contains black pieces)
	static
	{
		try
		{
			imgs1 = new BufferedImage[] {ImageIO.read(new File("Piece Images/1King.png")), ImageIO.read(new File("Piece Images/1Queen.png")), ImageIO.read(new File("Piece Images/1Bishop.png")), ImageIO.read(new File("Piece Images/1Knight.png")), ImageIO.read(new File("Piece Images/1Rook.png")), ImageIO.read(new File("Piece Images/1Pawn.png"))};
			imgs2 = new BufferedImage[] {ImageIO.read(new File("Piece Images/2King.png")), ImageIO.read(new File("Piece Images/2Queen.png")), ImageIO.read(new File("Piece Images/2Bishop.png")), ImageIO.read(new File("Piece Images/2Knight.png")), ImageIO.read(new File("Piece Images/2Rook.png")), ImageIO.read(new File("Piece Images/2Pawn.png"))};
		}
		catch(Exception e)
		{
			System.out.println("Error Loading Images");
			e.printStackTrace();
		}
	}
	
	//1 = player(white and on bottom), 2 = computer(black and on top)
	int player;
	int xPos;
	int yPos;
	int pieceID;
	long coolDown;
	public BufferedImage img;
	
	//constructor
	public Piece(int player,int xPos, int yPos, int pieceID)
	{
		this.player = player;
		this.xPos = xPos;
		this.yPos = yPos;
		this.pieceID = pieceID;
		//set piece color according to player
		if(player == 1)
		{
			this.img = imgs1[pieceID];
		}
		else if(player == 2)
		{
			this.img = imgs2[pieceID];
		}
		updateMoves();
	}
	
	//move to a position on the board
	public void moveTo(int x, int y)
	{
		GameScreen.board[xPos][yPos] = null;
		this.xPos = x;
		this.yPos = y;
		GameScreen.board[x][y] = this;
		updateMoves();
	}
	
	//update the list of possible moves(not done)
	public void updateMoves(){
		moves = new ArrayList<Coord>();
		switch (pieceID)
		{
			case KING:
				for(int i = -1; i <=1; i++){
					for(int j = -1; j <= 1; j++){
						if(i != 0 || j != 0){
							try{
								moves.add(new Coord(xPos + i, yPos + j));
							}
							catch(Exception e){}
						}
					}
				}
				break;
			case QUEEN:
				break;
			case BISHOP:
				break;
			case KNIGHT:
				break;
			case ROOK:
				break;
			case PAWN:
				break;
		}
	}
}