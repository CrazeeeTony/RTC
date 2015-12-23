/**
 * class for algorithm based decision making
 */
public class AI
{
	//objects used to represent possible moves
	//used to classify moves by how optimal they are
	//also used to store information about moves easily (piece, place, etc.)
	static class Move
	{
		Piece consider;
		Piece target;
		int targetX, targetY;
		public Move(Piece consider, Piece target, int targetX, int targetY)
		{
			this.consider = consider;
			//targeted piece is the piece taken, be careful might be NULL
			this.target = target;
			this.targetX = targetX;
			this.targetY = targetY;
		}
		//gets an integer representing how optimal the move is (lower is better)
		public int getOptimal()
		{
			//TEMP
			return 1;
		}
		
		public boolean compare(Move other)
		{
			//TEMP
			return true;
		}
	}
	//try to create a move object by moving a piece at a certain location to another location
	//if no piece exists or the piece cannot move to the targeted location then returns NULL
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
	}
	
	public static void quicksort(Move[] ll, int lBound, int rBound)
	{
		if (lBound >= rBound) return;

		Move[] buffer = new Move[ll.length];
		Move pivot = ll[lBound];
		int lPoint = lBound, rPoint = rBound;
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
		buffer[lPoint] = pivot;
		for (int e = lBound; e <= rBound; e++)
		{
			ll[e] = buffer[e];
		}
		quicksort(ll, lBound, lPoint - 1);
		quicksort(ll, rPoint + 1, rBound);
	}
}
