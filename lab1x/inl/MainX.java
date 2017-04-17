// Fredrik Eliasson

public class MainX {

	public static void main(String []args){
		WindowX w = new WindowX();
		w.setVisible(true);
		try{

			int numButtons = Integer.parseInt(args[0]);
			if(args.length == 1){
				for(int i = 0;i<numButtons;i++){
					w.addButton("on","off");
				}
			}else{
				for(int i = 0;i<numButtons;i++){
					w.addButton(args[(i*2)+1],args[(i*2)+2]); // create buttons with strings	
				}
			}
		}catch(Exception e){
			System.out.println("Exception thrown: " + e);
		}
	}
}