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
	void returnToMenu();
}

public class Main
{
	public static void main(String[] args)
	{
		RTC rtc = new RTC();
	}
	
	//class to handle the menu and game screen
	static class RTC implements DetectAction
	{
		//menu and game screen
		Menu mn;
		GameScreen gs;
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
			mn.setVisible(false);
			gs = new GameScreen();
			gs.addDetectAction(this);
			gs.setVisible(true);
		}
		
		/**
		* returns to menu
		* @return - void
		* */
		public void returnToMenu()
		{
			gs.setVisible(false);
			gs = null;
			mn.setVisible(true);
		}
	}
}//end Main class