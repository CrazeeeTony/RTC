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
	//all components in the settings part of the menu must be accessed globally
	static JSlider difficulty = 	new JSlider(1, 10, 5);
	static JCheckBox cheat = 		new JCheckBox("cheat mode");
	static JCheckBox startBlack = 	new JCheckBox("start as black");
	static JCheckBox resolution = 	new JCheckBox("compact menu interface");
	JPanel hotkeys = 				new JPanel();
	JTextField[] changeHotkeys = 	new JTextField[8];
	JButton saveHotkeys = 			new JButton("save hotkeys");
	JLabel hotkeyMessage = 			new JLabel("hotkey configuration");
	
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
				da.difficulty = difficulty.getValue();
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
				JPanel threeScores = new JPanel();
				threeScores.setLayout(new FlowLayout());
				JLabel _tmp = new JLabel("by time taken     by pieces lost     by overall score");
				_tmp.setFont(TBMS);
				graphicPanel.add(_tmp);
				graphicPanel.add(threeScores);
				try
				{
					Scanner reading = new Scanner(new File("highscores.txt"));
					String[][] names = {null, null, null};
					double[][] scores = {null, null, null};
					for (int e = 0; e < 3; e++)
					{
						JPanel board = new JPanel();
						board.setLayout(new FlowLayout(FlowLayout.CENTER, 99999, 5));
						int num = Integer.parseInt(reading.nextLine());
						names[e] = new String[num];
						scores[e] = new double[num];
						for (int ee = 0; ee < num; ee++)
						{
							String name = reading.nextLine();
							names[e][ee] = name;
							double score = Double.parseDouble(reading.nextLine());
							scores[e][ee] = score;
							JPanel current = new JPanel();
							JLabel _tmp2 = new JLabel(String.format("%-8s%8.3f", name, score));
							JButton rmv = new JButton("remove");
							final int e_ = e, ee_ = ee;
							rmv.addActionListener(new ActionListener(){
								public void actionPerformed(ActionEvent ev)
								{
									board.remove(current);
									//rewrite the file without the currently indexed score
									try
									{
										PrintWriter rewrite = new PrintWriter("highscores.txt");
										for (int x = 0; x < 3; x++)
										{
											if (x == e_)
												rewrite.println(names[x].length - 1);
											else
												rewrite.println(names[x].length);
											for (int y = 0; y < names[x].length; y++)
											{
												if (!(x == e_ && y == ee_))
												{
													rewrite.println(names[x][y]);
													rewrite.println(scores[x][y]);
												}
											}
										}
										rewrite.flush();
										rewrite.close();
									}
									catch (IOException exc) {}
									board.revalidate();
									board.repaint();
								}
							});
							current.setLayout(new FlowLayout());
							current.add(_tmp2);
							current.add(rmv);
							board.add(current);
						}
						board.setPreferredSize(new Dimension(graphicPanel.getWidth() / 3, 500));
						threeScores.add(board);
					}
				}
				catch (NoSuchElementException ex) {}
				catch (Exception ex)
				{
					System.out.println(ex);
					
					System.out.println("The highscores file may have been modified or corrupted.");
					//reset file
					try
					{
						new PrintWriter("highscores.txt").close();
					}
					catch (IOException exc) {}
					
				}
				//button removes all scores
				JButton rmvAll = new JButton("remove all");
				rmvAll.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ev)
					{
						threeScores.removeAll();
						threeScores.revalidate();
						threeScores.repaint();
						
						//update file
						try
						{
							new PrintWriter("highscores.txt").close();
						}
						catch (IOException ex) {}
					}
				});
				graphicPanel.add(rmvAll);
				graphicPanel.revalidate();
				graphicPanel.repaint();
				break;
			case 3:
				//clicked "settings"
				graphicPanel.removeAll();
				//difficulty, resolution, hotkeys, scoring system, player starts as black all go here
				graphicPanel.add(new JLabel("difficulty"));
				graphicPanel.add(difficulty);
				graphicPanel.add(cheat);
				cheat.addItemListener(new ItemListener(){
					public void itemStateChanged(ItemEvent ev)
					{
						DetectAction.cheat = ev.getStateChange() == 1;
					}
				});
				graphicPanel.add(startBlack);
				startBlack.addItemListener(new ItemListener(){
					public void itemStateChanged(ItemEvent ev)
					{
						da.startBlack = ev.getStateChange() == 1;
					}
				});
				graphicPanel.add(resolution);
				resolution.addItemListener(new ItemListener(){
					public void itemStateChanged(ItemEvent ev)
					{
						da.smallWindow = ev.getStateChange() == 1;
						if (da.smallWindow)
						{
							da.windowX = 850;
							da.windowY = 500;
						}
						else
						{
							da.windowX = 1000;
							da.windowY = 700;
						}
					}
				});
				for (int e = 0; e < 8; e++)
					changeHotkeys[e] = new JTextField(1);
				for (JTextField e : changeHotkeys)
					hotkeys.add(e);
				hotkeys.add(saveHotkeys);
				saveHotkeys.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ev)
					{
						for (int e = 0; e < 8; e++)
						{
							if (changeHotkeys[e].getText().length() != 1)
							{
								hotkeyMessage.setText("invalid configuration");
								return;
							}
							for (int ee = e; ee < 8; ee++)
							{
								if (changeHotkeys[e].getText() == changeHotkeys[ee].getText())
								{
									hotkeyMessage.setText("invalid configuration");
									return;
								}
							}
						}
						try
						{
							PrintWriter wr = new PrintWriter("hotkeys.txt");
							for (JTextField e : changeHotkeys)
							{
								wr.print(e);
							}
							wr.println();
							wr.flush();
							wr.close();
						}
						catch (IOException exc)
						{
							hotkeyMessage.setText("failed to change hotkeys, reset to default");
							
						}
					}
				});
				hotkeys.add(hotkeyMessage);
				graphicPanel.add(hotkeys);
				
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
