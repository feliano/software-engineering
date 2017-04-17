
public class Student extends Human {

	private int year;

	public Student (int ageIn, String nameIn, int yearIn) throws Exception{
		super(ageIn,nameIn);
		year = yearIn;

		// 
		if(year < 1934){
			throw new Exception("can't start before 1934");
		} else if(year > 2016){
			throw new Exception("can't start after 2016");
		}

		if(this.getAge()-(2016-year) < 15){
			throw new Exception("too young to start studying");
		}
	}

	public Student () throws Exception{
		this(new Double(Math.random()*100).intValue(),randomNames[new Double(Math.random()*100).intValue()%randomNames.length], new Double(Math.random()*82).intValue() + 1934);
	}


	@Override
	public String toString(){
		return String.format("%s, %d",super.toString(), year);
	}

	public int getYear(){
		return year;
	}

}