import javax.swing.JFrame;


class Window extends JFrame{


	public Window(){
		setVisible(true);
		setSize(200,200);

		add(new WebReader());

	}



	public static void main(String[] args){
		System.out.println("TJENA");
	
		new Window();
	}


}