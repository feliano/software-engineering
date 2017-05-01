import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;


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
		setTitle("Browser");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setVisible(true);
		setSize(800,800);

		addressField = new JTextField("http://www.nada.kth.se/~henrik");
		addressField.addActionListener(event -> {
            String address = event.getActionCommand();
            System.out.println(address);
            try{
                webReader.setPage(new URL(address));
				updateLinks(address);
            } catch (IOException | BadLocationException e) {
                e.printStackTrace();
            }
		});
		getContentPane().add(addressField,BorderLayout.NORTH);

		webReader = new WebReader();
		readerScrollPane = new JScrollPane(webReader);
		getContentPane().add(readerScrollPane,BorderLayout.CENTER);

		linksTable = new JTable(TABLE_MAX_ROWS,TABLE_NUM_COLUMNS);

		String[] header = {"Web Adress","About"};
		String[][] data = {};
		DefaultTableModel defaultTableModel = new DefaultTableModel(data,header) {
			@Override
			public boolean isCellEditable(int i, int i1) {
				return false;
			}
		};
		defaultTableModel.setRowCount(TABLE_MAX_ROWS);
		defaultTableModel.setColumnCount(TABLE_NUM_COLUMNS);

		linksTable.addMouseListener(new MouseAdapter() {
										@Override
										public void mousePressed(MouseEvent mouseEvent) {
											if(mouseEvent.getClickCount() == 2){
												int row = linksTable.rowAtPoint(mouseEvent.getPoint());
												int col = linksTable.columnAtPoint(mouseEvent.getPoint());
												if(col == 0){
													System.out.println(linksTable.getValueAt(row,col));
													String address = (String) linksTable.getValueAt(row,col);
													System.out.println("address: " + address);
													try{
														webReader.showPage(address);
														updateLinks(address);
														addressField.setText(address);
													}catch (IOException | BadLocationException e) {
														e.printStackTrace();
													}
												}
											}
										}
									});

				linksTable.setModel(defaultTableModel);
		linksScrollPane = new JScrollPane(linksTable);
		getContentPane().add(linksScrollPane,BorderLayout.EAST);
		pack();
	}

	private void updateLinks(String address) throws IOException, BadLocationException {
		InputStream in = new URL(address).openConnection().getInputStream();
		InputStreamReader reader = new InputStreamReader(in,"iso-8859-1");

		links.clear();
		titles.clear();

		HTMLDocument doc = new HTMLDocument();
		doc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);

		new HTMLEditorKit().read(reader,doc,0);
		HTMLDocument.Iterator it = doc.getIterator(HTML.Tag.A);
		while(it.isValid()){
			String link = (String) it.getAttributes().getAttribute(HTML.Attribute.HREF);

			// only add links which start with 'http'
			if(link != null && link.length() > 4 && link.substring(0,4).equals("http")){
				int start = it.getStartOffset();
				int len = it.getEndOffset()-start;
				String title = doc.getText(start,len);

				links.add(link);
				titles.add(title);
				System.out.println(title + ": " + link);
			}
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
		linksTable.setModel(defaultTableModel);
	}

	public static void main(String[] args){
		new Window();
	}

}