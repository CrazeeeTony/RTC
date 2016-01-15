/**
 * Class to handle program flow
 * @author Tony Li
 */
import javax.swing.*;
//interface to handle changing windows and closing
interface DetectAction
{
	void quitRequest();
	void startGameRequest();
	void gameOver();
	void returnToMenu();
}

public class Main
{
	public static void main(String[] args)
	{
		RTC rtc = new RTC();
	}
	
	/**
	 * given a JFrame of some kind, checks for null and closes it
	 */
	public static void terminate(JFrame item)
	{
		if (item != null)
			item.dispose();
	}
	
	//class to handle the menu and game screen
	static class RTC implements DetectAction
	{
		//menu and game screen
		Menu mn;
		GameScreen gs;
		//game over screen
		GameOverScreen gv;
		/**
		* Default constructor:
		* @return - void
		* */
		public RTC(){
			mn = new Menu();
			mn.addDetectAction(this);
			mn.setVisible(true);
		}
		
		/**
		* quits program
		* @return - void
		* */
		public void quitRequest()
		{
			System.exit(0);
		}
		
		/**
		* starts the game after play is clicked
		* @return - void
		* */
		public void startGameRequest()
		{
			terminate(mn);
			terminate(gv);
			gs = new GameScreen();
			gs.addDetectAction(this);
			gs.setVisible(true);
		}
		
		/**
		 * go to game over screen
		 *
		 */
		public void gameOver()
		{
			terminate(mn);
			terminate(gs);
			gv = new GameOverScreen();
			gv.addDetectAction(this);
			gv.setVisible(true);
		}
		
		/**
		* returns to menu
		* @return - void
		* */
		public void returnToMenu()
		{
			terminate(gs);
			terminate(gv);
			mn = new Menu();
			mn.addDetectAction(this);
			mn.setVisible(true);
		}
	}
}//end Main class