// Fredrik Eliasson
package human;

class NonBinary extends Human{

	public NonBinary(String name, String pnr){
		mPnr = pnr;
		mName = name;
	}

	@Override
	public String toString(){
		return "My name is " + mName + " and I'm a Non-Binary with personal number: " + mPnr;
	}

}