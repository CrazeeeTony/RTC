import java.util.ArrayList;
/**
 * class for algorithm based decision making
 * @author Charles Lei, Tony Li
 */
public class AI extends Player
{
	//represents which squares will put a piece in danger of being captured
	static boolean danger[][] = new boolean [GameScreen.BOARD_W][GameScreen.BOARD_H];
	//represents which squares will deter a capture via a friendly piece being able to protect that square
	static int safety[][] = new int[GameScreen.BOARD_W][GameScreen.BOARD_H];
	/**
	 * objects used to represent possible moves
	 * used to classify moves by how optimal they are
	 * also used to store information about moves easily (piece, place, etc.)
	 * @author Tony Li, Charles Lei
	 */
	static class Move
	{
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
			int takenPieceVal = 0;
			int lossVal = 0;
			if(target != null)
			{
				//capturing a king is the best
				if(target.pieceID == Piece.KING)
				{
					return -999999999;
				}
				takenPieceVal = target.getValue() + 1;
			}
			//always try to protect the king
			if(consider.pieceID == Piece.KING && danger[consider.xPos][consider.yPos] && !danger[targetX][targetY])
			{
				return -99999999;
			}
			//consider danger
			if(danger[targetX][targetY])
			{
				lossVal += consider.getValue();
				if(danger[consider.xPos][consider.yPos])
				{
					lossVal -= consider.getValue();
				}
			}
			//take into account how this moves affects the AI's control of the board
			int positionAdvantage = 0;
			//for now, just moving forward is a good thing
			positionAdvantage = consider.yPos - targetY;
			//moving into safety is also preferred
			if (consider.pieceID == Piece.PAWN)
			{
				if (Coord.inBoard(consider.xPos - 1, consider.yPos + 1))    //assuming AI is black always
					safety[consider.xPos - 1][consider.yPos + 1]--;
				if (Coord.inBoard(consider.xPos + 1, consider.yPos + 1))
					safety[consider.xPos + 1][consider.yPos + 1]--;
			}
			else
			{
				for (Coord e : consider.moves)
					safety[e.x][e.y]--;
			}
			if (safety[targetX][targetY] > 0)
				positionAdvantage -= 50;
			if (consider.pieceID == Piece.PAWN)
			{
				if (Coord.inBoard(consider.xPos - 1, consider.yPos + 1))    //assuming AI is black always
					safety[consider.xPos - 1][consider.yPos + 1]++;
				if (Coord.inBoard(consider.xPos + 1, consider.yPos + 1))
					safety[consider.xPos + 1][consider.yPos + 1]++;
			}
			else
			{
				for (Coord e : consider.moves)
					safety[e.x][e.y]++;
			}
			return (lossVal - takenPieceVal) * 10 + positionAdvantage;
		}
		
		/**
		 *
		 * @author Tony Li
		 */
		public boolean compare(Move other)
		{
			return this.getOptimal() < other.getOptimal();
		}
		
		/**
		 * Set the entire 2D array which indicates dangerous squares as safe
		 * also reset the entire 2D array indicating protection (safety)
		 * @param Player pl - the human player
		 * @author Tony Li
		 */
		public static void clearDanger()
		{
			for(int x = 0; x < danger.length; x++)
			{
				for(int y = 0; y < danger[x].length; y++)
				{
					danger[x][y] = false;
					safety[x][y] = 0;
				}
			}
		}
	}
	
 	//represents difficulty: lower is better (subject to change)
 	public double difficulty;
 	
 	/**
 	 * initialize the underlying Player, but also add in a parameter for the difficulty
	 * @author Charles Lei
 	 */
 	public AI(Piece[][] baseBoard, int baseIdentity, double difficulty)
 	{
 		super(baseBoard, baseIdentity);
 		this.difficulty = difficulty;
 	}//end constructor (Piece[][], int, int)
	
	/**
	 * returns a list of all possible moves for all possible pieces
	 */
	public ArrayList<Move> getAllMoves()
	{
		ArrayList<Move> validMoves = new ArrayList<>();
		for (Piece considering : controllable)
		{
			for (Coord e : considering.moves)
			{
				//caution: may be null
				Piece target = GameScreen.board[e.x][e.y];
				validMoves.add(new AI.Move(considering, target, e.x, e.y));
			}
		}
		return validMoves;
	}//end member getAllMoves
 	
 	/**
 	 * get a move out of all possible ones
	 * @author Charles Lei
 	 */
 	public void makeMove(Player opponent)
 	{ 		
 		//get all the moves, convert from ArrayList to array
 		ArrayList<Move> availableMoves = this.getAllMoves();
 		Move[] sortedMoves = new Move[availableMoves.size()];
 		sortedMoves = availableMoves.toArray(sortedMoves);
 		//sort the moves
 		quicksort(sortedMoves, 0, sortedMoves.length - 1);
 		//based on difficulty, select a better or worse move (but never select one out of bounds of the array)
		int lastGoodMove = binarySearch(sortedMoves, 0);
		//no good move available, make a neutral move
		if (lastGoodMove == -1)
			lastGoodMove = binarySearch(sortedMoves, 1);
		//no decent move available, don't make any move
		if (lastGoodMove == -1)
			return;
		//make the move
 		Move selectedMove = sortedMoves[(int)(Math.random() * lastGoodMove)];
		this.selected = selectedMove.consider;
		this.move(selectedMove.targetX, selectedMove.targetY);
 	}//end member makeMove
	
	/**
	 * updates all available moves just like the underlying Player class
	 * also updates the safety array
	 * @override
	 */
	public void updateMoves()
	{
		super.updateMoves();
		for (Move e : this.getAllMoves())
		{
			//special case for pawn: check the diagonals and make sure to only add consideration for one move per pawn
			if (e.consider.pieceID == Piece.PAWN)
			{
				if (e.targetY - e.consider.yPos == 1)
				{
					if (Coord.inBoard(e.consider.xPos - 1, e.consider.yPos + 1))
						safety[e.consider.xPos - 1][e.consider.yPos + 1]++;
					if (Coord.inBoard(e.consider.xPos + 1, e.consider.yPos + 1))
						safety[e.consider.xPos + 1][e.consider.yPos + 1]++;
				}
			}
			else
			{
				safety[e.targetX][e.targetY]++;
			}
		}
	}
  	
	/**
	 * overload of quicksort for convenient function calling
	 */
	public static void quicksort(Move[] ll, int lBound, int rBound)
	{
		quicksort(ll, lBound, rBound, new Move[ll.length]);
	}
	
 	/**
 	 * quicksorts an array of Move objects, using the instance compare method
 	 * ascending order, best moves at the front
	 * @author Charles Lei
 	 */
  	public static void quicksort(Move[] ll, int lBound, int rBound, Move[] buffer)
  	{
 		//if the length within the bound is already sorted (one element or less), stop
  		if (lBound >= rBound) return;
  
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
  		for(int e = lBound; e <= rBound; e++)
  		{
  			ll[e] = buffer[e];
  		}
		//done; quicksort the two parts beside the pivot
  		quicksort(ll, lBound, lPoint - 1, buffer);
  		quicksort(ll, rPoint + 1, rBound, buffer);
	}//end static member quicksort
	
	/**
	 * binary search an array for an element
	 * returns the last element which is strictly less than the target value
	 */
	public int binarySearch(Move[] arr, int val)
	{
		//place: keep track of the lowest possible place to be searched
		//jump: keep track of the distance from the lowest place to the highest
		int place = -1, jump = arr.length;
		//this is a totally legit binary search
		while (jump > 0)
		{
			if (place + jump < arr.length && arr[place + jump].getOptimal() < val)
				place += jump;
			//jump repreatedly halves
			jump /= 2;
		}
		while (place + 1 < arr.length && arr[place + 1].getOptimal() < val)
			place++;
		return place;
	}
 }//end class AI