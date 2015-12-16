
public class Player
{
	private Piece[][] controllable;
	Piece selected;
	
	public Player(Piece[][] initialPieces)
	{
		controllable = initialPieces;
	}//end contructor (int, int)
  
	/**
	 * takes an x and y for a board's coordinates at which to find a piece
	 * returns a success boolean: if null, then false, if a piece exists there, then true
	 */
	public boolean select(int x, int y)
	{
		if (controllable[x][y] == null)
		{
			return false;
		}
		else
		{
			//select the piece
			selected = controllable[x][y];
			return true;
		}//end if
	}
	
	public void move(int attemptX, int attemptY)
	{
		if (true)		//NEEDED: method to confirm the validity of movement of a piece
		{
			controllable[selected.xPos][selected.yPos] = null;
			controllable[attemptX][attemptY] = selected;
			selected.xPos = attemptX;
			selected.yPos = attemptY;
		}
	}
	
}//end class Player
