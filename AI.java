/**
 * class for algorithm based decision making
 * also contains a Move class for managing possible moves to make
 * @author Charles Lei, Tony Li
 */
import java.util.ArrayList;
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
		/****
		 dictionary:
		  consider - the piece to be moved
		  target - the piece taken if this move is executed, might be NULL if none exists
		  targetX - the x coordinate of the square to move to
		  targetY - the y coordinate of the square to move to
		****/
		Piece consider;
		Piece target;
		int targetX;
		int targetY;
		
		/**
		 * create a new move given the parameters
		 * @param Piece consider - piece to be moved
		 * @param Piece target - piece to take
		 * @param int targetX - x coord. to move to
		 * @param int targetY - y coord. to move to
		 * @author Charles Lei
		 *    #Charles
		 */
		public Move(Piece consider, Piece target, int targetX, int targetY)
		{
			this.consider = consider;
			this.target = target;
			this.targetX = targetX;
			this.targetY = targetY;
		}//end constructor Move(Piece, Piece, int, int)
		
		/**
		 * gets an integer representing how optimal the move is (lower is better)
		 * @param none
		 * @return int - an integer representing how good the move is, where < 0 is good, 0 is neutral, > 0 is bad
		 * @author Tony Li
		 *    #level : precise calculations result in a substantially better AI than one which makes random decisions
		 *    #Tony
		 */
		public int getOptimal()
		{
			//store the potential to take a piece, and to lose a piece
			int takenPieceVal = 0;
			int lossVal = 0;
			
			//check if this move can take a piece, if so, consider the piece's value
			if(target != null)
			{
				//capturing a king is the best
				if(target.pieceID == Piece.KING)
				{
					return -999999999;
				}//end if
				takenPieceVal = target.getValue() + 1;
			}//end if
			
			//try very hard to protect the king
			if(consider.pieceID == Piece.KING && danger[consider.xPos][consider.yPos] && !danger[targetX][targetY])
			{
				return -99999999;
			}//end if
			
			//consider danger (an array which represents all places where this piece might be taken)
			if(danger[targetX][targetY])
			{
				lossVal += consider.getValue();
			}//end if
			if(danger[consider.xPos][consider.yPos])
			{
				lossVal -= consider.getValue();
			}//end if
			
			//take into account how this move affects the AI's control of the board
			int positionAdvantage = 0;
			
			//for now, just moving forward is a good thing
			positionAdvantage = consider.yPos - targetY;
			
			//moving into safety is also preferred, which is when a piece protects another from capture
			//update safety (special case for pawn: check diagonals)
			if (consider.pieceID == Piece.PAWN)
			{
				if (Coord.inBoard(consider.xPos - 1, consider.yPos + 1))
				{
					safety[consider.xPos - 1][consider.yPos + 1]--;
				}//end if
				if (Coord.inBoard(consider.xPos + 1, consider.yPos + 1))
				{
					safety[consider.xPos + 1][consider.yPos + 1]--;
				}//end if
			}
			else
			{
				for (Coord e : consider.moves)
				{
					safety[e.x][e.y]--;
				}//end for
			}//end if
			
			//moving into safety adds a position advantage
			if (safety[targetX][targetY] > 0)
			{
				positionAdvantage -= 10 *(4 - consider.getValue());
			}//end if
			//special case for pawn again
			if (consider.pieceID == Piece.PAWN)
			{
				if (Coord.inBoard(consider.xPos - 1, consider.yPos + 1))   
				{
					safety[consider.xPos - 1][consider.yPos + 1]++;
				}//end if
				if (Coord.inBoard(consider.xPos + 1, consider.yPos + 1))
				{
					safety[consider.xPos + 1][consider.yPos + 1]++;
				}//end if
			}
			else
			{
				for (Coord e : consider.moves)
				{
					safety[e.x][e.y]++;
				}//end for
			}//end if
			
			//human should move the other way
			if(consider.player == 1)
			{
				positionAdvantage *= -1;
			}
			
			//overall value of a move is based on what it might take, what it might lose, and how it affects board control
			return (lossVal - takenPieceVal) * 10 + positionAdvantage;
		}//end member getOptimal
		
		/**
		 * compares this move with another, returns true if this one is better
		 * @param Move other - the other move
		 * @param boolean - true if this move is better, false otherwise
		 * @author Tony Li
		 *    #Tony
		 */
		public boolean compare(Move other)
		{
			return this.getOptimal() < other.getOptimal();
		}//end member compare
		
		/**
		 * Set the entire 2D array which indicates dangerous squares as safe
		 * also reset the entire 2D array indicating protection (safety)
		 * @param none
		 * @return void
		 * @author Tony Li
		 *    #Tony
		 */
		public static void clearDanger()
		{
			for(int x = 0; x < danger.length; x++)
			{
				for(int y = 0; y < danger[x].length; y++)
				{
					danger[x][y] = false;
					safety[x][y] = 0;
				}//end for
			}//end for
		}//end member clearDanger
		
	}//end static class Move
	
 	//represents difficulty: higher is harder, from 1 to 10
 	public int difficulty = 5;
	
	/**
	 * same constructor as Player
	 * @Piece[][] initialPieces - the board array from which pieces will be detected
	 * @int identity - the identity of the player, where each belonging piece has this identity as well
	 * @author Tony Li
	 *    #Tony
	 */
	public AI(Piece[][] board, int identity)
	{
		super(board, identity);
	}//end constructor(Piece[][], identity)
	
	/**
	 * change difficulty
	 * @param int difficulty - difficulty level to be set
	 * @return void
	 * @author Tony Li
	 *    #Tony
	 */
 	public void setDifficulty(int difficulty)
	{
		this.difficulty = difficulty;
	}//end member setDifficulty
	
	/**
	 * returns a list of all possible moves for all possible pieces
	 * @param none
	 * @return ArrayList<Move> - a list of AI.Move objects, in no particular order
	 * @author Charles Lei
	 *    #Charles
	 *    #method
	 */
	public ArrayList<Move> getAllMoves()
	{
		ArrayList<Move> validMoves = new ArrayList<>();
		
		//loop through every piece which is active and belonging to this player
		for (Piece considering : controllable)
		{
			//consider all its moves
			for (Coord e : considering.moves)
			{
				//caution: may be null; the piece it will take by moving to the current square
				Piece target = GameScreen.board[e.x][e.y];
				validMoves.add(new AI.Move(considering, target, e.x, e.y));
			}//end for
		}//end for
		
		//if cheat mode is on, the AI cannot take any pieces    #cheat
		if (DetectAction.cheat)
		{
			ArrayList<Move> cheatingMoves = new ArrayList<>();
			
			//filter array to remove moves which take pieces
			for (Move e : validMoves)
			{
				//only allows AI to suggest moves which take pieces for the human player
				if (e.target == null || e.target.player == 2)
				{
					cheatingMoves.add(e);
				}//end if
			}//end for
			validMoves = cheatingMoves;
		}//end if
		
		return validMoves;
	}//end member getAllMoves
 	
	/**
	 * get all moves and sort them in order
	 * @param none
	 * @return Move[] - array of moves sorted from best to worst
	 * @author Charles Lei
	 *    #Charles
	 */
	public Move[] getSortedMoves()
	{
 		//get all the moves, convert from ArrayList to array
 		ArrayList<Move> availableMoves = this.getAllMoves();
 		Move[] sortedMoves = new Move[availableMoves.size()];
 		sortedMoves = availableMoves.toArray(sortedMoves);
		
 		//sort the moves
 		quicksort(sortedMoves, 0, sortedMoves.length - 1);
		
		return sortedMoves;
	}//end member getSortedMoves
	
 	/**
 	 * out of all possible moves, make a move if one if favourable
	 * @param none
	 * @raturn void
	 * @author Charles Lei
	 *    #Charles
 	 */
 	public void makeMove()
 	{ 		
		//get the sorted moves
		Move[] sortedMoves = getSortedMoves();
		
 		//only consider moves which are better than neutral, or exactly neutral if no good moves are available
		int lastGoodMove = binarySearch(sortedMoves, 0);
		if (lastGoodMove == -1)
		{
			lastGoodMove = binarySearch(sortedMoves, 1);
		}//end if
		//if no acceptable moves are found, don't make any move
		if (lastGoodMove == -1)
		{
			return;
		}//end if
		
		//get a move from the range (higher difficulty = better moves)
		int randomSelect = lastGoodMove;
		for (int e = 0; e < difficulty; e++)
		{
			randomSelect = Math.min(randomSelect, (int)(Math.random() * lastGoodMove));
		}//end for
 		Move selectedMove = sortedMoves[randomSelect];
		
		//make the move
		this.selected = selectedMove.consider;
		this.move(selectedMove.targetX, selectedMove.targetY);
 	}//end member makeMove
	
	/**
	 * updates all available moves just like the underlying Player class
	 * also updates the safety array
	 * @param none
	 * @return void
	 * @override
	 * @author Charles Lei
	 *    #Charles
	 */
	public void updateMoves()
	{
		//update the underlying Player (which in turn updates the moves for all the pieces)
		super.updateMoves();
		
		//go through pieces, update the safety array
		for (Piece e : this.controllable)
		{
			//pawn: check diagonals only
			if (e.pieceID == Piece.PAWN)
			{
				if (Coord.inBoard(e.xPos - 1, e.yPos + 1))
					safety[e.xPos - 1][e.yPos + 1]++;
				if (Coord.inBoard(e.xPos + 1, e.yPos + 1))
					safety[e.xPos + 1][e.yPos + 1]++;
			}
			else
			{
				for (Coord mv : e.moves)
					safety[mv.x][mv.y]++;
			}//end if
		}//end for
	}//end member updateMoves
  	
	/**
	 * overload of quicksort for convenient function calling
	 * @param Move[] ll - list to be sorted
	 * @param int lBound - start left bound to be sorted inclusive
	 * @param int rBound - start right bound to be sorted inclusive
	 * @return void
	 * @author Charles Lei
	 *    #Charles
	 *    #static
	 */
	public static void quicksort(Move[] ll, int lBound, int rBound)
	{
		//automatically fill in the buffer with an array of equal size
		quicksort(ll, lBound, rBound, new Move[ll.length]);
	}//end static member quicksort
	
 	/**
 	 * quicksorts an array of Move objects, using the instance compare method
	 * @param Move[] ll - list to be sorted
	 * @param int lBound - start left bound to be sorted inclusive
	 * @param int rBound - start right bound to be sorted inclusive
	 * @param Move[] buffer - convenient buffer to store elements into memory
 	 * ascending order, best moves at the front
	 * @author Charles Lei
	 *    #alg
	 *    #level : algorithm not learned in class
	 *    #Charles
 	 */
  	public static void quicksort(Move[] ll, int lBound, int rBound, Move[] buffer)
  	{
 		//if the length within the bound is already sorted (one element or less), stop
  		if (lBound >= rBound)
		{
			return;
		}//end if
		
		//standard quicksort pivot
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
  			}//end if
  		}//end for
		
 		//put the pivot in the space remaining
  		buffer[lPoint] = pivot;
		
 		//write the buffer
  		for(int e = lBound; e <= rBound; e++)
  		{
  			ll[e] = buffer[e];
  		}//end for
		
		//done; quicksort the two parts beside the pivot
  		quicksort(ll, lBound, lPoint - 1, buffer);
  		quicksort(ll, rPoint + 1, rBound, buffer);
	}//end static member quicksort
	
	/**
	 * binary search an array for an element
	 * returns the last element which is strictly less than the target value (-1 if none exists)
	 * @param Move[] arr - array of moves to search
	 * @param int val - val which, when each Move is projected (Move e -> e.getOptimal()), is used to compare
	 * @author Charles Lei
	 *    #alg
	 *    #Charles
	 */
	public static int binarySearch(Move[] arr, int val)
	{
		//place: keep track of the lowest possible place to be searched
		//jump: keep track of the distance from the lowest place to the highest
		int place = -1, jump = arr.length;
		
		//repeatedly divide jump in half; place never decreases, grows by a factor of log, and never goes past the right place
		while (jump > 0)
		{
			if (place + jump < arr.length && arr[place + jump].getOptimal() < val)
				place += jump;
			
			jump /= 2;
		}//end while
		
		//loop at the end - due to division imprecision, place could be off by a factor of log(N)
		while (place + 1 < arr.length && arr[place + 1].getOptimal() < val)
		{
			place++;
		}//end while
		
		//it works - guaranteed O(log(N)) search time and correct result every time
		return place;
		
		/*
		 brief proof:
		  let N = the length of the list
		  jump starts at N
		  first loop: jump is halved until it is one, guaranteed log(N)
		  second loop: for each iteration of the first loop, the final result could be one off if jump is odd
		    therefore, off by a factor of log(N)
		  so doing log(N) twice is still O(log(N)), but it is usually FASTER than binary search seen in class
		  because it stores fewer variables and does fewer conditionals/arithmetic operations
		  
		  ...and the second loop guarantees that the correct element is always found
		*/
	}//end static member binarySearch
 }//end class AI
 