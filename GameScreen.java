/**
 *
 * @author Tony Li
 */
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.*;
public class GameScreen extends JFrame implements MouseListener, MouseMotionListener{
	
	public static final int BOARD_W = 8;
	public static final int BOARD_H = 8;
	public static final int SQUARE_SIZE = 50;
	public static final int EDGE_SPACE = 75;
	
	DetectAction da;
	
	Timer tm;
	ActionListener al;
	Piece[][] board;
	BoardPanel boardPnl;
	
	JButton btnMenu;
	JButton btnB;
	
	int mouseX = -9999;
	int mouseY = -9999;
	
	public GameScreen(){
		super("RTC");
		try{
			this.setIconImage(ImageIO.read(new File("Piece Images/1Knight.png")));
		}
		catch(Exception e){
		}
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		boardPnl = new BoardPanel();
		boardPnl.addMouseListener(this);
		boardPnl.addMouseMotionListener(this);
		
		Box b1 = Box.createHorizontalBox();
		b1.add(boardPnl);
		
		Box b2 = Box.createHorizontalBox();
		btnMenu = new JButton("Return to Menu");
		btnMenu.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				da.returnToMenu();
			}
		});
		btnB = new JButton("B");
		b2.add(btnMenu);
		b2.add(btnB);
		
		this.add(b1);
		this.add(b2);
		
		al = new ActionListener(){
			public void actionPerformed(ActionEvent e){
				boardPnl.repaint();
				boardPnl.revalidate();
			}
		};
		tm = new Timer(10, al);
		tm.start();
		board = new Piece[BOARD_W][BOARD_H];
		for(int x=0;x<6;x++){
			board[0][x] = new Piece(1,0,x,x);
			board[1][x] = new Piece(2,1,x,x);
		}
		this.pack();
	}
	
	public void mouseClicked(MouseEvent e){
	}

	public void mouseEntered(MouseEvent e){
	}

	public void mouseExited(MouseEvent e){
	}

	public void mousePressed(MouseEvent e){
	}

	public void mouseReleased(MouseEvent e){
	}
	
	public void mouseDragged(MouseEvent e){
		this.mouseX = e.getX();
		this.mouseY = e.getY();
	}

	public void mouseMoved(MouseEvent e){
		this.mouseX = e.getX();
		this.mouseY = e.getY();
	}
	
	public void addDetectAction(DetectAction d){
		this.da = d;
	}
	
	class BoardPanel extends JPanel{
		public BoardPanel(){
			this.setPreferredSize(new Dimension(SQUARE_SIZE * BOARD_W + 2 * EDGE_SPACE, SQUARE_SIZE * BOARD_H + 2 * EDGE_SPACE));
		}
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			//System.out.println("ndafnkjn");
			g.setColor(Color.black);
			for(int x = 0; x < BOARD_H; x++){
				g.drawString(-x + BOARD_H + "", EDGE_SPACE / 2, (int) (SQUARE_SIZE * (x + 0.5)) + EDGE_SPACE);
			}
			for(int x = 0; x < BOARD_W; x++){
				g.drawString((char)(x + 'A') + "", (int) (SQUARE_SIZE * (x + 0.5)) + EDGE_SPACE, EDGE_SPACE / 2);
			}
			for(int x = 0; x < BOARD_W; x++){
				for(int y = 0; y < BOARD_H; y++){
					if((x + y) % 2 == 0){
						g.setColor(new Color(255, 255, 255));
					}
					else{
						g.setColor(new Color(139, 69, 19));
					}
					g.fillRect(x * SQUARE_SIZE + EDGE_SPACE, y * SQUARE_SIZE + EDGE_SPACE, SQUARE_SIZE, SQUARE_SIZE);
					g.setColor(Color.red);
					g.drawString("{" + x + ", " + y + "}", x * SQUARE_SIZE + EDGE_SPACE + 20, y * SQUARE_SIZE + EDGE_SPACE + 20);
				}
			}
			
			for(int x=0;x<BOARD_W;x++){
				for(int y=0;y<BOARD_H;y++){
					if(board[x][y]!=null){
						g.drawImage(board[x][y].img, x * SQUARE_SIZE + EDGE_SPACE, y * SQUARE_SIZE + EDGE_SPACE, SQUARE_SIZE, SQUARE_SIZE, null);
					}
				}
			}
			g.setColor(Color.green);
			//System.out.println(mouseX + " " + mouseY);
			int mouseSqX = (mouseX + SQUARE_SIZE - EDGE_SPACE) / SQUARE_SIZE - 1;
			int mouseSqY = (mouseY + SQUARE_SIZE - EDGE_SPACE) / SQUARE_SIZE - 1;
			//System.out.println(mouseSqX + " " + mouseSqY);
			if(mouseSqX >= 0 && mouseSqX < BOARD_W && mouseSqY >= 0 && mouseSqY < BOARD_H){
				g.drawRect(mouseSqX * SQUARE_SIZE + EDGE_SPACE, mouseSqY * SQUARE_SIZE + EDGE_SPACE, SQUARE_SIZE, SQUARE_SIZE);	
			}
		}
	}
}