// Fredrik Eliasson

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class ViewControl extends JFrame implements ActionListener {

    private Boardgame game;
    private int size;
    private Square[][] board;			// Square is a subclass of JButton
    private JLabel mess = new JLabel();

    ViewControl (Boardgame gm, int n){
		super("Board game");
		setSize(640,480);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		game = gm;
		board = new Square[n][n];
		size = n;
		
		Container c = getContentPane();
		c.setBackground(new Color(170,200,200));
		c.setLayout(new BorderLayout());
		mess.setText(gm.getMessage());
		c.add(mess,BorderLayout.NORTH);

		// set up grid of buttons
		JPanel squarePanel = new JPanel();
		squarePanel.setLayout(new GridLayout(n,n));
		for(int i = 0;i<n;i++){
			for(int j = 0;j<n;j++){
				Square s = new Square(gm.getStatus(i,j),i,j);
				board[i][j] = s;
				s.addActionListener(this);
				squarePanel.add(s);
			}
		}
		c.add(squarePanel,BorderLayout.CENTER); // add buttons to window
    }

    /**updates graphics*/
    private void updateUI(){
    	mess.setText(game.getMessage());
		for(int i = 0;i<size;i++){
			for(int j = 0;j<size;j++){
				board[i][j].setText(game.getStatus(i,j));
			}
		}
    }

    @Override
    public void actionPerformed(ActionEvent e){
    	Square s = (Square) e.getSource();
    	game.move(s.getIPosition(),s.getJPosition());
    	updateUI();
    }
}