// Fredrik Eliasson
package human;

class Woman extends Human{

	public Woman(String name, String pnr){
		mPnr = pnr;
		mName = name;
	}

	@Override
	public String toString(){
		return "My name is " + mName + " and I'm a Woman with personal number: " + mPnr;
	}

}