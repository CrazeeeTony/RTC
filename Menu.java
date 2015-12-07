/**
 *
 * @author Charles Lei
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class Menu extends JFrame implements ActionListener
{
	JPanel everything = new JPanel();
	JPanel graphicPanel = new JPanel();
	JPanel buttonPanel = new JPanel();
	JLabel title = new JLabel();
	JLabel message = new JLabel();
	JButton[] buttons = 
	 {
		new JButton("play game"),
		new JButton("help"),
		new JButton("highscores"),
		new JButton("quit")
	 };
	/**
	 *
	 */
	public Menu()
	{
		super("RTC");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		title.setText("Real-- Time-- CHESS!!!1!!1!11one!");
		
		message.setText("This is the menu.");		//FUTURE: figure out a suitable initial message

		//graphicPanel contains all components in the top half of screen (non-interactive)
		graphicPanel.add(title);
		graphicPanel.add(message);
		graphicPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 99999, 5));
		
		for (JButton e : buttons)
		{
			e.addActionListener(this);
			buttonPanel.add(e);
		}//end for

		//button panel contains the interactive components (JButtons)
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 99999, 5));

		//add panels to background panel, set layout
		everything.add(graphicPanel);
		everything.add(buttonPanel);
		everything.setLayout(new BoxLayout(everything, BoxLayout.Y_AXIS));
		
		//set up the frame
		this.add(everything);
		this.setVisible(true);
		this.setSize(new Dimension(600,400));		//FUTURE: decide on more permanent dimensions
	}//end default constructor ()
	
	/**
	 *
	 */
	public void actionPerformed(ActionEvent ev)
	{
		//get the index of the button which was clicked, sequential search
		int clickedIndex = -1;
		for (int e = 0; e < buttons.length; e++)
		{
			if (buttons[e] == ev.getSource())
				clickedIndex = e;
		}//end for
		
		//match it with one of the following cases
		switch (clickedIndex)
		{
			case 0:
				//clicked "play game"

				break;
			case 1:
				//clicked "help"

				break;
			case 2:
				//clicked "highscores"

				break;
			case 3:
				//clicked "quit"
				this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));

				break;
			default:
				//for debugging purposes
		}//end switch
	}//end member actionPerformed

	public static void main(String[] args)		//can test menu from here, but delete this for final version
	{
		new Menu();
	}
}//end class Menu
