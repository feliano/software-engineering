import javax.swing.*;
import javax.swing.tree.*;

class BioTree extends TreeFrame{

	

	@Override
	void initTree(){
		// Create root and treemodel and tree
		root = new DefaultMutableTreeNode("root");
		treeModel = new DefaultTreeModel(root);
		tree = new JTree(treeModel);
	}


	public static void main(String[] args){
		new BioTree();
	}

}