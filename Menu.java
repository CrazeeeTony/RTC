/**
 * the main menu
 * @author Charles Lei
 */
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.Scanner;
public class Menu extends JFrame implements ActionListener
{
	//for line breaks in the html formatting used in JComponents
	public static final String LINE_END = "<br>";
	
	DetectAction da;
	//all the graphical components
	JPanel everything = 	new JPanel();
	JPanel information = 	new JPanel();
	JPanel graphicPanel = 	new JPanel();
	JPanel textPanel = 		new JPanel();
	JPanel buttonPanel = 	new JPanel();
	JLabel picture =		new JLabel();
	JLabel title = 			new JLabel();
	JLabel message = 		new JLabel();
	JButton[] buttons = 
	{
		new JButton("play game"),
		new JButton("help"),
		new JButton("highscores"),
		new JButton("settings"),
		new JButton("quit")
	};
	
	/**
	 * set up the menu
	 */
	public Menu()
	{
		super("RTC");
		
		//get the icon for the window bar
		try
		{
			this.setIconImage(ImageIO.read(new File("Piece Images/1Knight.png")));
		}
		catch(Exception e)
		{
			System.out.println("Error: Images could not be opened.");
		}
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		title.setText("Real-- Time-- CHESS!!!1!!1!11one!");
		
		message.setText("This is the menu.");		//FUTURE: figure out a suitable initial message

		//textPanel contains all components in the top right quarter of screen (non-interactive)
		textPanel.add(title);
		textPanel.add(message);
		textPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 99999, 5));
		textPanel.setPreferredSize(new Dimension(300, 200));
		
		for (JButton e : buttons)
		{
			e.addActionListener(this);
			buttonPanel.add(e);
			e.setPreferredSize(new Dimension(150, 35));
		}//end for

		//button panel contains the interactive components (JButtons) in the bottom right
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 99999, 5));
		buttonPanel.setPreferredSize(new Dimension(300, 200));

		//add panels to a background panel for message display and buttons, set layout
		information.add(textPanel);
		information.add(buttonPanel);
		information.setLayout(new BoxLayout(information, BoxLayout.Y_AXIS));
		information.setPreferredSize(new Dimension(300, 400));
		
		//the remaining left half of the screen is a nice looking image
		try
		{
			BufferedImage retrievedPic = ImageIO.read(new File("title_screen.png"));
			picture.setIcon(new ImageIcon(retrievedPic));
			picture.setPreferredSize(new Dimension(300, 400));
			graphicPanel.add(picture);
		}
		catch (IOException e)
		{
			System.out.println("Error: could not load image file.");
		}
		graphicPanel.setPreferredSize(new Dimension(300, 400));
		
		//add both image and information to the background panel, set their layout to be side-by-side
		everything.add(graphicPanel);
		everything.add(information);
		everything.setPreferredSize(new Dimension(600,400));
		everything.setLayout(new BoxLayout(everything, BoxLayout.X_AXIS));
		
		changeSize(1000, 700);
		//set up the frame
		this.add(everything);
		this.pack();
	}//end default constructor ()
	
	/**
	 * handle events from button clicks
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
				da.startGameRequest();
				break;
			case 1:
				//clicked "help"
				message.setText("Click 'play game' to play the game.");
				
				break;
			case 2:
				//clicked "highscores"
				try
				{
					message.setText("<html>highscores" + LINE_END);
					Scanner reading = new Scanner(new File("highscores.txt"));
					
					while (reading.hasNext())
					{
						message.setText(message.getText() + LINE_END + reading.nextLine());
					}
					message.setText(message.getText() + "</html>");
				}
				catch (IOException e)
				{
					System.out.println("Error: could not read the highscores file.");
				}
				break;
			case 3:
				//clicked "settings"
				//da.setupWindow();
				break;
			case 4:
				//clicked "quit"
				da.quitRequest();
				break;
			default:
				//for debugging purposes
		}//end switch
	}//end member actionPerformed
	
	/**
	 * resize method
	 * shifts all components
	 */
	public void changeSize(int newX, int newY)
	{
		everything.setPreferredSize(new Dimension(newX, newY));
		picture.setPreferredSize(new Dimension(newX - 300, newY));
		graphicPanel.setPreferredSize(new Dimension(newX - 300, newY));
		information.setPreferredSize(new Dimension(300, newY));
		textPanel.setPreferredSize(new Dimension(300, newY - 200));
	}
	
	public void validate()           /////////////////////////
	{
		super.validate();
		changeSize(this.getContentPane().getSize().width, this.getContentPane().getSize().height);
	}
	
	/**
	 * 
	 */
	public void addDetectAction(DetectAction d)
	{
		this.da = d;
	}//end member addDetectAction
	
}//end class Menu
