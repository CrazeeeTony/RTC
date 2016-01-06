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
	
	//load images(imgs1 contains white pieces and imgs2 contains black pieces)
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
	long coolDown = 0;
	public BufferedImage img;
	boolean moved;
	
	/**
	* Main function
	* @param int player args - the number of the player who owns that piece
	* @param int xPos - the x position of the piece on the board
	* @param int yPos - the y position of the piece on the board
	* @param int yPos - a number representing what the piece is
	* @return void
	* */
	public Piece(int player,int xPos, int yPos, int pieceID)
	{
		this.player = player;
		this.xPos = xPos;
		this.yPos = yPos;
		this.pieceID = pieceID;
		moved = false;
		//set piece color according to player
		if(player == 1)
		{
			this.img = imgs1[pieceID];
		}
		else if(player == 2)
		{
			this.img = imgs2[pieceID];
		}
	}
	
	//move to a position on the board
	public void moveTo(int x, int y)
	{
		GameScreen.board[xPos][yPos] = null;
		this.xPos = x;
		this.yPos = y;
		GameScreen.board[x][y] = this;
		moved = true;
		if(pieceID == PAWN && yPos == 0)
		{
			this.pieceID = QUEEN;
			this.img = imgs1[QUEEN];
		}
		if(pieceID == PAWN && yPos == GameScreen.BOARD_H - 1)
		{
			this.pieceID = QUEEN;
			this.img = imgs2[QUEEN];
		}
		GameScreen.updateMoves();
	}
	
	//update the list of possible moves
	public void updateMoves(){
		moves.clear();
		switch (pieceID)
		{
			case KING:
				for(int i = -1; i <=1; i++)
				{
					for(int j = -1; j <= 1; j++)
					{
						if(i != 0 || j != 0)
						{
							int newX = xPos + i;
							int newY = yPos + j;
							if(Coord.inBoard(newX, newY) && (GameScreen.board[newX][newY] == null || GameScreen.board[newX][newY].player != this.player))
							{
								if(newX == 0 && newY == 1)
								{
									System.out.println(GameScreen.board[newX][newY]);
								}
								moves.add(new Coord(newX, newY));
							}
						}
					}
				}
				break;
			case QUEEN:
				pieceID = BISHOP;
				updateMoves();
				ArrayList<Coord> diagMoves = moves;
				pieceID = ROOK;
				updateMoves();
				for(int x = 0; x < diagMoves.size(); x++)
				{
					moves.add(diagMoves.get(x));
				}
				this.pieceID = QUEEN;
				break;
			case BISHOP:
				for(int x = 1; x < GameScreen.BOARD_W; x++)
				{
					int newX = xPos + x;
					int newY = yPos + x;
					if(Coord.inBoard(newX, newY))
					{
						if(GameScreen.board[newX][newY] != null && GameScreen.board[newX][newY].player == this.player)
						{
							break;
						}
						moves.add(new Coord(newX, newY));
						if(GameScreen.board[newX][newY] != null && GameScreen.board[newX][newY].player != this.player)
						{
							break;
						}
					}
					else
					{
						break;
					}
				}
				for(int x = 1; x < GameScreen.BOARD_W; x++)
				{
					int newX = xPos + x;
					int newY = yPos - x;
					if(Coord.inBoard(newX, newY))
					{
						if(GameScreen.board[newX][newY] != null && GameScreen.board[newX][newY].player == this.player)
						{
							break;
						}
						moves.add(new Coord(newX, newY));
						if(GameScreen.board[newX][newY] != null && GameScreen.board[newX][newY].player != this.player)
						{
							break;
						}
					}
					else
					{
						break;
					}
				}
				for(int x = 1; x < GameScreen.BOARD_W; x++)
				{
					int newX = xPos - x;
					int newY = yPos + x;
					if(Coord.inBoard(newX, newY))
					{
						if(GameScreen.board[newX][newY] != null && GameScreen.board[newX][newY].player == this.player)
						{
							break;
						}
						moves.add(new Coord(newX, newY));
						if(GameScreen.board[newX][newY] != null && GameScreen.board[newX][newY].player != this.player)
						{
							break;
						}
					}
					else
					{
						break;
					}
				}
				for(int x = 1; x < GameScreen.BOARD_W; x++)
				{
					int newX = xPos - x;
					int newY = yPos - x;
					if(Coord.inBoard(newX, newY))
					{
						if(GameScreen.board[newX][newY] != null && GameScreen.board[newX][newY].player == this.player)
						{
							break;
						}
						moves.add(new Coord(newX, newY));
						if(GameScreen.board[newX][newY] != null && GameScreen.board[newX][newY].player != this.player)
						{
							break;
						}
					}
					else
					{
						break;
					}
				}
				break;
			case KNIGHT:
				int [][] offset = {{-2, -1}, {-2, 1}, {2, -1}, {2, 1}, {-1, -2}, {-1, 2}, {1, -2}, {1, 2}};
				for(int x = 0; x < offset.length; x++)
				{
					int newX = xPos + offset[x][0];
					int newY = yPos + offset[x][1];
					System.out.println(newX + " "+ newY);
					if(Coord.inBoard(newX, newY) && (GameScreen.board[newX][newY] == null || GameScreen.board[newX][newY].player != this.player))
					{
						moves.add(new Coord(newX, newY));
					}
				}
				break;
			case ROOK:
				for(int x = 1; x < GameScreen.BOARD_W; x++)
				{
					int newX = xPos + x;
					if(Coord.inBoard(newX, yPos))
					{
						if(GameScreen.board[newX][yPos] != null && GameScreen.board[newX][yPos].player == this.player)
						{
							break;
						}
						moves.add(new Coord(newX, yPos));
						if(GameScreen.board[newX][yPos] != null && GameScreen.board[newX][yPos].player != this.player)
						{
							break;
						}
					}
					else
					{
						break;
					}
				}
				for(int x = 1; x < GameScreen.BOARD_W; x++)
				{
					int newX = xPos - x;
					if(Coord.inBoard(newX, yPos))
					{
						if(GameScreen.board[newX][yPos] != null && GameScreen.board[newX][yPos].player == this.player)
						{
							break;
						}
						moves.add(new Coord(newX, yPos));
						if(GameScreen.board[newX][yPos] != null && GameScreen.board[newX][yPos].player != this.player)
						{
							break;
						}
					}
					else
					{
						break;
					}
				}
				for(int x = 1; x < GameScreen.BOARD_H; x++)
				{
					int newY = yPos + x;
					if(Coord.inBoard(xPos, newY))
					{
						if(GameScreen.board[xPos][newY] != null && GameScreen.board[xPos][newY].player == this.player)
						{
							break;
						}
						moves.add(new Coord(xPos, newY));
						if(GameScreen.board[xPos][newY] != null && GameScreen.board[xPos][newY].player != this.player)
						{
							break;
						}
					}
					else
					{
						break;
					}
				}
				for(int x = 1; x < GameScreen.BOARD_H; x++)
				{
					int newY = yPos - x;
					if(Coord.inBoard(xPos, newY)){
						if(GameScreen.board[xPos][newY] != null && GameScreen.board[xPos][newY].player == this.player)
						{
							break;
						}
						moves.add(new Coord(xPos, newY));
						if(GameScreen.board[xPos][newY] != null && GameScreen.board[xPos][newY].player != this.player)
						{
							break;
						}
					}
					else
					{
						break;
					}
				}
				break;
			case PAWN:
				if(player == 1 && Coord.inBoard(xPos, yPos - 1) && GameScreen.board[xPos][yPos - 1] == null)
				{
					moves.add(new Coord(xPos, yPos - 1));
				}
				if(player == 1 && Coord.inBoard(xPos + 1, yPos - 1) && GameScreen.board[xPos + 1][yPos - 1] != null && GameScreen.board[xPos + 1][yPos - 1].player != this.player)
				{
					moves.add(new Coord(xPos + 1, yPos - 1));
				}
				if(player == 1 && Coord.inBoard(xPos - 1, yPos - 1) && GameScreen.board[xPos - 1][yPos - 1] != null && GameScreen.board[xPos - 1][yPos - 1].player != this.player)
				{
					moves.add(new Coord(xPos - 1, yPos - 1));
				}
				if(player == 1 && !moved && Coord.inBoard(xPos, yPos - 2) && GameScreen.board[xPos][yPos - 2] == null && GameScreen.board[xPos][yPos - 1] == null)
				{
					moves.add(new Coord(xPos, yPos - 2));
				}
				if(player == 2 && Coord.inBoard(xPos, yPos + 1) && GameScreen.board[xPos][yPos + 1] == null)
				{
					moves.add(new Coord(xPos, yPos + 1));
				}
				if(player == 2 && Coord.inBoard(xPos + 1, yPos + 1) && GameScreen.board[xPos + 1][yPos + 1] != null && GameScreen.board[xPos + 1][yPos + 1].player != this.player)
				{
					moves.add(new Coord(xPos + 1, yPos + 1));
				}
				if(player == 2 && Coord.inBoard(xPos - 1, yPos + 1) && GameScreen.board[xPos - 1][yPos + 1] != null && GameScreen.board[xPos - 1][yPos + 1].player != this.player)
				{
					moves.add(new Coord(xPos - 1, yPos + 1));
				}
				if(player == 2 && !moved && Coord.inBoard(xPos, yPos + 2) && GameScreen.board[xPos][yPos + 2] == null && GameScreen.board[xPos][yPos + 1] == null)
				{
					moves.add(new Coord(xPos, yPos + 2));
				}
				break;
		}
	}
	
	public int getValue(){
		switch (pieceID)
		{
		case KING:
			return 999;
		case QUEEN:
			return 9;
		case ROOK:
			return 5;
		case BISHOP:
			return 3;
		case KNIGHT:
			return 3;
		case PAWN:
			return 1;
		}
		return 0;
	}
}