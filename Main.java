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
		* */
		public RTC(){
			mn = new Menu();
			mn.addDetectAction(this);
			mn.setVisible(true);
		}
		
		/**
		* 
		* */
		public void quitRequest()
		{
			System.exit(0);
		}
		
		/**
		* 
		* */
		public void startGameRequest()
		{
			mn.setVisible(false);
			gs = new GameScreen();
			gs.addDetectAction(this);
			gs.setVisible(true);
		}
		
		/**
		* 
		* */
		public void returnToMenu()
		{
			gs.setVisible(false);
			gs = null;
			mn.setVisible(true);
		}
	}
}