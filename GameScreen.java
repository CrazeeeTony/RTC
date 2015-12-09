//import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.*;
public class GameScreen extends JFrame{
	public static final int BOARD_W = 8;
	public static final int BOARD_H = 8;
	public static final int SQUARE_SIZE = 50;
	public static final int EDGE_SPACE = 100;
	Timer tm;
	ActionListener al;
	Piece[][] board;
	BoardPanel boardPnl;
	public GameScreen(){
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		boardPnl = new BoardPanel();
		this.add(boardPnl);
		al = new ActionListener(){
			public void actionPerformed(ActionEvent e){
				boardPnl.revalidate();
			}
		};
		tm = new Timer(10, al);
		tm.start();
		board = new Piece[BOARD_W][BOARD_H];
		this.pack();
	}
	
	class BoardPanel extends JPanel{
		public BoardPanel(){
			this.setPreferredSize(new Dimension(SQUARE_SIZE * BOARD_W + 2 * EDGE_SPACE, SQUARE_SIZE * BOARD_H + 2 * EDGE_SPACE));
		}
		public void paintComponent(Graphics g){
			super.paintComponent(g);
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
						g.setColor(Color.white);
					}
					else{
						g.setColor(Color.black);
					}
					g.fillRect(x * SQUARE_SIZE + EDGE_SPACE, y * SQUARE_SIZE + EDGE_SPACE, SQUARE_SIZE, SQUARE_SIZE);
					g.setColor(Color.red);
					g.drawString("{" + x + ", " + y + "}", x * SQUARE_SIZE + EDGE_SPACE + 20, y * SQUARE_SIZE + EDGE_SPACE + 20);
				}
			}
		}
	}
}