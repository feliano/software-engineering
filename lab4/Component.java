/** Component class */
abstract class Component {

	protected String mName;
	protected double mWeight;

	public Component(){}

	public double getWeight(){
		return mWeight;
	}

	public Component getChild(int index){
		return null;
	}

	@Override
	public String toString(){
		return mName + " " + getWeight();
	}

}