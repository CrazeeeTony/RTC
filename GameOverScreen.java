import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
/**
 * pops up when game is over, highscore interface
 * @author Charles Lei
 */
public class GameOverScreen extends JFrame implements ActionListener
{
	//for line breaks in the html formatting used in JComponents
	public static final String LINE_END = "<br>";
	
	//for the main font of the program
	public static final Font TBMS = new Font("Trebuchet MS", Font.PLAIN, 17);
	
	/****
	 dictionary:
	  da - game window handler
	  info - panel containing highscore details
	  scoreSummary - information about scores
	  scorePanel - background for score details
	  scoreDisplay - contains score categories
	  message - tells user score
	  nameField - for the user to enter a name
	  buttonPanel - background for buttons
	  submit - submit score
	  toMenu - return to menu
	  everything - background panel
	  names - three lists of names for three categories of score
	  scores - three lists of scores for three categories
	  score - the scores of the user (three of them)
	****/
	DetectAction da;
	JPanel info = 			new JPanel();
	JLabel scoreSummary = 	new JLabel();
	JPanel scorePanel = 	new JPanel();
	JLabel scoreDisplay[] =
	 {
		new JLabel(), new JLabel(), new JLabel()
	 };
	JLabel userScore = 		new JLabel();
	JLabel message = 		new JLabel();
	JTextField nameField = 	new JTextField();
	JPanel buttonPanel = 	new JPanel();
	JButton submit = 		new JButton("submit score");
	JButton toMenu = 		new JButton("return to menu");
	JPanel everything = 	new JPanel();
	ArrayList<ArrayList<String>> names = new ArrayList<>();
	ArrayList<ArrayList<Double>> scores = new ArrayList<>();
	double[] score = new double[3];
	
	/**
	 * construct the screen with set x/y
	 * @param int windowX - width of window
	 * @param int windowY - height of window
	 */
	public GameOverScreen(int windowX, int windowY)
	{
		this(GameScreen.human, GameScreen.comp, windowX, windowY);
	}//end constructor (int, int)
	
	/**
	 * construct with x/y and two players
	 * @param Player user - the user
	 * @param Player computer - the AI
	 * @param int windowX - width of window
	 * @param int windowY - height of window
	 */
	public GameOverScreen(Player user, Player computer, int windowX, int windowY)
	{
		//set the message (states who won)
		super((user.hasLost()) ? "computer wins" : ((computer.hasLost()) ? "user wins" : "stalemate"));
		
		//initialize score variables
		names.add(new ArrayList<>());
		names.add(new ArrayList<>());
		names.add(new ArrayList<>());
		scores.add(new ArrayList<>());
		scores.add(new ArrayList<>());
		scores.add(new ArrayList<>());
		
		//get the scores from the game details
		score[0] = user.timeTaken;
		score[1] = user.piecesLost;
		score[2] = user.score;
		
		//this pulls past scores from file
		updateScores();
		
		//the three categories are written here in different colours
		scoreSummary.setText(
			"<html><font color=\"red\">time taken</font>....." +
			"<font color=\"blue\">pieces lost</font>....." +
			"<font color=\"green\">overall score</font></html>"
			);
		scoreSummary.setFont(TBMS);
		info.add(scoreSummary);
		
		//each label handles one category, with its corresponding colour
		scoreDisplay[0].setForeground(Color.red);
		scoreDisplay[1].setForeground(Color.blue);
		scoreDisplay[2].setForeground(Color.green);
		for (JLabel e : scoreDisplay)
		{
			scorePanel.add(e);
		}
		
		//add all labels to the score panel side by side
		scorePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 3));
		info.add(scorePanel);
		
		//tell the player the scores for the current game
		userScore.setText(
			String.format("Time: %.0f. Piece value lost: %.0f. Final score: %.0f.", score[0] * 10, score[1], score[2] * 10)
			);
		userScore.setFont(TBMS);
		info.add(userScore);
		
		//give the user instructions (the game must be won to submit score)
		if (computer.hasLost())
		{
			message.setText("Enter an 8 character name below to submit your scores.");
		}
		else
		{
			message.setText("You must win the game to submit your score.");
			submit.setEnabled(false);
		}
		message.setFont(TBMS);
		info.add(message);
		
		//set the size for the text field where the user enters a name
		nameField.setPreferredSize(new Dimension(150, 50));
		nameField.setFont(TBMS);
		info.add(nameField);
		
		//vertically stack everything in a flow layout
		info.setLayout(new FlowLayout(FlowLayout.CENTER, 99999, 5));
		
		//do the two buttons
		submit.setPreferredSize(new Dimension(150, 35));
		submit.addActionListener(this);
		buttonPanel.add(submit);
		toMenu.setPreferredSize(new Dimension(150, 35));
		toMenu.addActionListener(this);
		buttonPanel.add(toMenu);
		
		//buttons are stacked vertically too
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 99999, 5));
		
		//finish setting up the frame
		everything.add(info);
		everything.add(buttonPanel);
		everything.setLayout(new BoxLayout(everything, BoxLayout.X_AXIS));
		
		this.changeSize(windowX, windowY);
		
		this.add(everything);
		this.pack();
		this.setVisible(true);
	}//end constructor (Player, Player, int, int)
	
	/**
	 * open the file with highscores and try to parse it, then display the info
	 * @param none
	 * @return void
	 */
	public void updateScores()
	{
		//clear the current scores
		for (ArrayList<String> e : names)
			e.clear();
		for (ArrayList<Double> e : scores)
			e.clear();
		
		//try to parse the file of highscores
		try
		{
			Scanner reading = new Scanner(new File("highscores.txt"));
			
			//file comes in three blocks
			for (int e = 0; e < 3; e++)
			{
				//each block starts with an integer N: the number of scores
				int num = Integer.parseInt(reading.nextLine());
				
				//next, 2*N lines where each pair contains a name and then a score
				for (int ee = 0; ee < num; ee++)
				{
					names.get(e).add(reading.nextLine());
					scores.get(e).add(Double.parseDouble(reading.nextLine()));
				}//end for
			}//end for
		}
		//empty file requires no action
		catch (NoSuchElementException ex) {}
		//other exceptions will reset the file
		catch (Exception ex)
		{
			System.out.println(ex);
			
			//empty the file if it could not be parsed
			System.out.println("The highscore data has been modified or corrupted. All scores have been reset.");
			try
			{
				new PrintWriter("highscores.txt").close();
			}
			catch (IOException exc) {}//end try/catch
		}//end try/catch
		
		//update all the text fields
		for (int ee = 0; ee < 3; ee++)
		{
			String scoreText = "<html>";
			for (int e = 0; e < names.get(ee).size(); e++)
			{
				//display the name, then the value
				scoreText += String.format("%-8s .....%8.3f", names.get(ee).get(e), scores.get(ee).get(e)) + LINE_END;
			}
			scoreDisplay[ee].setText(scoreText + "</html>");
		}//end for
	}//end member updateScores
	
	/**
	 * when a button is pressed
	 * @param ActionEvent ev - event which triggered it
	 * @return void
	 * @override
	 */
	public void actionPerformed(ActionEvent ev)
	{
		//for hitting the submit button, update the display and the file, then block future submissions
		if (ev.getSource() == submit)
		{
			//name cannot be empty, and it cannot be more than 8 characters
			if (!nameField.getText().equals("") && nameField.getText().length() <= 8)
			{
				//get the new name
				String enteredName = nameField.getText();
				
				//insert the new element to all score lists by position for each score
				for (int e = 0; e < 3; e++)
				{
					int place = 0;
					while (place < names.get(e).size() && scores.get(e).get(place) < score[e])
						place++;
					names.get(e).add(place, enteredName);
					scores.get(e).add(place, score[e]);
				}//end for
				
				//rewrite the file
				try
				{
					PrintWriter rewrite = new PrintWriter("highscores.txt");
					for (int e = 0; e < 3; e++)
					{
						//write the number of scores
						rewrite.println(names.get(e).size());
						
						//write each score
						for (int ee = 0; ee < names.get(e).size(); ee++)
						{
							rewrite.println(names.get(e).get(ee));
							rewrite.println(scores.get(e).get(ee));
						}//end for
					}//end for
					rewrite.flush();
					rewrite.close();
				}
				catch (IOException ex) {}//end try/catch
			
				//update the scores
				updateScores();
				message.setText("successfully submitted");
				
				//can't submit more than once
				submit.setEnabled(false);
			}
			else
			{
				//tell the user that a valid name must be entered
				message.setText("That name is invalid.");
			}//end if
		}
		//clicked "return to menu": back to menu
		else
		{
			da.returnToMenu();
		}//end if
	}//end member actionPerformed
	
	/**
	 * set the size for all relevant panels
	 * @param int newX - new width
	 * @param int newY - new height
	 * @return void
	 */
	public void changeSize(int newX, int newY)
	{
		everything.setPreferredSize(new Dimension(newX, newY));
		info.setPreferredSize(new Dimension(newX - 300, newY));
		buttonPanel.setPreferredSize(new Dimension(300, newY));
	}//end member changeSize
	
	/**
	 * add a window manager
	 * @param DetectAction da - the manager
	 * @return void
	 */
	public void addDetectAction(DetectAction da)
	{
		this.da = da;
	}//end member addDetectAction
	
}//end class GameOverScreen