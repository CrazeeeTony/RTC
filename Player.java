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
	//scores for the highscore: by time taken (milliseconds), pieces lost, and both
	int timeTaken = 0;
	int piecesLost = 0;
	double score = 100.0;
	
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
	 * given a specific ID of piece, select the first one
	 * but if one is already selected, select the first one to appear after it in the list
	 */
	public void nextPiece(int identity)
	{
		if (selected != null && selected.pieceID == identity)
		{
			for (int e = controllable.indexOf(selected) + 1; e < controllable.size(); e++)
			{
				if (controllable.get(e).pieceID == identity && controllable.get(e).moves.size() > 0)
				{
					selected = controllable.get(e);
					return;
				}
			}
		}
		for (Piece e : controllable)
		{
			if (e.pieceID == identity && e.moves.size() > 0)
			{
				selected = e;
				return;
			}
		}
	}
	
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
	 * given the opportunity, takes a piece with the selected
	 */
	public void takePiece()
	{
		if (selected != null)
		{
			for (Coord e : selected.moves)
			{
				if (GameScreen.board[e.x][e.y] != null)
				{
					this.move(e.x, e.y);
					return;
				}
			}
		}
	}
	
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
			//update score for a piece lost
			piecesLost++;
			score -= 5.0;
		}
	}//end member kill
	
	/**
	 * update moves for all this player's pieces
	 */
	public void updateMoves()
	{
		//update all moves for all pieces
		for (Piece e : controllable)
			e.updateMoves();
		//update score (score goes down 0.001% every game tick, from 100%)
		timeTaken += 10;
		score -= 0.001;
	}
	
}//end class Player


