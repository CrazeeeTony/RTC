/**
 * Simple class to store an xy coordinate
 * @author Tony Li
 */
public class Coord
{
	int x;
	int y;
	public Coord(int xPos, int yPos) throws CoordException
	{
		x = xPos;
		y = yPos;
		//throw exception if the coordinates are out of bounds
		if(x < 0 || x >= GameScreen.BOARD_W || y < 0 || y >= GameScreen.BOARD_H){
			throw new CoordException(x, y);
		}
	}
}

//exception for out of board bounds
class CoordException extends Exception
{
	public CoordException(int x, int y)
	{
		super("Invalid Coordinate: " + x + " " + y);
	}
}