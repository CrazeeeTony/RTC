import java.util.ArrayList;
/**
 * class for algorithm based decision making
 * inherits the Player class to use its methods as a standalone object
 */
public class AI extends Player
{
	//objects used to represent possible moves
	//used to classify moves by how optimal they are
	//also used to store information about moves easily (piece, place, etc.)
	static class Move
	{
		//piece selected, piece which will be taken if the move is made (maybe null), position of piece afterwards
		Piece consider;
		Piece target;
		int targetX, targetY;
		/**
		 * set all the variables
		 */
		public Move(Piece consider, Piece target, int targetX, int targetY)
		{
			this.consider = consider;
			//targeted piece is the piece taken, be careful might be NULL
			this.target = target;
			this.targetX = targetX;
			this.targetY = targetY;
		}//end constructor (Piece, Piece, int, int)
		
		//gets an integer representing how optimal the move is (lower is better)
		public int getOptimal(Move [] enemyMoves)
		{
			if(target != null && target.pieceID == Piece.KING)
			{
				return -999999999;
			}
			for(int x = 0; x < enemyMoves.length; x++)
			{
				if(enemyMoves[x].target.pieceID == Piece.KING && this.consider.pieceID == Piece.KING)
				{
					return -99999999;
				}
			}
			int takenPieceVal = target.getValue();
			int lossVal = 0;
			//temp return statement so that this compiles
			return 0;
		}//end member getOptimal
		
		/**
		 * compares this move with another, true if this one is better (more optimal)
		 */
		public boolean compare(Move other)
		{
			//TEMP
			return true;
		}//end member compare
	}//end static class Move
	
	//represents difficulty: lower is better (subject to change)
	public int difficulty;
	
	/**
	 * initialize the underlying Player, but also add in a parameter for the difficulty
	 */
	public AI(Piece[][] baseBoard, int baseIdentity, int difficulty)
	{
		super(baseBoard, baseIdentity);
		this.difficulty = difficulty;
	}//end constructor (Piece[][], int, int)
	
	/**
	 * get a move out of all possible ones
	 */
	public Move makeMove(Player opponent)
	{
		//opponent is unused right now, will be implemented later to increase the "danger" values of certain spaces
		
		//get all the moves, convert from ArrayList to array
		ArrayList<Move> availableMoves = this.getAllMoves();
		Move[] sortedMoves = new Move[availableMoves.size()];
		sortedMoves = availableMoves.toArray(sortedMoves);
		//sort the moves
		quicksort(sortedMoves, 0, sortedMoves.length - 1);
		//based on difficulty, select a better or worse move (but never select one out of bounds of the array)
		return sortedMoves[Math.min(difficulty, sortedMoves.length - 1)];
	}//end member makeMove
	
	/**
	 * try to create a move object by moving a piece at a certain location to another
	 * nothing exists, or invalid move: return NULL
	 */
	public static Move attemptMove(Piece[][] state, int placeX, int placeY, int targetX, int targetY)
	{
		//if no piece exists
		if (state[placeX][placeY] == null)
		{
			return null;
		}
		//if piece cannot move to that location (validateMove is used as placeholder)
		/*
		else if (!state[placeX][placeY].validateMove(targetX, targetY))
		{
			return null;
		}
		*/
		else
		{
			//second parameter might be null
			return new Move(state[placeX][placeY], state[targetX][targetY], targetX, targetY);
		}
	}//end static member attemptMove
	
	/**
	 * quicksorts an array of Move objects, using the instance compare method
	 * ascending order, best moves at the front
	 */
	public static void quicksort(Move[] ll, int lBound, int rBound)
	{
		//if the length within the bound is already sorted (one element or less), stop
		if (lBound >= rBound) return;

		//standard quicksort: buffer to store items, pivot
		Move[] buffer = new Move[ll.length];
		Move pivot = ll[lBound];
		//dynamic pointer variables within the array
		int lPoint = lBound, rPoint = rBound;
		//go from the left bound to the right, stack elements onto either left or right based on pivot compare
		for (int e = lBound + 1; e <= rBound; e++)
		{
			if (ll[e].compare(pivot))
			{
				buffer[lPoint] = ll[e];
				lPoint++;
			}
			else
			{
				buffer[rPoint] = ll[e];
				rPoint--;
			}
		}
		//put the pivot in the space remaining
		buffer[lPoint] = pivot;
		//write the buffer
		for (int e = lBound; e <= rBound; e++)
		{
			ll[e] = buffer[e];
		}
		//done; quicksort the two parts beside the pivot
		quicksort(ll, lBound, lPoint - 1);
		quicksort(ll, rPoint + 1, rBound);
	}//end static member quicksort
}//end class AI
