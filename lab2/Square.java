// Fredrik Eliasson

import javax.swing.*;
import java.awt.Color;

class Square extends JButton {

	private String squareText;
	private int ipos,jpos;

	Square(String value,int i, int j){
		super(value);
		setBackground(new Color(170,170,170));
		setFocusPainted(false); // removes border around text
		squareText = value;
		ipos = i;
		jpos = j;
	}

	public int getIPosition(){
		return ipos;
	}

	public int getJPosition(){
		return jpos;
	}

}