// Fredrik Eliasson

// NO imports are needed in this class
// FifteenModel doesn't have any graphics !!!

class FifteenModel implements Boardgame {
	
	private String[][] status = new String[4][4]; // Game board
	private int iemp,jemp; // index for empty square
	private String currentMessage = "No message";

	public FifteenModel() {
		// set up board
		for(int i = 0;i<status.length;i++){
			for(int j = 0;j<status.length;j++){
				if(i == status.length-1 && j == status.length-1){
					iemp = i;
					jemp = j;
					status[i][j] = "";
				}else{
					status[i][j] = Integer.toString(i*status.length + j + 1); // adds 1-15 to the squares
				}
			}
		}
		shuffleBoard();
	}

	/** randomly moves the squares around*/
	private void shuffleBoard(){
		int shuffles = 1000;
		for(int i = 0;i<shuffles;i++){
			int x = (int) (Math.random() * 4.0);
			int y = (int) (Math.random() * 4.0);
			move(x,y);
		}
	}

	/** converts a move to a String for printing */
	private String moveToString(int ifrom,int jfrom,int ito, int jto){
		return Integer.toString(ifrom) + "," + Integer.toString(jfrom) + " to: " +
			   Integer.toString(ito) + "," + Integer.toString(jto);
	}

	/** tries to move a square to the empty square,
		returns true if move is valid, false otherwise
	*/
	public boolean move(int i, int j){
		// 	
		int idir = iemp-i;
		int jdir = jemp-j;
		if((Math.abs(idir) + Math.abs(jdir)) == 1){
			//manhattan distance == 1 -> valid move
			status[iemp][jemp] = status[i][j];
			status[i][j] = "";
			currentMessage = "Successful move: " + moveToString(i,j,iemp,jemp);

			iemp = i;
			jemp = j;
			return true;
		}else{
			currentMessage = "Unsuccessful move: " + moveToString(i,j,iemp,jemp);
			return false;
		}
	}  
	
	
	public String getStatus(int i, int j){
		return status[i][j];
	}      

	public String getMessage(){
		return currentMessage;
	}
}