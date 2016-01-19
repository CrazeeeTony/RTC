/**
 * Simple class to store an xy coordinate pair
 * @author Tony Li
 * #Tony
 */
public class Coord
{
	int x;
	int y;
	
	/**
	* Full constructor: 
	* Constructs a JFrame and adds a menu to it along with listeners for buttons
	* @param int xPos - the x coordinate
	* @param int yPos - the y coordinate
	* */
	public Coord(int xPos, int yPos)
	{
		x = xPos;
		y = yPos;
	}//end (int, int) constructor
	
	/**
	* Checks a given x,y pair to see if it's inside the board
	* @param int xPos - the x coordinate
	* @param int yPos - the y coordinate
	* @return boolean - whether the given x,y coordinate is in the board
	* */
	public static boolean inBoard(int x, int y)
	{
		return x >= 0 && x < GameScreen.BOARD_W && y >= 0 && y < GameScreen.BOARD_H;
	}//end inBoard method
}//end Coord class