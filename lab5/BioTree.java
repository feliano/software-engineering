import javax.swing.*;
import javax.swing.tree.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayDeque;
import java.util.Deque;

class BioTree extends TreeFrame {

    static String mTreeFile = "Liv.xml";
    BioNode root;

    @Override
    void initTree() {
        // Create root and treemodel and tree
        try {
            buildTree();
        } catch (Exception e) {
            e.printStackTrace();
        }
        tree.setBackground(Color.green);
    }


    private void buildTree() throws IOException, ParserConfigurationException, SAXException {
        // parse mTreeFile
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = documentBuilder.parse(new File(mTreeFile));
        root = new BioNode(doc.getDocumentElement().getAttribute("namn"));
        treeModel = new DefaultTreeModel(root);
        tree = new JTree(treeModel);

        Node docRootNode = doc.getDocumentElement();
        root.setInfoText(parseInfoText(docRootNode.getTextContent()));
        root.setBioLevel(docRootNode.getNodeName());

        // Stacks to keep track of current and parent nodes
        Deque<Node> nodeStack = new ArrayDeque<>();
        nodeStack.push(doc.getDocumentElement());
        Deque<DefaultMutableTreeNode> parentStack = new ArrayDeque<>();
        parentStack.push(root);

        // Use non-recursive DFS to build tree
        while (nodeStack.size() != 0) {
            Node current = nodeStack.pop();
            DefaultMutableTreeNode parent = parentStack.pop();
            NodeList childNodes = current.getChildNodes();
            // add children to stack and tree
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node child = childNodes.item(i);
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    BioNode newNode = new BioNode(child.getAttributes().getNamedItem("namn").getNodeValue());
                    newNode.setInfoText(parseInfoText(child.getTextContent()));
                    newNode.setBioLevel(child.getNodeName());
                    parent.add(newNode);
                    parentStack.push(newNode);
                    nodeStack.push(childNodes.item(i));
                }
            }
        }
    }

    // help method that makes sure each node has the right info
    private String parseInfoText(String infoText) {
        try {
            String[] strings = infoText.split("\n");
            return strings[0];
        } catch (NullPointerException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    void showDetails(TreePath path) {
        if (path == null) {
            return;
        }

        BioNode node = (BioNode) path.getLastPathComponent();
        String info = node.getBioLevel() + ": " + node.getUserObject().toString() + node.getInfoText();

        BioNode parent = (BioNode) ((BioNode) path.getLastPathComponent()).getParent();
        String chain = ", allt som är " + node.getUserObject().toString();
        while (parent != null) {
            chain += " är ";
            chain += parent.getUserObject().toString();
            parent = (BioNode) parent.getParent();
        }
        JOptionPane.showMessageDialog(this, info + chain);
    }

    public static void main(String[] args) {
        try {
            if (args[0].length() != 0) {
                if (new File(args[0]).isFile()) {
                    mTreeFile = args[0];
                    System.out.println("Loading " + args[0]);
                } else {
                    throw new FileNotFoundException("file " + args[0] + " couldn't be found");
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Loading Liv.xml");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Loading Liv.xml");
        }
        new BioTree();
    }
}


class BioNode extends DefaultMutableTreeNode {
    private String mLevel;
    private String mInfoText;

    public BioNode(String name) {
        super(name);
    }

    public void setInfoText(String info) {
        mInfoText = info;
    }

    public void setBioLevel(String level) {
        mLevel = level;
    }

    public String getInfoText() {
        return mInfoText;
    }

    public String getBioLevel() {
        return mLevel;
    }

}