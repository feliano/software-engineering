import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


class Window extends JFrame{

	final static int TABLE_MAX_ROWS = 50;
	final static int TABLE_NUM_COLUMNS = 2;

	JTextField addressField;
	WebReader webReader;
	JTable linksTable;
	JScrollPane linksScrollPane;
	JScrollPane readerScrollPane;
	ArrayList<String> links = new ArrayList<>();
	ArrayList<String> titles = new ArrayList<>();

	public Window(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setVisible(true);
		setSize(500,500);

		addressField = new JTextField("http://www.nada.kth.se/~henrik");
		addressField.addActionListener(event -> {
            String address = event.getActionCommand();
            System.out.println(address);
            try{
                webReader.setPage(new URL(address));
				updateLinks(address);
            }catch (MalformedURLException e1){
                e1.printStackTrace();
            }catch (IOException e2) {
                e2.printStackTrace();
            } catch (BadLocationException e3) {
				e3.printStackTrace();
			}
		});
		getContentPane().add(addressField,BorderLayout.NORTH);

		webReader = new WebReader();
		readerScrollPane = new JScrollPane(webReader);
		getContentPane().add(readerScrollPane,BorderLayout.CENTER);

		linksTable = new JTable(TABLE_MAX_ROWS,TABLE_NUM_COLUMNS);
		JTableHeader header = linksTable.getTableHeader();
		TableColumnModel tableColumnModel = header.getColumnModel();

		TableColumn tableColumnOne = tableColumnModel.getColumn(0);
		tableColumnOne.setHeaderValue("Web Address");
		TableColumn tableColumnTwo = tableColumnModel.getColumn(1);
		tableColumnTwo.setHeaderValue("About");
		//header.repaint();

		linksScrollPane = new JScrollPane(linksTable);
		getContentPane().add(linksScrollPane,BorderLayout.EAST);
		pack();
	}

	private void updateLinks(String address) throws IOException, BadLocationException {
		InputStream in = new URL(address).openConnection().getInputStream();
		InputStreamReader reader = new InputStreamReader(in);

		links.clear();
		titles.clear();

		HTMLDocument doc = new HTMLDocument();
		doc.putProperty("IgnoreCharsetDirective",true);
		new HTMLEditorKit().read(reader,doc,0);
		HTMLDocument.Iterator it = doc.getIterator(HTML.Tag.A);
		while(it.isValid()){
			String link = (String) it.getAttributes().getAttribute(HTML.Attribute.HREF);
			int start = it.getStartOffset();
			int len = it.getEndOffset()-start;
			String title = doc.getText(start,len);

			links.add(link);
			titles.add(title);
			System.out.println(title + ": " + link);
			it.next();
		}

		DefaultTableModel defaultTableModel = (DefaultTableModel) linksTable.getModel();
		defaultTableModel.setRowCount(0);
		// update JTable
		int numLinks = (links.size() > TABLE_MAX_ROWS ? TABLE_MAX_ROWS : links.size()); // limit num of links
		for (int i = 0; i < numLinks; i++) {
			defaultTableModel.insertRow(0,new Object[] { links.get(i), titles.get(i)});
		}
		defaultTableModel.setRowCount(TABLE_MAX_ROWS); // calls for a UI update
	}

	public static void main(String[] args){
		new Window();
	}

}