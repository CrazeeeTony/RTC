/**
 * Simple class to store an xy coordinate
 * @author Tony Li
 */
public class Coord
{
	int x;
	int y;
	public Coord(int xPos, int yPos)
	{
		x = xPos;
		y = yPos;
	}
	public static boolean inBoard(int x, int y)
	{
		return x >= 0 && x < GameScreen.BOARD_W && y >= 0 && y < GameScreen.BOARD_H;
	}
}