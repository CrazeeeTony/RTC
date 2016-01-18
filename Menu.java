/**
 * the main menu
 * has a title screen, help for the user, several configuration options, highscore management, and a quit button
 * game is initially launched from the menu, and the menu launches the main game window
 * @author Charles Lei
 * #Charles
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
	
	//for the main font of the program
	public static final Font TBMS = new Font("Trebuchet MS", Font.PLAIN, 17);
	
	/****
	 dictionary:
	  da - object used to manage all windows of the application
	  everything - background for all graphical components
	  information - background for buttons and message to the user
	  graphicPanel - background for a panel which shows a picture or settings, depending on user's choice
	  picture - main title picture
	  textPanel - panel containing text for the user
	  buttonPanel - panel containing buttons for interaction with the menu
	  message - contains a message telling the user how to use the menu
	  buttons - contains all buttons for interaction
	  
	  the following components are only visible when the settings part of the menu is accessed
	  difficulty - slider allowing user to set difficulty of the AI
	  cheat - check this to enable cheat mode                      #cheat
	  startBlack - check this to start as black
	  completeKill - check this to keep playing until one side has no pieces left
	  resolution - check this to make windows smaller
	  hotkeys - contains all hotkey configuration
	  changeHotkeys - array of text fields, which contain new proposed settings for which buttons should be hotkeys
	  saveHotkeys - button to save the configuration entered in the text fields
	  hotkeyMessage - contains messages about updating hotkey config
	****/
	DetectAction da;
	
	//all graphical components
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
	
	//all components in the settings part of the menu
	static JSlider difficulty = 	new JSlider(1, 10, 5);
	//#cheat
	static JCheckBox cheat = 		new JCheckBox("cheat mode (also toggles AI assist)");
	static JCheckBox startBlack = 	new JCheckBox("start as black");
	static JCheckBox completeKill = new JCheckBox("complete kill mode: a side wins when it controls all pieces on the board");
	static JCheckBox resolution = 	new JCheckBox("compact menu interface");
	static JCheckBox noAnimations = new JCheckBox("turn animations off");
	JPanel hotkeys = 				new JPanel();
	JTextField[] changeHotkeys = 	new JTextField[8];
	JButton saveHotkeys = 			new JButton("save hotkeys");
	JLabel hotkeyMessage = 			new JLabel("hotkey configuration");
	
	/**
	 * set up the menu
	 * @param windowX - initial width of the window
	 * @param windowY - initial height of the window
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
			System.out.println("Error: Could not load icon file.");
		}
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//a message to the user with instructions
		String messageText = "Real Time Chess." + LINE_END;
		messageText += "This is the menu." + LINE_END;
		messageText += "Click 'play game' to start a new game." + LINE_END;
		messageText += "If this is your first time playing, you should click 'help' instead." + LINE_END;
		messageText += "You can also click 'highscores' to view and manage highscores." + LINE_END;
		messageText += "The 'settings' button contains many other options." + LINE_END;
		messageText += "Exit this application using the 'quit' button.";
		
		//put the instructions inside the massage label, with font and formatting
		message.setText(String.format("<html><div WIDTH=300>%s</div></html>", messageText));
		message.setFont(TBMS);

		//textPanel contains all components in the top right quarter of screen (non-interactive)
		textPanel.add(message);
		textPanel.setLayout(new FlowLayout());
		
		//loop through all buttons by setting action listener, adding it to the panel, and setting size
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
			picture = new JLabel(new ImageIcon(retrievedPic));
			graphicPanel.add(picture);
		}
		catch (IOException e)
		{
			System.out.println("Error: could not load image file.");
		}
		
		//set its layout to stack components vertically
		graphicPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 99999, 5));
		
		//add both image and information to the background panel, set their layout to be side-by-side
		everything.add(graphicPanel);
		everything.add(information);
		everything.setLayout(new BoxLayout(everything, BoxLayout.X_AXIS));
		
		//call the size setting function
		this.changeSize(windowX, windowY);
		
		//set up the frame
		this.add(everything);
		this.pack();
		this.setVisible(true);
	}//end constructor (int, $
	
	//#action
	/**
	 * handle events from button clicks
	 * @param ActionEvent ev - event to handle
	 * @return void
	 * @override
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
				//set difficulty, tell game manager to launch the game
				da.difficulty = difficulty.getValue();
				da.startGameRequest();
				break;
				
			case 1:
				//clicked "help"
				//remove all components from left half of the screen
				graphicPanel.removeAll();
				
				//replace them with a picture detailing how to play the game
				try
				{
					//read the picture
					BufferedImage instPic = ImageIO.read(new File("instructions.png"));
					JLabel inst = new JLabel(new ImageIcon(instPic));
					graphicPanel.add(inst);
				}
				catch (IOException ex)
				{
					System.out.println("Could not read the instruction image file.");
				}//end try/catch
				
				//display the left side of the screen by revalidating and repainting
				graphicPanel.revalidate();
				graphicPanel.repaint();
				break;
				
			case 2:
				//clicked "highscores"
				//#level : removing individual scores is possible, which is quite difficult
				//remove everything from left of screen
				graphicPanel.removeAll();
				
				//three categories of score will be contained here
				JPanel threeScores = new JPanel();
				threeScores.setLayout(new FlowLayout());
				JLabel categDescription = new JLabel("by time taken            by pieces lost           by overall score");
				categDescription.setFont(TBMS);
				graphicPanel.add(categDescription);
				graphicPanel.add(threeScores);
				
				//now, try opening the score file and displaying the scores
				try
				{
					//scan the file
					Scanner reading = new Scanner(new File("highscores.txt"));
					
					//2D arrays for names and scores contain three lists of names corresponding with three for scores
					String[][] names = {null, null, null};
					double[][] scores = {null, null, null};
					
					//loop three times for each category; the file has been written in a predetermined order
					for (int e = 0; e < 3; e++)
					{
						//create a new scoreboard panel to contain each category
						JPanel board = new JPanel();
						board.setLayout(new FlowLayout(FlowLayout.CENTER, 99999, 5));
						
						//the first line of each of three blocks in the file contains N:
						//the number of scores recorded in this block, and 2*N is the number of lines containing the data
						int num = Integer.parseInt(reading.nextLine());
						names[e] = new String[num];
						scores[e] = new double[num];
						
						//loop N times to read the data
						for (int ee = 0; ee < num; ee++)
						{
							//read two lines
							String name = reading.nextLine();
							names[e][ee] = name;
							double score = Double.parseDouble(reading.nextLine());
							scores[e][ee] = score;
							
							//new panel for every individual score, contains details and a remove button
							JPanel current = new JPanel();
							JLabel dsc = new JLabel(String.format("%-8s%8.0f", name, score));
							JButton rmv = new JButton("remove");
							
							//save immutable versions of the current loop indexes to use in lambda closure
							final int e_ = e, ee_ = ee;
							rmv.addActionListener(new ActionListener(){
								//called when the remove button for this particular score is clicked
								public void actionPerformed(ActionEvent ev)
								{
									board.remove(current);
									
									//rewrite the file without the currently indexed score
									//#save
									try
									{
										PrintWriter rewrite = new PrintWriter("highscores.txt");
										
										for (int x = 0; x < 3; x++)
										{
											//if it is the current category, there is one less score
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
												}//end if
											}//end for
										}//end for
										rewrite.flush();
										rewrite.close();
										
										//and reset the entire scoreboard
										buttons[2].doClick();
									}
									catch (IOException exc) {}//end try/catch
									
									//redraw board
									board.revalidate();
									board.repaint();
								}//end anonymous member actionPerformed
							});
							
							current.setLayout(new FlowLayout(FlowLayout.TRAILING));
							current.add(dsc);
							current.add(rmv);
							board.add(current);
						}//end for
						
						board.setPreferredSize(new Dimension(graphicPanel.getWidth() / 3, 350));
						threeScores.add(board);
					}//end for
				}
				//empty file triggers no action
				catch (NoSuchElementException ex) {}
				//other exceptions prompt a message and a reset of the file
				catch (Exception ex)
				{
					System.out.println(ex);
					
					System.out.println("The highscores file may have been modified or corrupted.");
					//reset file
					try
					{
						new PrintWriter("highscores.txt").close();
					}
					catch (IOException exc) {}//end try/catch
				}//end try/catch
				
				//this button removes all scores
				JButton rmvAll = new JButton("remove all");
				rmvAll.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ev)
					{
						//delete contents visually
						threeScores.removeAll();
						threeScores.revalidate();
						threeScores.repaint();
						
						//delete data in the file
						try
						{
							new PrintWriter("highscores.txt").close();
						}
						catch (IOException ex) {}
					}
				});
				graphicPanel.add(rmvAll);
				
				//redraw the graphic panel (left of the screen)
				graphicPanel.revalidate();
				graphicPanel.repaint();
				break;
				
			case 3:
				//clicked "settings"
				graphicPanel.removeAll();
				hotkeys.removeAll();
				
				//difficulty, resolution, hotkeys, cheat mode, player starts as black all go here
				//difficulty
				graphicPanel.add(new JLabel("difficulty"));
				
				//tick spacing
				difficulty.setMinorTickSpacing(1);
				difficulty.setPaintTicks(true);
				
				//labels on the ends
				Hashtable labels = new Hashtable();
				labels.put(new Integer(1), new JLabel("easy"));
				labels.put(new Integer(10), new JLabel("hard"));
				difficulty.setLabelTable(labels);
				difficulty.setPaintLabels(true);
				
				graphicPanel.add(difficulty);
				
				//cheating 		#cheat
				graphicPanel.add(cheat);
				cheat.addItemListener(new ItemListener()
				{
					public void itemStateChanged(ItemEvent ev)
					{
						DetectAction.cheat = ev.getStateChange() == 1;
					}//end anonymous member itemStateChanged
				});
				
				//start as black
				graphicPanel.add(startBlack);
				startBlack.addItemListener(new ItemListener()
				{
					public void itemStateChanged(ItemEvent ev)
					{
						da.startBlack = ev.getStateChange() == 1;
					}//end anonymous member itemStateChanged
				});
				
				//complete kill mode
				graphicPanel.add(completeKill);
				completeKill.addItemListener(new ItemListener()
				{
					public void itemStateChanged(ItemEvent ev)
					{
						da.completeKillMode = ev.getStateChange() == 1;
					}//end anonymous member itemStateChanged
				});
				
				//change window size
				graphicPanel.add(resolution);
				resolution.addItemListener(new ItemListener()
				{
					public void itemStateChanged(ItemEvent ev)
					{
						da.smallWindow = ev.getStateChange() == 1;
						
						//two window presets
						if (da.smallWindow)
						{
							da.windowX = 850;
							da.windowY = 510;
						}
						else
						{
							da.windowX = 1000;
							da.windowY = 700;
						}//end if
					}//end anonymous member itemStateChanged
				});
				
				//animations off
				graphicPanel.add(noAnimations);
				noAnimations.addItemListener(new ItemListener(){
					public void itemStateChanged(ItemEvent ev)
					{
						da.noAnimations = ev.getStateChange() == 1;
					}//end anonymous member itemStateChanged
				});
				
				//hotkeys
				
				//the text fields first
				//array of text descriptors for each keybinding
				String[] desc =
				{
					"pause",
					"select pawn",
					"select bishop",
					"select knight",
					"select rook",
					"select queen",
					"select king",
					"take available piece"
				};
				
				//read the hotkeys
				try
				{
					Scanner reading = new Scanner(new File("hotkeys.txt"));
					char[] setting = reading.nextLine().toCharArray();
					
					//set up the text fields with the current configuration
					for (int e = 0; e < 8; e++)
					{
						changeHotkeys[e] = new JTextField(1);
						changeHotkeys[e].setText("" + setting[e]);
						
						//each hotkey gets its own panel
						JPanel thisKey = new JPanel();
						thisKey.add(new JLabel(desc[e]));
						thisKey.add(changeHotkeys[e]);
						thisKey.setLayout(new FlowLayout(FlowLayout.TRAILING));
						hotkeys.add(thisKey);
					}//end for
				}
				catch (Exception ex)
				{
					hotkeyMessage.setText("failed to read hotkeys, reset to default");
							
					//reset to default
					try
					{
						PrintWriter wr = new PrintWriter(new FileOutputStream(new File("hotkeys.txt")), true);
						wr.println(" asdfwet");
						wr.flush();
						wr.close();
					}
					catch (IOException exc) {} //end try/catch
					buttons[3].doClick();
				}//end try/catch
				
				//save button
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
							}//end if
							for (int ee = e; ee < 8; ee++)
							{
								if (changeHotkeys[e].getText() == changeHotkeys[ee].getText())
								{
									hotkeyMessage.setText("invalid configuration");
									return;
								}//end if
							}//end for
						}//end for
						
						//rewrite the file
						//#save
						try
						{
							PrintWriter wr = new PrintWriter("hotkeys.txt");
							for (JTextField e : changeHotkeys)
							{
								//the file is just a line of characters
								wr.print(e.getText());
							}//end for
							wr.println();
							wr.flush();
							wr.close();
							
							hotkeyMessage.setText("saved");
						}
						catch (IOException ex)
						{
							hotkeyMessage.setText("failed to change hotkeys, reset to default");
							
							//reset to default
							try
							{
								PrintWriter wr = new PrintWriter("hotkeys.txt");
								wr.println(" asdfwet");
								wr.flush();
								wr.close();
							}
							catch (IOException exc) {} //end try/catch
							buttons[3].doClick();
						}//end try/catch
					}//end anonymous member itemStateChanged
				});
				hotkeys.add(hotkeyMessage);
				
				//stack components vertically
				hotkeys.setLayout(new BoxLayout(hotkeys, BoxLayout.Y_AXIS));
				
				graphicPanel.add(hotkeys);
				
				//redraw graphic panel
				graphicPanel.revalidate();
				graphicPanel.repaint();
				break;
				
			case 4:
				//clicked "quit"
				da.quitRequest();
				break;
				
			default:
				//should be unused
				
		}//end switch
	}//end member actionPerformed
	
	/**
	 * resize method, shifts all components to maintain good ratios
	 * @param int newX - new width of window
	 * @param int newY - new height of window
	 * @return void
	 *    #level : resizeable interface with detail in the form of buttons, labels, etc.
	 */
	public void changeSize(int newX, int newY)
	{
		//set preferred size for all important panels
		everything.setPreferredSize(new Dimension(newX, newY));
		picture.setPreferredSize(new Dimension(newX - 300, newY));
		graphicPanel.setPreferredSize(new Dimension(newX - 300, newY));
		information.setPreferredSize(new Dimension(300, newY));
		textPanel.setPreferredSize(new Dimension(300, newY / 2));
		buttonPanel.setPreferredSize(new Dimension(300, newY / 2));
		
		this.setResizable(false);
	}//end member changeSize
	
	/**
	 * adds a reference to the main game window manager
	 * @param DetectAction d - the manager
	 * @return void
	 */
	public void addDetectAction(DetectAction d)
	{
		this.da = d;
	}//end member addDetectAction
	
}//end class Menu
