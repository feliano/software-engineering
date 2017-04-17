// Fredrik Eliasson

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

	public Window(){
		super("Fredrik");
		setSize(400,400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = getContentPane();
		c.setBackground(new Color(170,200,200));
		c.setLayout(new FlowLayout());
		c.add(new MyButton(Color.GREEN,Color.RED,"On","Off"));
		c.add(new MyButton(Color.GREEN,Color.RED,"Run","Stop"));
	}
    
}