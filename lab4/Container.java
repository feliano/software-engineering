import java.util.ArrayList;

/**composite class*/
class Container extends Component{

	ArrayList<Component> children = new ArrayList<Component>();

	public Container(String name,double weight){
		mName = name;
		mWeight = weight;
	}

	public void add(Component c){
		children.add(c);
	}

	public void remove(Component c){
		children.remove(c);
	}

	public Component getChild(int index){
		Component child = null;
		try{
			child = children.get(index);
		}catch(Exception e){
			System.out.println("Error while getting child: " + e);
		}
		return child;
	}

	@Override
	public String toString(){
		String output = mName + " " + getWeight() + "\n";
		for(Component item : children){
			output += item + "\n";
		}
		return output;
	}

	public double getWeight(){
		double totalWeight = mWeight;
		for(Component item : children){
			totalWeight += item.getWeight(); 
		}
		return totalWeight;
	}

	public static void main(String[] args){

		// Packing the suitcase
		Container suitcase = new Container("Suitcase",2.0);
		suitcase.add(new Item("Socks",0.2));
		suitcase.add(new Item("Shoes",0.3));
		suitcase.add(new Item("Pants",0.3));
		Item shirt = new Item("Shirt",0.15);
		suitcase.add(shirt);

		// packing the necessaire
		Container necessaire = new Container("Necessaire",0.25);
		necessaire.add(new Item("Toothbrush",0.05));
		necessaire.add(new Item("Toothpaste",0.15));
		necessaire.add(new Item("Soap",0.125));

		// putting the necessaire in the suitcase
		suitcase.add(necessaire);
		
		// packing a shavekit
		Container shavekit = new Container("Shavekit",0.18);
		shavekit.add(new Item("Razor",0.1));
		shavekit.add(new Item("Shaving Foam",0.2));
		necessaire.add(shavekit);

		System.out.println(suitcase);
		System.out.println(necessaire);


		Component child = suitcase.getChild(4);
		System.out.println(child);

		// removing the shave kit and the shirt
		suitcase.remove(shavekit);

		System.out.println(suitcase);

	}
}