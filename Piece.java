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
	
	//stores whether to swap piece colours
	public static boolean swap = false;
	
	//variables to hold images of pieces
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
		}//end try catch
	}//end static block
	
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
		if (player == 1)
		{
			if (swap)
			{
				this.img = imgs2[pieceID];
			}
			else
			{
				this.img = imgs1[pieceID];				
			}//end if
		}
		else if (player == 2)
		{
			if (swap)
			{
				this.img = imgs1[pieceID];
			}
			else
			{
				this.img = imgs2[pieceID];				
			}//end if
		}//end if
	}//end (int, int, int, int) constructor
	
	/**
	 * move piece to a position on the board
	 * @param int x - the x coordinate to move to
	 * @param int y - the y coordinate to move to
	 * @return void
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
		}//end if
		
		GameScreen.board[x][y] = this;
		moved = true;
		
		//converts pawn to queen when the end of the board is reached
		if (pieceID == PAWN && yPos == 0)
		{
			System.out.println(swap);
			this.pieceID = QUEEN;
			//if colours are swapped use other queen image
			if (swap)
			{
				this.img = imgs2[QUEEN];
			}
			else
			{
				this.img = imgs1[QUEEN];
			}//end if
		}//end if
		if (pieceID == PAWN && yPos == GameScreen.BOARD_H - 1)
		{
			//if colours are swapped use other queen image
			if (swap)
			{
				this.img = imgs1[QUEEN];
			}
			else
			{
				this.img = imgs2[QUEEN];
			}//end if
		}//end if
		this.coolDown = COOL_DOWN;
		GameScreen.updateMoves();
	}//end moveTo method
	
	/**
	 * updates list of possible moves
	 * also updates the list of dangerous squares
	 * @param - none
	 * @return void
	 */
	public void updateMoves()
	{
		//update the cooldown first
		if (coolDown > 0)
		{
			coolDown--;
		}//end if
		
		//dispose of old moves and create new arraylist
		moves = new ArrayList<Coord>();
		switch (pieceID)
		{
			case KING:
				//check all adjacent squares
				for(int i = -1; i <=1; i++)
				{
					for(int j = -1; j <= 1; j++)
					{
						//if it's not the same square that the piece is on now
						if (i != 0 || j != 0)
						{
							int newX = xPos + i;
							int newY = yPos + j;
							if (Coord.inBoard(newX, newY))
							{
								//mark the square as dangerous for AI if friendly piece
								if (this.player == 1)
								{
									AI.danger[newX][newY] = true;
								}//end if
								//check if the square is empty and valid and the piece has cooled down
								if ((GameScreen.board[newX][newY] == null || GameScreen.board[newX][newY].player != this.player) && this.coolDown == 0)
								{
									moves.add(new Coord(newX, newY));
								}//end if
							}//end if
						}//end if
					}//end for
				}//end for
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
				}//end for
				this.pieceID = QUEEN;
				//queen is cooled down twice when it turns into a rook
				coolDown += 2;
				if (coolDown <= 2)
				{
					coolDown = 0;
				}//end if
				break;
			case BISHOP:
				//checks in four directions until it reaches the edge of the board or a piece
				for(int x = 1; x < GameScreen.BOARD_W; x++)
				{
					int newX = xPos + x;
					int newY = yPos + x;
					if (Coord.inBoard(newX, newY))
					{
						if (this.player == 1)
						{
							AI.danger[newX][newY] = true;
						}//end if
						if (GameScreen.board[newX][newY] != null && GameScreen.board[newX][newY].player == this.player)
						{
							break;
						}//end if
						if (this.coolDown == 0)
						{
							moves.add(new Coord(newX, newY));
						}//end if
						if (GameScreen.board[newX][newY] != null && GameScreen.board[newX][newY].player != this.player)
						{
							break;
						}//end if
					}
					else
					{
						break;
					}//end if
				}
				for(int x = 1; x < GameScreen.BOARD_W; x++)
				{
					int newX = xPos + x;
					int newY = yPos - x;
					//if new coordinate in square
					if (Coord.inBoard(newX, newY))
					{
						//mark square as dangerous for AI if friendly piece
						if (this.player == 1)
						{
							AI.danger[newX][newY] = true;
						}//end if
						//stop generating moves when friendly piece is in the way
						if (GameScreen.board[newX][newY] != null && GameScreen.board[newX][newY].player == this.player)
						{
							break;
						}//end if
						//add moves if piece is cooled down
						if (this.coolDown == 0)
						{
							moves.add(new Coord(newX, newY));
						}//end if
						//stop generating moves if enemy is in the way
						if (GameScreen.board[newX][newY] != null && GameScreen.board[newX][newY].player != this.player)
						{
							break;
						}//end if
					}
					else
					{
						break;
					}//end if
				}
				for(int x = 1; x < GameScreen.BOARD_W; x++)
				{
					int newX = xPos - x;
					int newY = yPos + x;
					if (Coord.inBoard(newX, newY))
					{
						if (this.player == 1)
						{
							AI.danger[newX][newY] = true;
						}
						if (GameScreen.board[newX][newY] != null && GameScreen.board[newX][newY].player == this.player)
						{
							break;
						}
						if (this.coolDown == 0)
						{
							moves.add(new Coord(newX, newY));
						}
						if (GameScreen.board[newX][newY] != null && GameScreen.board[newX][newY].player != this.player)
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
					if (Coord.inBoard(newX, newY))
					{
						if (this.player == 1)
						{
							AI.danger[newX][newY] = true;
						}
						if (GameScreen.board[newX][newY] != null && GameScreen.board[newX][newY].player == this.player)
						{

							break;
						}
						if (this.coolDown == 0)
						{
							moves.add(new Coord(newX, newY));
						}
						if (GameScreen.board[newX][newY] != null && GameScreen.board[newX][newY].player != this.player)
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
					//generate coordinates
					int newX = xPos + offset[x][0];
					int newY = yPos + offset[x][1];
					if (Coord.inBoard(newX, newY))
					{
						//mark moves for player as dangerous for AI
						if (this.player == 1)
						{
							AI.danger[newX][newY] = true;
						}//end if
						
						//add move
						if ((GameScreen.board[newX][newY] == null || GameScreen.board[newX][newY].player != this.player) && this.coolDown == 0)
						{
							moves.add(new Coord(newX, newY));
						}//end if
					}//end of
				}//end for
				break;
			case ROOK:
				//checks in four directions until it reaches the edge of the board or a piece
				for(int x = 1; x < GameScreen.BOARD_W; x++)
				{
					int newX = xPos + x;
					//if in board
					if (Coord.inBoard(newX, yPos))
					{
						//mark danger for AI
						if (this.player == 1)
						{
							AI.danger[newX][yPos] = true;
						}
						//break when friendly piece is in the way
						if (GameScreen.board[newX][yPos] != null && GameScreen.board[newX][yPos].player == this.player)
						{
							break;
						}
						//add coordinate if piece is cooled down
						if (this.coolDown == 0)
						{
							moves.add(new Coord(newX, yPos));
						}
						//break when enemy piece is in the way
						if (GameScreen.board[newX][yPos] != null && GameScreen.board[newX][yPos].player != this.player)
						{
							break;
						}
					}
					//break when the edge of the board is reached
					else
					{
						break;
					}//end if
				}//end for
				
				for(int x = 1; x < GameScreen.BOARD_W; x++)
				{
					int newX = xPos - x;
					if (Coord.inBoard(newX, yPos))
					{
						if (this.player == 1)
						{
							AI.danger[newX][yPos] = true;
						}//end if
						if (GameScreen.board[newX][yPos] != null && GameScreen.board[newX][yPos].player == this.player)
						{
							break;
						}//end if
						if (this.coolDown == 0)
						{
							moves.add(new Coord(newX, yPos));
						}//end if
						if (GameScreen.board[newX][yPos] != null && GameScreen.board[newX][yPos].player != this.player)
						{
							break;
						}//end if
					}
					else
					{
						break;
					}//end if
				}//end for
				
				for(int x = 1; x < GameScreen.BOARD_H; x++)
				{
					int newY = yPos + x;
					if (Coord.inBoard(xPos, newY))
					{
						if (this.player == 1)
						{
							AI.danger[xPos][newY] = true;
						}//end if

						if (GameScreen.board[xPos][newY] != null && GameScreen.board[xPos][newY].player == this.player)
						{
							break;
						}//end if
						if (this.coolDown == 0)
						{
							moves.add(new Coord(xPos, newY));
						}//end if
						if (GameScreen.board[xPos][newY] != null && GameScreen.board[xPos][newY].player != this.player)
						{
							break;
						}//end if
					}
					else
					{
						break;
					}//end if
				}//end for
				
				for(int x = 1; x < GameScreen.BOARD_H; x++)
				{
					int newY = yPos - x;
					if (Coord.inBoard(xPos, newY))
					{
						if (this.player == 1)
						{
							AI.danger[xPos][newY] = true;
						}//end if
						if (GameScreen.board[xPos][newY] != null && GameScreen.board[xPos][newY].player == this.player)
						{
							break;
						}//end if
						if (this.coolDown == 0)
						{
							moves.add(new Coord(xPos, newY));
						}//end if
						if (GameScreen.board[xPos][newY] != null && GameScreen.board[xPos][newY].player != this.player)
						{
							break;
						}//end if
					}
					else
					{
						break;
					}//end if
				}//end for
				break;
			case PAWN:
				//human
				//move forward one square
				if (player == 1 && Coord.inBoard(xPos, yPos - 1) && GameScreen.board[xPos][yPos - 1] == null && this.coolDown == 0)
				{
					moves.add(new Coord(xPos, yPos - 1));
				}//end if
				if (player == 1 && Coord.inBoard(xPos + 1, yPos - 1))
				{
					//mark the front diagonal squares as dangerous
					AI.danger[xPos + 1][yPos - 1] = true;
					
					//only allow diagonal move if pawn can capture piece
					if (GameScreen.board[xPos + 1][yPos - 1] != null && GameScreen.board[xPos + 1][yPos - 1].player != this.player && this.coolDown == 0)
					{
						moves.add(new Coord(xPos + 1, yPos - 1));
					}//end if
				}//end if
				if (player == 1 && Coord.inBoard(xPos - 1, yPos - 1))
				{
					//mark the front diagonal squares as dangerous
					AI.danger[xPos - 1][yPos - 1] = true;
					
					//only allow diagonal move if pawn can capture piece
					if (GameScreen.board[xPos - 1][yPos - 1] != null && GameScreen.board[xPos - 1][yPos - 1].player != this.player && this.coolDown == 0)
					{
						moves.add(new Coord(xPos - 1, yPos - 1));
					}//end if
				}//end if
				//move an extra square the first move
				if (player == 1 && !moved && Coord.inBoard(xPos, yPos - 2) && GameScreen.board[xPos][yPos - 2] == null && GameScreen.board[xPos][yPos - 1] == null)
				{
					moves.add(new Coord(xPos, yPos - 2));
				}//end if
				
				//break when piece isn't cooled down, since danger doesn't need to be generated
				if (player == 2 && coolDown == 0)
				{
					//move forward one square
					if (Coord.inBoard(xPos, yPos + 1) && GameScreen.board[xPos][yPos + 1] == null)
					{
						moves.add(new Coord(xPos, yPos + 1));
					}//end if
					//move diagonal forward when the AI can capture pieces
					if (Coord.inBoard(xPos + 1, yPos + 1) && GameScreen.board[xPos + 1][yPos + 1] != null && GameScreen.board[xPos + 1][yPos + 1].player != this.player)
					{
						moves.add(new Coord(xPos + 1, yPos + 1));
					}//end if
					if (Coord.inBoard(xPos - 1, yPos + 1) && GameScreen.board[xPos - 1][yPos + 1] != null && GameScreen.board[xPos - 1][yPos + 1].player != this.player)
					{
						moves.add(new Coord(xPos - 1, yPos + 1));
					}//end if
					//move an extra square the first move
					if (!moved && Coord.inBoard(xPos, yPos + 2) && GameScreen.board[xPos][yPos + 2] == null && GameScreen.board[xPos][yPos + 1] == null)
					{
						moves.add(new Coord(xPos, yPos + 2));
					}//end if
					break;
				}
				else
				{
					break;
				}//end if
		}//end switch
	}//end updateMoves
	
	/**
	 * a method to return an estimated value of the piece
	 * @param - none
	 * @return int - the estimated value of the piece
	 */
	public int getValue()
	{
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
		}//end switch
		return 0;
	}//end getValue method
}//end Piece class