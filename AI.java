import java.util.ArrayList;
/**
 * class for algorithm based decision making
 * @author Charles Lei, Tony Li
 */
public class AI extends Player
{
	//objects used to represent possible moves
	//used to classify moves by how optimal they are
	//also used to store information about moves easily (piece, place, etc.)
	static class Move
	{
		static boolean danger[][] = new boolean [GameScreen.BOARD_W][GameScreen.BOARD_H];
		Piece consider;
		Piece target;
		int targetX, targetY;
		
		/**
		 *
		 * @author Charles Lei
		 */
		public Move(Piece consider, Piece target, int targetX, int targetY)
		{
			this.consider = consider;
			//targeted piece is the piece taken, be careful might be NULL
			this.target = target;
			this.targetX = targetX;
			this.targetY = targetY;
		}//end constructor Move(Piece, Piece, int, int)
		
		/**
		 * gets an integer representing how optimal the move is (lower is better)
		 * 
		 * @author Tony Li
		 */
		public int getOptimal()
		{
			if(target != null && target.pieceID == Piece.KING)
			{
				return -999999999;
			}
			if(consider.pieceID == Piece.KING && danger[consider.xPos][consider.yPos] && !danger[targetX][targetY])
			{
				return -99999999;
			}
			int takenPieceVal = target.getValue();
			int lossVal = 0;
			if(danger[target.xPos][target.yPos])
			{
				lossVal += consider.getValue();
			}
			if(danger[consider.xPos][consider.yPos])
			{
				lossVal -= consider.getValue();
			}
			return lossVal - takenPieceVal;
		}
		
		/**
		 * Update the 2D array which indicates dangerous squares
		 * @param Player pl - the human player
		 * @author Tony Li
		 */
		public static void updateDanger(Player pl)
		{
			for(int x = 0; x < danger.length; x++)
			{
				for(int y = 0; y < danger[x].length; y++)
				{
					danger[x][y] = false;
				}
			}
			for(Piece pc : pl.controllable)
			{
				for(Coord move : pc.moves)
				{
					if(pc.pieceID != Piece.PAWN || move.x != pc.xPos)
					{
						danger[move.x][move.y] = true;
					}
				}
			}
		}
		
		/**
		 *
		 * @author Tony Li
		 */
		public boolean compare(Move other)
		{
			//TEMP
			return true;
		}
	}
	
 	//represents difficulty: lower is better (subject to change)
 	public int difficulty;
 	
 	/**
 	 * initialize the underlying Player, but also add in a parameter for the difficulty
	 * @author Charles Lei
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
 	 * quicksorts an array of Move objects, using the instance compare method
 	 * ascending order, best moves at the front
	 * @author Charles Lei
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