import java.util.ArrayList;
import java.util.Iterator;
import java.util.ArrayDeque;
import java.util.Deque;


/**composite class*/
class Container extends Component implements Iterable<Component> {

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

	@Override
	public Component getChild(int index){
		Component child = null;
		try{
			child = children.get(index);
		}catch(Exception e){
			//System.out.println("Error while getting child: " + e);
			return null;
		}
		return child;
	}

	@Override
	public String toString(){
		String output = mName + " " + getWeight();
		for(Component item : children){
			output += "\n" + item;
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

	private Container getOuterObject(){
		return this;
	}

	private class CompositeIterator implements Iterator<Component>{

		private ArrayList<Component> nodes = new ArrayList<Component>(); // used for BFS
		private Deque<Component> stack = new ArrayDeque<Component>(); // used for DFS
		private int index = 0;
		private boolean useBFS = false;

		public CompositeIterator(){
			// set up iterator nodes
			/* BFS */
			if(useBFS){
				nodes.add(getOuterObject());
				for(Component c : children){
					nodes.add(c);
				}				
			}else{
				/* DFS */
				stack.push(getOuterObject());				
			}


		}

	    public Component next(){

	    	/* BFS */
	    	if(useBFS){
			    System.out.println(nodes.get(index).mName);		    
				if(index != 0){
					Component current = nodes.get(index);
					// try to add children of current node
				    int i = 0;
				    Component c = current.getChild(i++);
				    while(c != null){
				    	nodes.add(c);
				    	c = current.getChild(i++);
				    }				
				}

			    return nodes.get(index++);	    		
	    	}else{

		    	/* DFS */
		    	if(stack.isEmpty()) return null;
		    	Component curr = stack.pop();
		    	Component child = curr.getChild(0);
				// try to add children of current node
			    int i = 0;
			    Component c = curr.getChild(i++);
			    while(c != null){
			    	stack.push(c);
			    	c = curr.getChild(i++);
			    }	
			    System.out.println(curr.mName);		    

			    return curr;
	    		
	    	}

	    }   
	    
	    public boolean hasNext(){
		    // returns true if more components to visit
	    	if(useBFS){

		    	/*BFS*/
		    	if(index < nodes.size()){
			    	return true;    		
		    	}else{
		    		return false;
		    	}

	    	}else{

		    	/*DFS*/
		    	if(stack.isEmpty()){
			    	return false;    		
		    	}else{
		    		return true;
		    	}	    		
	    	}

			
	    }

	    public void remove(){
	        // not required for lab
	    }
	}

	@Override
	public CompositeIterator iterator(){
		return new CompositeIterator();
	}

	public static void main(String[] args){

		// Packing the suitcase
		Container suitcase = new Container("Suitcase",2.0);
		suitcase.add(new Item("Socks",0.2));
		suitcase.add(new Item("Shoes",0.3));
		Item shirt = new Item("Shirt",0.15);
		suitcase.add(shirt);

		// packing the necessaire
		Container necessaire = new Container("Necessaire",0.25);
		necessaire.add(new Item("Toothbrush",0.05));
		necessaire.add(new Item("Toothpaste",0.15));
		necessaire.add(new Item("Soap",0.125));

		// putting the necessaire in the suitcase
		suitcase.add(necessaire);
		suitcase.add(new Item("Pants",0.3));

		// packing a shavekit
		Container shavekit = new Container("Shavekit",0.18);
		shavekit.add(new Item("Razor",0.1));
		shavekit.add(new Item("Shaving Foam",0.2));
		necessaire.add(shavekit);

		Component child = suitcase.getChild(4);
		//System.out.println(child);

		// removing the shave kit and the shirt
		suitcase.remove(shavekit);

		System.out.println(suitcase);

		Iterator it = suitcase.iterator();

		System.out.println("\nFor Each loop");
		for(Component c : suitcase){
			//System.out.println(c);

		}
		
		System.out.println("\nExplicit Iterator method calls");		
		while(it.hasNext()){
			it.next();
			//System.out.println(it.next());		
		}
		
	}
}