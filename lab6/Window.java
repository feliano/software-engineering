import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.*;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;

class Window extends JFrame {

	private final static int TABLE_MAX_ROWS = 50;
	private final static int TABLE_NUM_COLUMNS = 2;

	private JTextField addressField;
	private WebReader webReader;
	private JTable linksTable;
	private ArrayList<String> links = new ArrayList<>();
	private ArrayList<String> titles = new ArrayList<>();

	public Window(){
		setTitle("Browser");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setVisible(true);
		setSize(800,800);

		addressField = new JTextField("http://www.nada.kth.se/~henrik");
		addressField.addActionListener(event -> {
            String address = event.getActionCommand();
			new Thread(new DataLoader()).start();
		});
		getContentPane().add(addressField,BorderLayout.NORTH);

		webReader = new WebReader();
		JScrollPane readerScrollPane = new JScrollPane(webReader);
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
													String address = (String) linksTable.getValueAt(row,col);
														addressField.setText(address);
														new Thread(new DataLoader()).start();
													}
											}
										}
									});
		linksTable.setModel(defaultTableModel);
		JScrollPane linksScrollPane = new JScrollPane(linksTable);
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

		/*
		InputStream in2 = new URL(address).openConnection().getInputStream();
		InputStreamReader reader2 = new InputStreamReader(in2,"iso-8859-1");

		new ParserDelegator().parse(reader2,new HTMLEditorKit.ParserCallback(){
			@Override
			public void handleStartTag(HTML.Tag tag, MutableAttributeSet a, int pos){
				if(tag.equals(HTML.Tag.A)){
					String link = (String) a.getAttribute(HTML.Attribute.HREF);
					System.out.println(link);
				}
			}
		},true);
		*/

		while(it.isValid()){
			String link = (String) it.getAttributes().getAttribute(HTML.Attribute.HREF);
			// only add links which start with 'http'
			if(link != null && link.length() > 4 && link.substring(0,4).equals("http")){
				int start = it.getStartOffset();
				int len = it.getEndOffset()-start;
				String title = doc.getText(start,len);

				links.add(link);
				titles.add(title);
			}
			it.next();
		}

		DefaultTableModel defaultTableModel = (DefaultTableModel) linksTable.getModel();
		defaultTableModel.setRowCount(0);
		// update JTable
		int numLinks = (links.size() > TABLE_MAX_ROWS ? TABLE_MAX_ROWS : links.size()); // limit num of links
		for (int i = 0; i < numLinks; i++) {
			defaultTableModel.addRow(new Object[] { links.get(i), titles.get(i)});
		}
		defaultTableModel.setRowCount(TABLE_MAX_ROWS); // calls for a UI update
		linksTable.setModel(defaultTableModel);
	}

	private void displayError(String message){
		JOptionPane.showMessageDialog (this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public static void main(String[] args){
		new Window();
	}

	private class DataLoader implements Runnable{
		@Override
		public void run() {
			String address = addressField.getText();
			try {
				webReader.showPage(addressField.getText());
				updateLinks(address);
			} catch (IOException | BadLocationException e) {
				displayError("Couldn't open page: " + e.getMessage());
			}
		}
	}

}