import javax.swing.*;

interface DetectAction{
	void quitRequest();
	void startGameRequest();
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
			gs.setVisible(true);
		}
	}
}