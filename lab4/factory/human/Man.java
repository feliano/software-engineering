package human;

class Man extends Human{

	public Man(String name, String pnr){
		mPnr = pnr;
		mName = name;
	}

	@Override
	public String toString(){
		return "My name is " + mName + " and I'm a Man with personal number: " + mPnr;
	}


}