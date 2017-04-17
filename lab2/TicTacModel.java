// Fredrik Eliasson

class TicTacModel implements Boardgame {

	private boolean isSelected = false; // is a square selected for move
	private int selectedI, selectedJ;   // index of selected square
	private boolean xToPlay = true; 	// true if X's turn and false if O's turn
	private String currentMessage = "X's turn! Place an X on the board";
	private String[][] status = new String[3][3];
	private boolean isPlacementPhase = true;
	private int squareCount = 0;

	public TicTacModel(){
		for(int i = 0;i<3;i++){
			for(int j = 0;j<3;j++){
				status[i][j] = "";
			}	
		}
	}

	private boolean isEmpty(int i, int j){
		if(status[i][j].equals("")){
			return true;
		}else{
			return false;
		}

	}

	private String moveToString(int iFrom,int jFrom,int iTo,int jTo){
		return Integer.toString(iFrom) + "," + Integer.toString(jFrom) + " to: " +
			   Integer.toString(iTo) + "," + Integer.toString(jTo);
	}


	public boolean move(int i, int j){
		
		if(isPlacementPhase){
			if(xToPlay){
				if(isEmpty(i,j)){
					status[i][j] = "X";
					xToPlay = false;	
					currentMessage = "O's turn! Place an O on the board";
				}else{
					currentMessage = "Please select an empty square to place X";
				}
			}else{
				if(isEmpty(i,j)){
					status[i][j] = "O";
					xToPlay = true;
					currentMessage = "X's turn! Place an X on the board";
					squareCount += 1;
				}else{
					currentMessage = "Please select an empty square to place O";
				}
			}
			if(squareCount == 3){
				isPlacementPhase = false;
				currentMessage = "X's turn! Pick an X to move";
			}
		}else{
			if(xToPlay){
				if(isSelected){
					//check that square to move to is empty
					if(isEmpty(i,j)){
						status[i][j] = status[selectedI][selectedJ];
						status[selectedI][selectedJ] = "";
						isSelected = false;
						xToPlay = false;
						currentMessage = "Successful move: " + moveToString(i,j,selectedI,selectedJ) + ". O's turn";
					}else{
						currentMessage =  "Please select an empty square to move the X to";
					}

				}else{
					// select which one to move
					if(status[i][j].equals("X")){
						isSelected = true;
						selectedI = i;
						selectedJ = j;
						currentMessage = "Selected square: " + i + "," + j;
					}else{
						currentMessage = "X's turn! Pick an X to move";
					}
				}

			}else{
				if(isSelected){
					if(isEmpty(i,j)){
						status[i][j] = status[selectedI][selectedJ];
						status[selectedI][selectedJ] = "";
						isSelected = false;
						xToPlay = true;
						currentMessage = "Successful move: " + moveToString(i,j,selectedI,selectedJ) + ". X's turn";
					}else{
						currentMessage = "Please select an empty square to move the O to";
					}
				}else{
					// select which one to move
					if(status[i][j].equals("O")){
						isSelected = true;
						selectedI = i;
						selectedJ = j;
						currentMessage = "Selected square: " + i + "," + j;
					}else{
						currentMessage = "O's turn! Pick an O to move";
					}
				}
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