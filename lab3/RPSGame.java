import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.*;

class RPSGame extends JFrame {
    private Gameboard mUserBoard, mCPUBoard; // cpu = computer played unit
    private int mCount; // counts 1st shake,2nd shake, Show hands!
    private JButton mCloseBtn;
    private HashMap<String,String> mWinnerMap = new HashMap<String,String>(); // <loser,winner>
    private Client mClient;

    RPSGame () {

     	setDefaultCloseOperation(EXIT_ON_CLOSE);
		mClient = Client.getInstance();
		mCloseBtn = new JButton("Close");
		mCloseBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				setVisible(false);
				dispose(); // removes the JFrame object
			}
		});

		mWinnerMap.put("STEN","PASE"); // paper beats rock
		mWinnerMap.put("PASE","SAX"); // scissor beats paper
		mWinnerMap.put("SAX","STEN"); // rock beats scissor

		mCPUBoard = new Gameboard("CPU");
		mUserBoard = new Gameboard("Me",new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mCount++;
				mUserBoard.resetColor();
				JButton btn = (JButton) e.getSource();
				mUserBoard.markPlayed(btn);
				String userMove = btn.getActionCommand();
				if(mCount == 1){
					mCPUBoard.resetColor();
					mUserBoard.setUpper(userMove);
					mUserBoard.setLower("ONE...");
					mCPUBoard.setLower("ONE...");
				}else if(mCount == 2){
					mUserBoard.setUpper(userMove);
					mUserBoard.setLower("TWO...");
					mCPUBoard.setLower("TWO...");
				}else{
					mCount = 0;
					mUserBoard.setUpper(userMove);
					String computerMove = mClient.getRPSPick();
					
					mCPUBoard.markPlayed(computerMove);

					if(computerMove.equals(userMove)){
						mUserBoard.setLower("TIE!");
						mCPUBoard.setLower("TIE!");
					}else if(computerMove.equals(mWinnerMap.get(userMove))){
						mUserBoard.setLower("LOSE!");						
						mCPUBoard.setLower("WIN!");
						mCPUBoard.wins();
					}else{
						mUserBoard.setLower("WIN!");						
						mUserBoard.wins();
						mCPUBoard.setLower("LOSE!");
					}
				}
			}
		});

		JPanel boards = new JPanel();
		boards.setLayout(new GridLayout(1,2));
		boards.add(mUserBoard);
		boards.add(mCPUBoard);
		add(mClient,BorderLayout.NORTH);
		add(boards, BorderLayout.CENTER);
		add(mCloseBtn, BorderLayout.SOUTH);
		
		setSize(300, 700);
		setVisible(true);
    }

    public static void main (String[] args) {
		new RPSGame();
    }
}


