/**
 * options for the game
 */
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
public class OptionsWindow extends JFrame implements ActionListener
{
	public static final Font TBMS = new Font("Trebuchet MS", Font.PLAIN, 17);
	
	GameScreen creator;
	JPanel everything = new JPanel();
	JLabel message = new JLabel();
	JPanel buttonPanel = new JPanel();
	JButton[] buttons =
	 {
		new JButton("return to game"),
		new JButton("exit to menu"),
		new JButton("exit to desktop")
	 };
	
	public OptionsWindow(GameScreen creator)
	{
		super("Game is paused");
		//JFrame which created this one
		this.creator = creator;
		
		message.setText("Your score is " + creator.human.score);
		message.setFont(TBMS);
		
		for (JButton e : buttons)
		{
			e.setPreferredSize(new Dimension(150, 35));
			e.addActionListener(this);
			buttonPanel.add(e);
		}
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 99999, 5));
		
		everything.add(message);
		everything.add(buttonPanel);
		everything.setLayout(new BoxLayout(everything, BoxLayout.X_AXIS));
		
		changeSize(creator.da.windowX, creator.da.windowY);
		
		this.add(everything);
		this.pack();
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				buttons[0].doClick();
			}
		});
	}
	
	public void actionPerformed(ActionEvent ev)
	{
		this.dispose();
		if (ev.getSource() == buttons[0])
		{
			//restart timer
			creator.tm.start();
		}
		else if (ev.getSource() == buttons[1])
		{
			creator.da.returnToMenu();
		}
		else
		{
			creator.da.quitRequest();
		}
	}
	
	public void changeSize(int newX, int newY)
	{
		everything.setPreferredSize(new Dimension(newX, newY));
		message.setPreferredSize(new Dimension(newX - 300, newY));
		buttonPanel.setPreferredSize(new Dimension(300, newY));
	}
	
	public void windowClosing(WindowEvent e)
	{
		System.out.println("CLOSEING");
		buttons[0].doClick();
	}
}