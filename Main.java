import javax.swing.*;

interface DetectAction{
	void quitRequest();
	void startGameRequest();
	void returnToMenu();
}

public class Main{
	public static void main(String[] args){
		RTC rtc = new RTC();
	}
	
	static class RTC implements DetectAction{
		Menu mn;
		GameScreen gs;
		public RTC(){
			mn = new Menu();
			mn.addDetectAction(this);
			mn.setVisible(true);
		}
		public void quitRequest(){
			System.exit(0);
		}
		public void startGameRequest(){
			mn.setVisible(false);
			gs = new GameScreen();
			gs.addDetectAction(this);
			gs.setVisible(true);
		}
		public void returnToMenu(){
			gs.setVisible(false);
			gs = null;
			mn.setVisible(true);
		}
	}
}