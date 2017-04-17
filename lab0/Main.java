public class Main {

	public static void main(String []args){
		Human[] randomHumans = new Human[15];
		
		// part 3 - random humans
		System.out.println("Random Humans:");
		for(int i = 0; i<randomHumans.length; ++i){
			randomHumans[i] = new Human();
			System.out.println(randomHumans[i]);
		}

		try{
			Student putte = new Student(20,"Putte",2011);
			System.out.println(putte);			
		} catch(Exception e){
			System.out.println(e);
		}


		Student[] randomStudents = new Student[15];
		// part4.4 random students
		System.out.println("\nRandom Students:");
		int numStudents = 0;
		while(numStudents < randomStudents.length) {
			try{
				randomStudents[numStudents] = new Student();
				numStudents++;
			}catch(Exception e){
				System.out.println(e);
			}
		}

		for(int i = 0; i<randomStudents.length; i++){
			System.out.println(randomStudents[i]);
		}

		// part 4.5 random mix
		Human[] randomMix = new Human[10];
		for(int i = 0; i<5; ++i){
			randomMix[i] = new Human();
		}

		System.out.println("\nCreating Random Students:");
		numStudents = 0;
		while(numStudents < 5) {
			try{
				randomMix[numStudents+5] = new Student();
				numStudents++;
			}catch(Exception e){
				System.out.println(e);
			}
		}

		System.out.println("5 Random Humans followed by 5 random students:");
		for(int i = 0;i<randomMix.length;i++){
			System.out.println(randomMix[i]);
		}

	}
}