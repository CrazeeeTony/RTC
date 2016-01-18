/**
 * Class to launch program
 * @author Tony Li
 */
//#main
import javax.swing.*;
public class Main
{
	public static void main(String[] args)
	{
		//start the application
		new DetectAction();
	}//end static member main
}//end Main class
	
/**
 * Class to handle the menu and game screens, program flow
 * @author Tony Li
 */
class DetectAction
{
	/****
	 dictionary:
	  mn - menu screen
	  gs - game screen
	  gv - game over/highscore screen
	  difficulty - difficulty level for the AI
	  startBlack - true if the user starts as black
	  smallWindow - true if the window should be compact
	  windowX - size of menu interface's x-dimension
	  windowY - size of menu interface's x-dimension
	  cheat - true if the user has cheat mode enabled    #cheat
	  completeKillMode - true if the game mode requires all pieces to be captured
	  noAnimations - false if animations are on
	****/
	Menu mn;
	GameScreen gs;
	GameOverScreen gv;
	int difficulty = 5;
	boolean startBlack = false;
	boolean smallWindow = true;
	int windowX = 850, windowY = 510;
	static boolean cheat = false;
	static boolean completeKillMode = false;
	static boolean noAnimations = false;
	
	/**
	* Default constructor:
	* @param none
	* @return - void
	* */
	public DetectAction()
	{
		mn = new Menu(windowX, windowY);
		mn.addDetectAction(this);
		Menu.resolution.setSelected(true);
	}//end default constructor ()
		
	/**
	* quits program
	* @param none
	* @return - void
	* */
	public void quitRequest()
	{
		System.exit(0);
	}//end member quitRequest
	
	/**
	* starts the game after play is clicked
	* @param none
	* @return - void
	* */
	public void startGameRequest()
	{
		//close everything else
		terminate(mn);
		terminate(gv);
		
		//new game screen
		gs = new GameScreen(startBlack, difficulty);
		gs.addDetectAction(this);
		gs.setVisible(true);
	}//end member startGameRequest
	
	/**
	 * go to game over screen
	 * @param none
	 * @return void
	 */
	public void gameOver()
	{
		terminate(mn);
		terminate(gs);
		
		//new game over screen
		gv = new GameOverScreen(windowX, windowY);
		gv.addDetectAction(this);
	}//end member gameOver
	
	/**
	* returns to menu
	* @param none
	* @return - void
	* */
	public void returnToMenu()
	{
		terminate(gs);
		terminate(gv);
		
		//new menu
		mn = new Menu(windowX, windowY);
		mn.addDetectAction(this);
	}//end member returnToMenu
	
	/**
	 * given a JFrame of some kind, checks for null and closes it
	 * @param JFrame item - the item to terminate
	 * @return void
	 *    #static
	 */
	public static void terminate(JFrame item)
	{	
		if (item != null)
			item.dispose();
	}//end static member terminate
}//end DetectAction class