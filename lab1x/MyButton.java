// Fredrik Eliasson

import javax.swing.*;
import java.awt.Color;
import javax.swing.border.Border;

public class MyButtonX extends JButton {

	private boolean mState = true;
	private String mOnLabel;
	private String mOffLabel;
	private Color mOnColor;
	private Color mOffColor;

	public MyButtonX(Color c1, Color c2, String s1, String s2){
		super(s1);
		setBounds(150,150,100,100);
		setOpaque(true);
		//setForeground(c2);
		setBackground(c1);
		setFocusPainted(false); // removes border around text
		mOnLabel = s1;
		mOffLabel = s2;
		mOnColor = c1;
		mOffColor = c2;
	}

	public void toggleState(){
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
}
