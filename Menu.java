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
import java.util.*;
public class Menu extends JFrame implements ActionListener
{
	//for line breaks in the html formatting used in JComponents
	public static final String LINE_END = "<br>";
	public static final Font TBMS = new Font("Trebuchet MS", Font.PLAIN, 17);
	
	DetectAction da;
	//all the graphical components
	JPanel everything = 	new JPanel();
	JPanel information = 	new JPanel();
	JPanel graphicPanel = 	new JPanel();
	JLabel picture = 		new JLabel();
	JPanel textPanel = 		new JPanel();
	JPanel buttonPanel = 	new JPanel();
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
	public Menu(int windowX, int windowY)
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
		
		String messageText = "Real Time Chess." + LINE_END;
		messageText += "This is the menu." + LINE_END;
		messageText += "Click 'play game' to start a new game." + LINE_END;
		messageText += "If this is your first time playing, you should click 'help' instead." + LINE_END;
		messageText += "You can also click 'highscores' to view and manage highscores." + LINE_END;
		messageText += "The 'settings' button contains many other options." + LINE_END;
		messageText += "Exit this application using the 'quit' button.";
		message.setText(String.format("<html><div WIDTH=300>%s</div></html>", messageText));
		message.setFont(TBMS);

		//textPanel contains all components in the top right quarter of screen (non-interactive)
		textPanel.add(message);
		textPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 99999, 5));
		
		for (JButton e : buttons)
		{
			e.addActionListener(this);
			buttonPanel.add(e);
			e.setPreferredSize(new Dimension(150, 35));
		}//end for

		//button panel contains the interactive components (JButtons) in the bottom right
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 99999, 5));

		//add panels to a background panel for message display and buttons, set layout
		information.add(textPanel);
		information.add(buttonPanel);
		information.setLayout(new BoxLayout(information, BoxLayout.Y_AXIS));
		
		//the remaining left half of the screen is a nice looking image (and changes based on button clicks)
		try
		{
			BufferedImage retrievedPic = ImageIO.read(new File("title_screen.png"));
			picture.setIcon(new ImageIcon(retrievedPic));
			graphicPanel.add(picture);
		}
		catch (IOException e)
		{
			System.out.println("Error: could not load image file.");
		}
		graphicPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 99999, 5));
		
		//add both image and information to the background panel, set their layout to be side-by-side
		everything.add(graphicPanel);
		everything.add(information);
		everything.setLayout(new BoxLayout(everything, BoxLayout.X_AXIS));
		
		this.changeSize(windowX, windowY);
		
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
				graphicPanel.removeAll();
				JLabel inst = new JLabel("instructions go here");
				inst.setFont(TBMS);
				graphicPanel.add(inst);
				graphicPanel.revalidate();
				graphicPanel.repaint();
				break;
			case 2:
				//clicked "highscores"
				graphicPanel.removeAll();
				JLabel tmp_ = new JLabel("Highscores:");
				tmp_.setFont(TBMS);
				graphicPanel.add(tmp_);
				//to store the highscores read from file form
				ArrayList<String> names = new ArrayList<>();
				ArrayList<Double> scores = new ArrayList<>();
				try
				{
					Scanner reading = new Scanner(new File("highscores.txt"));
					for (int e = 0; reading.hasNext(); e++)
					{
						JPanel current = new JPanel();
						current.setLayout(new FlowLayout());
						String name = reading.nextLine();
						double score = reading.nextDouble();
						names.add(name);
						scores.add(score);
						JLabel txt_ = new JLabel(String.format("%d --- %-10s --- %10.3f  ", e, name, score));
						txt_.setFont(TBMS);
						current.add(txt_);
						reading.nextLine();
						JButton rmv_ = new JButton("remove");
						rmv_.addActionListener(new ActionListener(){
								public void actionPerformed(ActionEvent ev)
								{
									graphicPanel.remove(rmv_.getParent());
									graphicPanel.revalidate();
									graphicPanel.repaint();
									
									//update the file with scores
									names.remove(name);
									scores.remove(score);
									try
									{
										PrintWriter rewrite = new PrintWriter("highscores.txt");
										for (int e = 0; e < names.size(); e++)
											rewrite.println(names.get(e) + scores.get(e));
										rewrite.flush();
										rewrite.close();
									}
									catch (IOException ex)
									{
										
									}
								}
							});
						current.add(rmv_);
						graphicPanel.add(current);
					}
				}
				catch (IOException e)
				{
					System.out.println("Error: could not read the highscores file.");
				}
				//button removes all scores
				JButton rmvAll = new JButton("remove all");
				rmvAll.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent ev)
						{
							graphicPanel.removeAll();
							graphicPanel.add(tmp_);
							graphicPanel.add(rmvAll);
							graphicPanel.revalidate();
							graphicPanel.repaint();
							
							//update file
							scores.clear();
							names.clear();
							try
							{
								(new PrintWriter("highscores.txt")).close();
							}
							catch (IOException ex)
							{
								
							}
						}
					});
				graphicPanel.add(rmvAll);
				graphicPanel.revalidate();
				graphicPanel.repaint();
				break;
			case 3:
				//clicked "settings"
				graphicPanel.removeAll();
				graphicPanel.add(new JButton("click me"));
				graphicPanel.revalidate();
				graphicPanel.repaint();
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
		textPanel.setPreferredSize(new Dimension(300, newY / 2));
		buttonPanel.setPreferredSize(new Dimension(300, newY / 2));
	}
	
	/**
	 * 
	 */
	public void addDetectAction(DetectAction d)
	{
		this.da = d;
	}//end member addDetectAction
	
}//end class Menu
