/**
 * logic for each player of the game
 * stores pieces, handles moving pieces, keeps track of scores
 * @author Charles Lei
 */
import java.util.ArrayList;
public class Player
{
	/****
	 dictionary:
	  controllable - list of pieces under this player's control
	  captured - captured pieces
	  selected - selected piece (NULL is none is selected)
	  timeTaken - score by time taken
	  piecesLost - score by value of pieces lost
	  score -
  	    combined score (lower is better - just like golf)
		the reason a higher score is worse is to ensure that very poor scores don't become negative
	****/
	ArrayList<Piece> controllable = new ArrayList<>();
	ArrayList<Piece> captured = new ArrayList<>();
	Piece selected;
	double timeTaken = 0.0;
	double piecesLost = 0.0;
	double score = 0.0;
	
	/**
	 * use a board (Piece 2D array) and integer identity to find all belonging pieces
	 * then adds them to a stored list of pieces, to signify that they are under this player's control
	 * @Piece[][] initialPieces - the board array from which pieces will be detected
	 * @int identity - the identity of the player, where each belonging piece has this identity as well
	 */
	public Player(Piece[][] initialPieces, int identity)
	{
		//go through everything
		for (Piece[] row : initialPieces)
		{
			for (Piece p : row)
			{
				//check null, check identity
				if (p != null && p.player == identity)
					controllable.add(p);
			}//end for
		}//end for
	}//end contructor (Piece[][], int)
  
	/**
	 * takes an x and y for coordinate at which to find a piece; tries to select that piece
	 * @param int x - the x coordinate on the main board (GameScreen.board)
	 * @param int y - the y coordinate on the main board
	 * @return void
	 */
	public void select(int x, int y)
	{
		//special case: clicking the selected piece deselects it and does nothing else
		if (selected != null && x == selected.xPos && y == selected.yPos)
		{
			selected = null;
			return;
		}//end if
		
		//otherwise, start by deselecting the current piece
		selected = null;
		//sequential search for the piece at (x,y)
		for (Piece e : controllable)
		{
			if (e.xPos == x && e.yPos == y)
			{
				selected = e;
				break;
			}//end if
		}//end for
	}//end member select
	
	/**
	 * given a specific ID of piece, select the first one
	 * but if one is already selected, select the first one to appear after it in the list
	 * used as a means of automatically a specific type and cycling through pieces, called via hotkeys
	 * @param int identity - identity of the piece to look for
	 * @return void
	 */
	public void nextPiece(int identity)
	{
		//if current piece already meets the requirements, find the next one in the list
		if (selected != null && selected.pieceID == identity)
		{
			for (int e = controllable.indexOf(selected) + 1; e < controllable.size(); e++)
			{
				//note that the piece must have valid moves
				if (controllable.get(e).pieceID == identity && controllable.get(e).moves.size() > 0)
				{
					selected = controllable.get(e);
					return;
				}//end if
			}//end for
		}//end if
		
		//start from the beginning, searching for a valid piece to select (it is possible that none will be found)
		for (Piece e : controllable)
		{
			//note that the piece must have valid moves
			if (e.pieceID == identity && e.moves.size() > 0)
			{
				selected = e;
				return;
			}//end if
		}//end for
	}//end member nextPiece
	
	/**
	 * move the selected piece, if one is selected
	 * @param int x - x coordinate to move to
	 * @param int y - y coordinate to move to
	 * @return void
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
		}//end for
		
		if (validate)
		{
			//also, check for null: if selected is null, then no action necessary
			if (selected != null)
				selected.moveTo(x, y);
		}//end if
	}//end member move
	
	/**
	 * given the opportunity, takes a piece with the selected one
	 * called by hotkeys as a convenience feature
	 * @param none
	 * @return void
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
				}//end if
			}//end for
		}//end if
	}//end member takePiece
	
	/**
	 * has this player lost?
	 * @param none
	 * @return boolean - true only if the player has lost according to the current win condition
	 */
	public boolean hasLost()
	{
		if (DetectAction.completeKillMode)
		{
			//complete kill mode requires all pieces to be captured for the game to be over
			return controllable.size() == 0;
		}
		else
		{
			//when complete kill mode is off, only the king must be captured
			boolean hasKing = false;
			for (Piece e : controllable)
				if (e.pieceID == Piece.KING)
					hasKing = true;
			return !hasKing;
		}//end if
	}//end member hasLost
	
	/**
	 * a piece belonging to this player is flagged as captured, or "killed", by calling this method
	 * used when a piece is taken by the opponent
	 * note that each player stores its own captured pieces, as the player class still needs to access them
	 * @param Piece p - the piece to kill
	 * @return void
	 */
	public void kill(Piece p)
	{
		//check null
		if (p != null)
		{
			//cannot be controlled any more, but add it to captured list
			controllable.remove(p);
			captured.add(p);
			
			//deselect it
			if (selected == p)
			{
				selected = null;
			}//end if
			//update score for a piece lost
			piecesLost += p.getValue();
			score += p.getValue() * 10;
		}//end if
	}//end member kill
	
	/**
	 * update moves for all controllable pieces
	 * called once per gameloop, and updates time score as the game goes on
	 * @param none
	 * @return void
	 */
	public void updateMoves()
	{
		//update all moves for all pieces
		for (Piece e : controllable)
		{
			e.updateMoves();
		}//end for
		
		//update score (score goes down 0.001% every game tick, from 100%)
		timeTaken += 0.01;
		score += 0.01;
	}//end member updateMoves
	
}//end class Player


