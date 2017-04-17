// Fredrik Eliasson

public class Main {
	public static void main(String args[]){
		FifteenModel fifteen = new FifteenModel();
		MemoryModel memory = new MemoryModel(4);
		TicTacModel tictac = new TicTacModel();

		ViewControl vc = new ViewControl(tictac,3);
		ViewControl vc2 = new ViewControl(fifteen,4);
		ViewControl vc3 = new ViewControl(memory,4);
	}
}