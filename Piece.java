public class Piece{
	public static final int KING = 0;
	public static final int QUEEN = 1;
	public static final int BISHOP = 2;
	public static final int KNIGHT = 3;
	public static final int ROOK = 4;
	public static final int PAWN = 5;
	
	int xPos;
	int yPos;
	int pieceID;
	long coolDown;
	
	public Piece(int xPos, int yPos, int pieceID){
		this.xPos = xPos;
		this.yPos = yPos;
		this.pieceID = pieceID;
	}
}