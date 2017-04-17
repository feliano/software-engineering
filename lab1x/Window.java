// Fredrik Eliasson

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;

public class WindowX extends JFrame implements ActionListener {

	private Container mContainer;
	private ArrayList<MyButtonX> mButtons;

	public WindowX(){
		super("Fredrik");
		setSize(400,400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mContainer = getContentPane();
		mContainer.setBackground(new Color(170,200,200));
		mContainer.setLayout(new FlowLayout());
		mButtons = new ArrayList<MyButtonX>();
	}

	public void addButton(String s1, String s2){
		MyButtonX b = new MyButtonX(Color.GREEN,Color.RED,s1,s2); 
		mContainer.add(b);
		b.addActionListener(this);
		mButtons.add(b);
	}

    @Override
    public void actionPerformed(ActionEvent e) {
    	Iterator<MyButtonX> it = mButtons.iterator();
    	while(it.hasNext()){
    		MyButtonX b = it.next();
    		if(b != e.getSource()){ // check if button is not pressed
    			b.toggleState();
    		}
    	}
    }
}
