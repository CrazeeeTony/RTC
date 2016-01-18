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
	public static int COOL_DOWN = 100;
	public static boolean swap = false;
	private static BufferedImage[] imgs1;
	private static BufferedImage[] imgs2;
	
	//stores possible moves(not done)
	public ArrayList <Coord> moves = new ArrayList<>();
	
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
	public BufferedImage img;
	boolean moved;
	int coolDown = 0;
	Coord lastPlace;
	
	/**
	* Main function
	* @param int player args - the number of the player who owns that piece
	* @param int xPos - the x position of the piece on the board
	* @param int yPos - the y position of the piece on the board
	* @param int yPos - a number representing what the piece is
	* @return void
	* */
	public Piece(int player, int xPos, int yPos, int pieceID)
	{
		this.player = player;
		this.xPos = xPos;
		this.yPos = yPos;
		this.pieceID = pieceID;
		moved = false;
		lastPlace = new Coord(xPos, yPos);
		//set piece color according to player
		if(player == 1)
		{
			if(swap)
			{
				this.img = imgs2[pieceID];
			}
			else
			{
				this.img = imgs1[pieceID];				
			}
		}
		else if(player == 2)
		{
			if(swap)
			{
				this.img = imgs1[pieceID];
			}
			else
			{
				this.img = imgs2[pieceID];				
			}
		}
	}
	
	/**
	 * move piece to a position on the board
	 * @param int x - the x coordinate to move to
	 * @param int y - the y coordinate to move to
	 */
	public void moveTo(int x, int y)
	{
		//set last move to current x and y
		lastPlace = new Coord(xPos, yPos);
		
		GameScreen.board[xPos][yPos] = null;
		this.xPos = x;
		this.yPos = y;
		//kill the current piece there if it exists
		if (player == 2)
		{
			GameScreen.human.kill(GameScreen.board[x][y]);
		}
		else if (player == 1)
		{
			GameScreen.comp.kill(GameScreen.board[x][y]);
		}
		
		GameScreen.board[x][y] = this;
		moved = true;
		//converts pawn to queen when the end of the board is reached
		if(pieceID == PAWN && yPos == 0)
		{
			System.out.println(swap);
			this.pieceID = QUEEN;
			if(swap)
			{
				this.img = imgs2[QUEEN];
			}
			else
			{
				this.img = imgs1[QUEEN];
			}
		}
		if(pieceID == PAWN && yPos == GameScreen.BOARD_H - 1)
		{
			if(swap)
			{
				this.img = imgs1[QUEEN];
			}
			else
			{
				this.img = imgs2[QUEEN];
			}
		}
		this.coolDown = COOL_DOWN;
		GameScreen.updateMoves();
	}
	
	/**
	 * updates list of possible moves
	 * also updates the list of dangerous squares
	 */
	public void updateMoves()
	{
		//update the cooldown first
		if (coolDown > 0)
			coolDown--;
		
		moves = new ArrayList<Coord>();
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
							if(Coord.inBoard(newX, newY))
							{
								if(this.player == 1)
								{
									AI.danger[newX][newY] = true;
								}
								//check if the square is empty and valid and the piece has cooled down
								if((GameScreen.board[newX][newY] == null || GameScreen.board[newX][newY].player != this.player) && this.coolDown == 0)
								{
									moves.add(new Coord(newX, newY));
								}
							}
						}
					}
				}
				break;
			case QUEEN:
				//generate queen moves by combining bishop and rook moves
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
				//queen is cooled down twice when it turns into a rook
				coolDown += 2;
				if(coolDown <= 2)
				{
					coolDown = 0;
				}
				break;
			case BISHOP:
				//checks in four directions until it reaches the edge of the board or a piece
				for(int x = 1; x < GameScreen.BOARD_W; x++)
				{
					int newX = xPos + x;
					int newY = yPos + x;
					if(Coord.inBoard(newX, newY))
					{
						if(this.player == 1)
						{
							AI.danger[newX][newY] = true;
						}
						if(GameScreen.board[newX][newY] != null && GameScreen.board[newX][newY].player == this.player)
						{
							break;
						}
						if(this.coolDown == 0)
						{
							moves.add(new Coord(newX, newY));
						}
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
						if(this.player == 1)
						{
							AI.danger[newX][newY] = true;
						}
						if(GameScreen.board[newX][newY] != null && GameScreen.board[newX][newY].player == this.player)
						{
							break;
						}
						if(this.coolDown == 0)
						{
							moves.add(new Coord(newX, newY));
						}
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
						if(this.player == 1)
						{
							AI.danger[newX][newY] = true;
						}
						if(GameScreen.board[newX][newY] != null && GameScreen.board[newX][newY].player == this.player)
						{
							break;
						}
						if(this.coolDown == 0)
						{
							moves.add(new Coord(newX, newY));
						}
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
						if(this.player == 1)
						{
							AI.danger[newX][newY] = true;
						}
						if(GameScreen.board[newX][newY] != null && GameScreen.board[newX][newY].player == this.player)
						{

							break;
						}
						if(this.coolDown == 0)
						{
							moves.add(new Coord(newX, newY));
						}
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
				//array representing the x and y movements a knight can make
				int [][] offset = {{-2, -1}, {-2, 1}, {2, -1}, {2, 1}, {-1, -2}, {-1, 2}, {1, -2}, {1, 2}};
				for(int x = 0; x < offset.length; x++)
				{
					int newX = xPos + offset[x][0];
					int newY = yPos + offset[x][1];
					if(Coord.inBoard(newX, newY))
					{
						if(this.player == 1)
						{
							AI.danger[newX][newY] = true;
						}
						if((GameScreen.board[newX][newY] == null || GameScreen.board[newX][newY].player != this.player) && this.coolDown == 0)
						{
							moves.add(new Coord(newX, newY));
						}
					}
				}
				break;
			case ROOK:
				//checks in four directions until it reaches the edge of the board or a piece
				for(int x = 1; x < GameScreen.BOARD_W; x++)
				{
					int newX = xPos + x;
					if(Coord.inBoard(newX, yPos))
					{
						if(this.player == 1)
						{
							AI.danger[newX][yPos] = true;
						}
						if(GameScreen.board[newX][yPos] != null && GameScreen.board[newX][yPos].player == this.player)
						{
							break;
						}
						if(this.coolDown == 0)
						{
							moves.add(new Coord(newX, yPos));
						}
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
						if(this.player == 1)
						{
							AI.danger[newX][yPos] = true;
						}
						if(GameScreen.board[newX][yPos] != null && GameScreen.board[newX][yPos].player == this.player)
						{
							break;
						}
						if(this.coolDown == 0)
						{
							moves.add(new Coord(newX, yPos));
						}
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
						if(this.player == 1)
						{
							AI.danger[xPos][newY] = true;
						}

						if(GameScreen.board[xPos][newY] != null && GameScreen.board[xPos][newY].player == this.player)
						{
							break;
						}
						if(this.coolDown == 0)
						{
							moves.add(new Coord(xPos, newY));
						}
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
					if(Coord.inBoard(xPos, newY))
					{
						if(this.player == 1)
						{
							AI.danger[xPos][newY] = true;
						}
						if(GameScreen.board[xPos][newY] != null && GameScreen.board[xPos][newY].player == this.player)
						{
							break;
						}
						if(this.coolDown == 0)
						{
							moves.add(new Coord(xPos, newY));
						}
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
				//human
				//move forward one square
				if(player == 1 && Coord.inBoard(xPos, yPos - 1) && GameScreen.board[xPos][yPos - 1] == null && this.coolDown == 0)
				{
					moves.add(new Coord(xPos, yPos - 1));
				}
				//
				if(player == 1 && Coord.inBoard(xPos + 1, yPos - 1))
				{
					AI.danger[xPos + 1][yPos - 1] = true;
					if(GameScreen.board[xPos + 1][yPos - 1] != null && GameScreen.board[xPos + 1][yPos - 1].player != this.player && this.coolDown == 0)
					{
						moves.add(new Coord(xPos + 1, yPos - 1));
					}
				}
				if(player == 1 && Coord.inBoard(xPos - 1, yPos - 1))
				{
					AI.danger[xPos - 1][yPos - 1] = true;
					if(GameScreen.board[xPos - 1][yPos - 1] != null && GameScreen.board[xPos - 1][yPos - 1].player != this.player && this.coolDown == 0)
					{
						moves.add(new Coord(xPos - 1, yPos - 1));
					}
				}
				//move an extra square the first move
				if(player == 1 && !moved && Coord.inBoard(xPos, yPos - 2) && GameScreen.board[xPos][yPos - 2] == null && GameScreen.board[xPos][yPos - 1] == null)
				{
					moves.add(new Coord(xPos, yPos - 2));
				}
				if(player == 2 && coolDown == 0){
					if(Coord.inBoard(xPos, yPos + 1) && GameScreen.board[xPos][yPos + 1] == null)
					{
						moves.add(new Coord(xPos, yPos + 1));
					}
					//move forward one square
					if(Coord.inBoard(xPos + 1, yPos + 1) && GameScreen.board[xPos + 1][yPos + 1] != null && GameScreen.board[xPos + 1][yPos + 1].player != this.player)
					{
						moves.add(new Coord(xPos + 1, yPos + 1));
					}
					if(Coord.inBoard(xPos - 1, yPos + 1) && GameScreen.board[xPos - 1][yPos + 1] != null && GameScreen.board[xPos - 1][yPos + 1].player != this.player)
					{
						moves.add(new Coord(xPos - 1, yPos + 1));
					}
					//move an extra square the first move
					if(!moved && Coord.inBoard(xPos, yPos + 2) && GameScreen.board[xPos][yPos + 2] == null && GameScreen.board[xPos][yPos + 1] == null)
					{
						moves.add(new Coord(xPos, yPos + 2));
					}
					break;
				}
				else
				{
					break;
				}
		}
	}//end updateMoves
	
	/**
	 * a method to return an estimated value of the piece
	 */
	public int getValue(){
		switch (pieceID)
		{
			case KING:
				return 999;
			case QUEEN:
				return 18;
			case ROOK:
				return 10;
			case BISHOP:
				return 6;
			case KNIGHT:
				return 6;
			case PAWN:
				return 2;
		}
		return 0;
	}
}