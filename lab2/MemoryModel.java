// Fredrik Eliasson
// mock-up of memory model

public class MemoryModel implements Boardgame {

	private int mockNumber = 1; // number used to match pairs
	private int iSelected,jSelected; // index of first selected square
	private boolean squareSelected = false;
	private String[][] status;
	private String currentMessage = "memorized message";

	public MemoryModel(int pairs) {
		status = new String[pairs][pairs];
		for(int i = 0;i<pairs;i++){
			for(int j = 0;j<pairs;j++){
				status[i][j] = "???";	
			}
		}
	}

	private void shuffle(){
		// shuffle board
	}

	public boolean move(int i, int j){
		// simple mocked_up gameplay
		if(squareSelected){
			if(status[i][j].equals("???")){
				status[i][j] = Integer.toString(mockNumber);
				squareSelected = false;
				mockNumber++;				
			}
		}else{
			if(status[i][j].equals("???")){
				status[i][j] = Integer.toString(mockNumber);
				squareSelected = true;
			}
		}
		return true;
	}

	public String getStatus(int i, int j){
		return status[i][j];
	}

	public String getMessage(){
		return currentMessage;
	}
}