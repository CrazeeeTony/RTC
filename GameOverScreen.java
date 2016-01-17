import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
/**
 * pops up when game is over
 */
public class GameOverScreen extends JFrame implements ActionListener
{
	public static final String LINE_END = "<br>";
	public static final Font TBMS = new Font("Trebuchet MS", Font.PLAIN, 17);
	
	DetectAction da;
	JPanel info = 			new JPanel();
	JLabel scoreSummary = 	new JLabel();
	JPanel scorePanel = 	new JPanel();
	JLabel scoreDisplay[] =
	 {
		new JLabel(), new JLabel(), new JLabel()
	 };
	JLabel message = 		new JLabel();
	JTextField nameField = 	new JTextField();
	JPanel buttonPanel = 	new JPanel();
	JButton submit = 		new JButton("submit score");
	JButton toMenu = 		new JButton("return to menu");
	JPanel everything = 	new JPanel();
	//high score variables
	ArrayList<ArrayList<String>> names = new ArrayList<>();
	ArrayList<ArrayList<Double>> scores = new ArrayList<>();
	double[] score = new double[3];
	
	public GameOverScreen(int windowX, int windowY)
	{
		this(GameScreen.human, GameScreen.comp, windowX, windowY);
	}
	
	public GameOverScreen(Player user, Player computer, int windowX, int windowY)
	{
		//set the message
		super((user.hasLost()) ? "computer wins" : ((computer.hasLost()) ? "user wins" : "stalemate"));
		
		names.add(new ArrayList<>());
		names.add(new ArrayList<>());
		names.add(new ArrayList<>());
		scores.add(new ArrayList<>());
		scores.add(new ArrayList<>());
		scores.add(new ArrayList<>());
		//get the scores from the game details and the file
		score[0] = user.timeTaken;
		score[1] = user.piecesLost;
		score[2] = user.score;
		updateScores();
		
		scoreSummary.setText(
			"<html><font color=\"red\">time taken   </font>" +
			"<font color=\"blue\">pieces lost   </font>" +
			"<font color=\"green\">overall score</font></html>"
			);
		scoreSummary.setFont(TBMS);
		info.add(scoreSummary);
		
		scoreDisplay[0].setForeground(Color.red);
		scoreDisplay[1].setForeground(Color.blue);
		scoreDisplay[2].setForeground(Color.green);
		for (JLabel e : scoreDisplay)
		{
			scorePanel.add(e);
		}
		
		scorePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 3));
		info.add(scorePanel);
		
		message.setText("Time: " + score[0] + "  Pieces lost: " + score[1] + "   Final score: " + score[2]);
		message.setFont(TBMS);
		info.add(message);
		
		nameField.setPreferredSize(new Dimension(150, 50));
		info.add(nameField);
		
		info.setLayout(new FlowLayout(FlowLayout.CENTER, 99999, 5));
		
		submit.setPreferredSize(new Dimension(150, 35));
		submit.addActionListener(this);
		buttonPanel.add(submit);
		
		toMenu.setPreferredSize(new Dimension(150, 35));
		toMenu.addActionListener(this);
		buttonPanel.add(toMenu);
		
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 99999, 5));
		
		everything.add(info);
		everything.add(buttonPanel);
		everything.setLayout(new BoxLayout(everything, BoxLayout.X_AXIS));
		
		this.changeSize(windowX, windowY);
		
		this.add(everything);
		this.pack();
	}
	
	public void updateScores()
	{
		for (ArrayList<String> e : names)
			e.clear();
		for (ArrayList<Double> e : scores)
			e.clear();
		
		//try to parse the file of highscores
		try
		{
			Scanner reading = new Scanner(new File("highscores.txt"));
			for (int e = 0; e < 3; e++)
			{
				int num = Integer.parseInt(reading.nextLine());
				for (int ee = 0; ee < num; ee++)
				{
					names.get(e).add(reading.nextLine());
					scores.get(e).add(Double.parseDouble(reading.nextLine()));
				}
			}
		}
		catch (NoSuchElementException ex) {}
		catch (Exception ex)
		{
			System.out.println(ex);
			
			//empty the file if it could not be parsed
			System.out.println("The highscore data has been modified or corrupted. All scores have been reset.");
			try
			{
				new PrintWriter("highscores.txt").close();
			}
			catch (IOException exc) {}
			
		}
		
		//update all the text fields
		for (int ee = 0; ee < 3; ee++)
		{
			String scoreText = "<html>";
			for (int e = 0; e < names.get(ee).size(); e++)
			{
				scoreText += String.format("%-8s .....%8.3f", names.get(ee).get(e), scores.get(ee).get(e)) + LINE_END;
			}
			scoreDisplay[ee].setText(scoreText + "</html>");
			System.out.println(scoreText + ee);
		}
	}
	
	public void actionPerformed(ActionEvent ev)
	{
		if (ev.getSource() == submit)
		{
			if (!nameField.getText().equals("") && nameField.getText().length() <= 10)
			{
				String enteredName = nameField.getText();
				//insert the new element to all score lists
				for (int e = 0; e < 3; e++)
				{
					int place = 0;
					while (place < names.get(e).size() && scores.get(e).get(place) < score[e])
						place++;
					names.get(e).add(place, enteredName);
					scores.get(e).add(place, score[e]);
				}
				//rewrite the file
				try
				{
					PrintWriter rewrite = new PrintWriter("highscores.txt");
					for (int e = 0; e < 3; e++)
					{
						rewrite.println(names.get(e).size());
						for (int ee = 0; ee < names.get(e).size(); ee++)
						{
							rewrite.println(names.get(e).get(ee));
							rewrite.println(scores.get(e).get(ee));
						}
					}
					rewrite.flush();
					rewrite.close();
				}
				catch (IOException ex) {}
			
				//update the scores
				updateScores();
				//can't submit more than once
				submit.setEnabled(false);
			}
			else
			{
				//tell the user that a valid name must be entered
				
			}
		}
		else
		{
			//back to menu
			da.returnToMenu();
		}
	}
	
	public void changeSize(int newX, int newY)
	{
		everything.setPreferredSize(new Dimension(newX, newY));
		info.setPreferredSize(new Dimension(newX - 300, newY));
		buttonPanel.setPreferredSize(new Dimension(300, newY));
	}
	
	public void addDetectAction(DetectAction da)
	{
		this.da = da;
	}
}