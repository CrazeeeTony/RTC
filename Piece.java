import java.awt.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;
public class Piece{
	public static final int KING = 0;
	public static final int QUEEN = 1;
	public static final int BISHOP = 2;
	public static final int KNIGHT = 3;
	public static final int ROOK = 4;
	public static final int PAWN = 5;
	static final BufferedImage[] imgs1;
	static final BufferedImage[] imgs2;
	
	static{
		try{
			imgs1 = {ImageIO.read(new File("Piece Images/1King.png")), ImageIO.read(new File("Piece Images/1Queen.png")),
			ImageIO.read(new File("Piece Images/1Boshop.png")), ImageIO.read(new File("Piece Images/1Knight.png")), ImageIO.read(new File("Piece Images/1Rook.png"))
			ImageIO.read(new File("Piece Images/1Pawn.png"))};
			
			imgs2 = {ImageIO.read(new File("Piece Images/2King.png")), ImageIO.read(new File("Piece Images/2Queen.png")),
			ImageIO.read(new File("Piece Images/2Boshop.png")), ImageIO.read(new File("Piece Images/2Knight.png")), ImageIO.read(new File("Piece Images/2Rook.png"))
			ImageIO.read(new File("Piece Images/2Pawn.png"))};
		}
		catch(Exception e){
			System.out.println("Error Loading Images");
			e.printStackTrace();
		}
	}
	
	//1=player(white and on bottom), 2=computer(black and on top)
	int player;
	int xPos;
	int yPos;
	int pieceID;
	long coolDown;
	BufferedImage img;
	
	public Piece(int player,int xPos, int yPos, int pieceID){
		this.player player;
		this.xPos = xPos;
		this.yPos = yPos;
		this.pieceID = pieceID;
		if(player == 1){
			this.img = imgs1[pieceID];
		}
		else if(player == 2){
			this.img = imgs2[pieceID];
		}
	}
}