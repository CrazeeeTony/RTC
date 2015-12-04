//import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
public class GameScreen extends JFrame{
	public static final int BOARD_W = 8;
	public static final int BOARD_H = 8;
	public static final int SQUARE_SIZE = 60;
	public static final int EDGE_SPACE = 100;
	Timer tm;
	ActionListener al;
	int[][] board;
	BoardPanel boardPnl;
	public GameScreen(){
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		boardPnl = new BoardPanel();
		this.add(boardPnl);
		al = new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.out.println("dasf");
				boardPnl.revalidate();
			}
		};
		tm = new Timer(10, al);
		tm.start();
		board = new int[BOARD_W][BOARD_H];
		this.pack();
	}
	
	class BoardPanel extends JPanel{
		public BoardPanel(){
			this.setPreferredSize(new Dimension(SQUARE_SIZE * BOARD_W + EDGE_SPACE, SQUARE_SIZE * BOARD_H + EDGE_SPACE));
		}
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			for(int x = 0; x < BOARD_W; x++){
				for(int y = 0; y < BOARD_H; y++){
					if((x + y) % 2 == 0){
						g.setColor(Color.white);
					}
					else{
						g.setColor(Color.black);
					}
					g.fillRect(x * SQUARE_SIZE + EDGE_SPACE / 2, y * SQUARE_SIZE + EDGE_SPACE / 2, SQUARE_SIZE, SQUARE_SIZE);
				}
			}
		}
	}
}