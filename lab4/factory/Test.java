import human.*;

class Test {

	public static void main(String[] args){	

		Human anna = Human.create("Anna","112233-3728");
		Human burt = Human.create("Burt","112233-3738");
		System.out.println(anna);
		System.out.println(burt);

		// These calls shouldn't compile
		//Human anna2 = new Woman("Anna","112233-3728");
		//Human burt2 = new Man("Burt","112233-3738");
		//Human h = new Human(){};

	}
}