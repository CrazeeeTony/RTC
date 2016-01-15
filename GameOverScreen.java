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
	public static final String LINE_END = "<br><\br>";
	
	DetectAction da;
	JPanel info = 			new JPanel();
	JLabel scoreDisplay = 	new JLabel();
	JLabel message = 		new JLabel();
	JTextField nameField = 	new JTextField();
	JPanel buttonPanel = 	new JPanel();
	JButton submit = 		new JButton("submit score");
	JButton toMenu = 		new JButton("return to menu");
	JPanel everything = 	new JPanel();
	//high score variables
	ArrayList<String> names = new ArrayList<>();
	ArrayList<Double> scores = new ArrayList<>();
	double score;
	
	public GameOverScreen(int windowX, int windowY)
	{
		this(GameScreen.human, GameScreen.comp, windowX, windowY);
	}
	
	public GameOverScreen(Player user, Player computer, int windowX, int windowY)
	{
		//set the message
		super((user.hasLost()) ? "computer wins" : ((computer.hasLost()) ? "user wins" : "stalemate"));
		
		//get the scores from the game details and the file
		this.score = user.score;
		updateScores();
		
		info.add(scoreDisplay);
		
		message.setText("Your final score is " + score + ". Enter your name:");
		info.add(message);
		
		nameField.setPreferredSize(new Dimension(150, 50));
		info.add(nameField);
		
		info.setLayout(new FlowLayout(FlowLayout.CENTER, 99999, 5));
		info.setPreferredSize(new Dimension(300, 400));
		
		submit.setPreferredSize(new Dimension(150, 35));
		submit.addActionListener(this);
		buttonPanel.add(submit);
		
		toMenu.setPreferredSize(new Dimension(150, 35));
		toMenu.addActionListener(this);
		buttonPanel.add(toMenu);
		
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 99999, 5));
		buttonPanel.setPreferredSize(new Dimension(300, 400));
		
		everything.add(info);
		everything.add(buttonPanel);
		everything.setLayout(new BoxLayout(everything, BoxLayout.X_AXIS));
		everything.setPreferredSize(new Dimension(600, 400));
		
		this.changeSize(windowX, windowY);
		
		this.add(everything);
		this.pack();
	}
	
	public void updateScores()
	{
		names.clear();
		scores.clear();
		
		//try to parse the file of highscores
		try
		{
			Scanner reading = new Scanner(new File("highscores.txt"));
			while (reading.hasNext())
			{
				//scan a name & score (double)
				names.add(reading.next());				//ISSUE: does not work if user enters spaces
				scores.add(reading.nextDouble());
				reading.nextLine();
			}
		}
		catch (Exception ex)
		{
			System.out.println("could not open the file or found a name with no score");
		}
		
		//update the text
		String scoreText = "<html>Highscores:" + LINE_END;
		for (int e = 0; e < names.size(); e++)
		{
			scoreText += names.get(e) + " --- " + scores.get(e) + LINE_END;
		}
		scoreDisplay.setText(scoreText + "</html>");
	}
	
	public void actionPerformed(ActionEvent ev)
	{
		if (ev.getSource() == submit)
		{
			if (!nameField.getText().equals(""))
			{
				//insert the new element via insertion sort function
				int insertIndex = names.size();
				names.add("");
				scores.add(0.0);
				while (insertIndex > 0 && scores.get(insertIndex - 1) <= score)
				{
					names.set(insertIndex, names.get(insertIndex - 1));
					scores.set(insertIndex, scores.get(insertIndex - 1));
					insertIndex--;
				}
				names.set(insertIndex, nameField.getText());
				scores.set(insertIndex, this.score);
			}
			else
			{
				//tell the user that a valid name must be entered
				
			}
			//rewrite the file
			try
			{
				PrintWriter rewrite = new PrintWriter("highscores.txt", "UTF-8");
				for (int e = 0; e < names.size(); e++)
				{
					rewrite.println(names.get(e) + " " + scores.get(e));
				}
				rewrite.flush();
				rewrite.close();
			}
			catch (Exception ex)
			{
				
			}
			
			//update the scores
			updateScores();
			//can't submit more than once
			submit.setEnabled(false);
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