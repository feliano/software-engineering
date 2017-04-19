package human;

public abstract class Human {

	protected String mPnr;
	protected String mName;

	Human(){} // default access, accessible only within package human

	// Creates a man or woman object depending on second last digit in personal number 
	public static Human create(String name, String pnr){
		
		// in this lab it's okay to assume that input is correct format
		int genderDigit = Integer.parseInt(pnr.split("-")[1].split("")[2]);
		if(genderDigit%2 == 0){
			return new Woman(name,pnr);
		}else{
			return new Man(name,pnr);
		}
	}
}
