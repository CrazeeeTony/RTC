import java.util.ArrayList;
/**
 * logic for each player of the game
 * @author Charles Lei
 */
public class Player
{
	//list of pieces under this player's control, captured pieces, selected piece
	ArrayList<Piece> controllable = new ArrayList<>();
	ArrayList<Piece> captured = new ArrayList<>();
	Piece selected;
	
	/**
	 * give a board (Piece 2D array) and integer to find all belonging pieces
	 */
	public Player(Piece[][] initialPieces, int identity)
	{
		//go through everything
		for (Piece[] row : initialPieces)
		{
			for (Piece p : row)
			{
				if (p != null && p.player == identity)
					controllable.add(p);
			}
		}
	}//end contructor (Piece[][], int)
  
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
	}//end member select
	
	/**
	 * move the selected piece
	 */
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
	}//end member move
	
	/**
	 * returns a list of all possible moves for all possible pieces
	 */
	public ArrayList<AI.Move> getAllMoves()
	{
		ArrayList<AI.Move> validMoves = new ArrayList<>();
		for (Piece considering : controllable)
		{
			for (Coord e : considering.moves)
			{
				//caution: may be null
				Piece target = GameScreen.board[e.x][e.y];
				//NEEDED: confirm a move is valid
				validMoves.add(new AI.Move(considering, target, e.x, e.y));
			}
		}
		return validMoves;
	}//end member getAllMoves
	
	/**
	 * has this player lost?
	 */
	public boolean hasLost()
	{
		return controllable.size() == 0;
	}//end member hasLost
	
	/**
	 * a piece belonging to this player should be considered captured, or "killed"
	 */
	public void kill(Piece e)
	{
		if (e != null)
		{
			//cannot be controlled any more, but add it to captured list
			controllable.remove(e);
			captured.add(e);
			//deselect it
			if (selected == e)
				selected = null;
		}
	}//end member kill
	
}//end class Player


