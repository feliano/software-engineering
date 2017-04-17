// Fredrik Eliasson

import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.border.Border;
import java.awt.event.*;

public class MyButton extends JButton implements ActionListener {

	private boolean mState = true;
	private String mOnLabel;
	private String mOffLabel;
	private Color mOnColor;
	private Color mOffColor;

	public MyButton(Color c1, Color c2, String s1, String s2){
		super(s1);
		setBounds(150,150,100,100);
		setOpaque(true);
		//setForeground(c2);
		setBackground(c1);
		setFocusPainted(false); // removes border around text
		addActionListener(this);
		mOnLabel = s1;
		mOffLabel = s2;
		mOnColor = c1;
		mOffColor = c2;
	}

	private void toggleState(){
		if(mState){
			setText(mOffLabel);
			setBackground(mOffColor);
			mState = false;
		}else{
			setText(mOnLabel);
			setBackground(mOnColor);
			mState = true;
		}
	}

    @Override
    public void actionPerformed(ActionEvent e) {
        toggleState();
    }
}
