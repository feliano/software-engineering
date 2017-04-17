public class Human {

	private	String name;
	private int age;
	// needs to be static to be able to access in constructor
	protected static String[] randomNames = {"Anna","Bosse","Carl","Daniel","Emma",
	"Felix","Gustav","Hanna","Isak","Johanna","Kim","Malin","Nils","Oscar","Patrik"};

	public Human(int ageIn, String nameIn){
		this.age = ageIn;
		this.name = nameIn;
	}

	public Human(){
		// call to other constructor needs to be on first line.
		this(new Double(Math.random()*100).intValue(),randomNames[new Double(Math.random()*100).intValue()%randomNames.length]);
	}

	@Override // will make compiler complain if not overriding
	public String toString(){
		return String.format("%s, %d", name, age);
	}

	public int getAge(){
		return age;
	}

	public String getName(){
		return name;
	}

}
