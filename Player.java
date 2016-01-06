import java.util.ArrayList;
public class Player
{
	ArrayList<Piece> controllable = new ArrayList<>();
	Piece selected;
	
	public Player(Piece[][] initialPieces, int identity)
	{
		for (Piece[] row : initialPieces)
		{
			for (Piece p : row)
			{
				if (p != null && p.player == identity)
					controllable.add(p);
			}
		}
	}//end contructor (int, int)
  
	/**
	 * takes an x and y for a board's coordinates at which to find a piece
	 * returns a success boolean: if null, then false, if a piece exists there, then true
	 */
	public void select(int x, int y)
	{
		//special case: clicking the selected piece deselects it
		if (selected != null && x == selected.xPos && y == selected.yPos)
		{
			selected = null;
			return;
		}
		//deselect the piece
		selected = null;
		//sequential search for the piece
		for (Piece e : controllable)
		{
			if (e.xPos == x && e.yPos == y)
			{
				if (selected == e)
					selected = null;
				else
					selected = e;
				break;
			}
		}
	}
	
	public void move(int x, int y)
	{
		//confirm the validity of the proposed movement
		boolean validate = false;
		//sequential search to find a corresponding move in the list of valid moves
		for (Coord e : selected.moves)
		{
			if (e.x == x && e.y == y)
				validate = true;
		}
		if (validate)
		{
			if (selected != null)
				selected.moveTo(x, y);
		}
	}
	
	public ArrayList<AI.Move> getAllMoves()
	{
		ArrayList<AI.Move> validMoves = new ArrayList<>();
		for (Piece considering : controllable)
		{
			considering.updateMoves();
			for (Coord e : considering.moves)
			{
				//caution: may be null
				Piece target = GameScreen.board[e.x][e.y];
				//NEEDED: confirm a move is valid
				validMoves.add(new AI.Move(considering, target, e.x, e.y));
			}
		}
		return validMoves;
	}
	
}//end class Player


