/**
 *
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
		if(x < 0 || x >= GameScreen.BOARD_W || y < 0 || y >= GameScreen.BOARD_H){
			throw new CoordException(x, y);
		}
	}
}

class CoordException extends Exception
{
	public CoordException(int x, int y)
	{
		super("Invalid Coordinate: " + x + " " + y);
	}
}