/**
 * options for the game
 */
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
public class OptionsWindow extends JFrame implements ActionListener
{
	GameScreen creator;
	JPanel everything = new JPanel();
	JLabel message = new JLabel();
	JButton backToGame = new JButton("return to game");
	JButton exitToMenu = new JButton("exit to menu");
	JButton exitAll = new JButton("exit to desktop");
	
	public OptionsWindow(GameScreen creator)
	{
		super("Game is paused");
		//JFrame which created this one
		this.creator = creator;
		
		message.setText("Your score is " + creator.human.score);
		everything.add(message);
		
		backToGame.addActionListener(this);
		everything.add(backToGame);
		exitToMenu.addActionListener(this);
		everything.add(exitToMenu);
		exitAll.addActionListener(this);
		everything.add(exitAll);
		
		everything.setLayout(new BoxLayout(everything, BoxLayout.Y_AXIS));
		
		this.add(everything);
		this.pack();
		this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent ev)
	{
		this.dispose();
		if (ev.getSource() == backToGame)
		{
			//restart timer
			creator.tm.start();
		}
		else if (ev.getSource() == exitToMenu)
		{
			creator.da.returnToMenu();
		}
		else
		{
			creator.da.quitRequest();
		}
	}
}