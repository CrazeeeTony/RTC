/**
 * options for the game when it is paused
 * @author Charles Lei
 */
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
public class OptionsWindow extends JFrame implements ActionListener
{
	//font used in the application
	public static final Font TBMS = new Font("Trebuchet MS", Font.PLAIN, 17);
	
	/****
	 dictionary:
	  creator - parent screen
	  message - updates the user
	  buttonPanel - contains all buttons and the message
	  buttons - stores all buttons for navigating
	  snapshot - stores the current view of the screen
	****/
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
	JPanel snapshot = new JPanel();
	
	/**
	 * creates a new options window given the game screen which created it
	 * @param GameScreen creator - the game which was paused to create this menu
	 */
	public OptionsWindow(GameScreen creator)
	{
		super("Game is paused");
		
		this.creator = creator;
		
		//tell the user the scores currently (html used to word wrap)
		
		message.setText(
			String.format("<html><div WIDTH=300>Your scores: time - %.3f, losses - %.3f, overall - %.3f</div></html>",
			creator.human.timeTaken, creator.human.piecesLost, creator.human.score )
			);
			
		message.setFont(TBMS);
		buttonPanel.add(message);
		
		//set size, etc. for all buttons
		for (JButton e : buttons)
		{
			e.setPreferredSize(new Dimension(150, 35));
			e.addActionListener(this);
			buttonPanel.add(e);
		}
		
		//stack the buttons vertically
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 99999, 5));
		
		//the snapshot takes a picture of the screen, so set its layout to an 8x8 grid
		snapshot.setLayout(new GridLayout(8, 8));
		
		//and add each piece's picture one by one
		for (int e = 0; e < 8; e++)
		{
			for (int ee = 0; ee < 8; ee++)
			{
				JLabel thisSquare;
				//if a piece exists here, add its image
				//note that columns are traversed outside of rows, due to the order that GridLayout adds stuff
				if (GameScreen.board[ee][e] != null)
					thisSquare = new JLabel(new ImageIcon(GameScreen.board[ee][e].img));
				else
					thisSquare = new JLabel();
				
				//regardless, draw a border around the square
				thisSquare.setBorder(BorderFactory.createLineBorder(Color.black));
				snapshot.add(thisSquare);
			}
		}
		
		//put everything on the background panel
		everything.add(snapshot);
		everything.add(buttonPanel);
		everything.setLayout(new BoxLayout(everything, BoxLayout.X_AXIS));
		
		//set the size to what the window manager has saved
		changeSize(creator.da.windowX, creator.da.windowY);
		
		//finish setting up the JFrame
		this.add(everything);
		this.pack();
		this.setVisible(true);
		
		//when the window is closed, assume a resume game event
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				buttons[0].doClick();
			}//end anonymous member windowClosing
		});
	}//end constructor (GameScreen)
	
	/**
	 * when a button is clicked
	 * @param ActionEvent ev - the event
	 * @return void
	 * @override
	 */
	public void actionPerformed(ActionEvent ev)
	{
		//close the window regardless
		this.dispose();
		
		if (ev.getSource() == buttons[0])
		{
			//restart timer, resume game
			creator.tm.start();
		}
		else if (ev.getSource() == buttons[1])
		{
			//back to menu
			creator.da.returnToMenu();
		}
		else
		{
			//quit everything
			creator.da.quitRequest();
		}//end if
	}//end member actionPerformed
	
	/**
	 * resize all relevant panels
	 * @param int newX - new width
	 * @param int newY - new height
	 * @return void
	 */
	public void changeSize(int newX, int newY)
	{
		everything.setPreferredSize(new Dimension(newX, newY));
		snapshot.setPreferredSize(new Dimension(newX - 300, newY));
		buttonPanel.setPreferredSize(new Dimension(300, newY));
		
		this.setResizable(false);
	}//end member changeSize
	
}//end class OptionsWindow