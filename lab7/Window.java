// Fredrik Eliasson
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.table.*;
import javax.swing.text.*;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	private ArrayList<Bookmark> bookmarks = new ArrayList<>();

	private JButton backButton;
	private JButton forwardButton;
	private JButton addBookmarkButton;
	private JButton showBookmarksButton;

	private boolean showBookmarks = false;

	public Window(){
		setTitle("Browser");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setVisible(true);
		setSize(800,800);

		JPanel navigator = new JPanel();
		BoxLayout navigatorLayout = new BoxLayout(navigator,BoxLayout.X_AXIS);
		navigator.setLayout(navigatorLayout);

		backButton = new JButton("<-");
		forwardButton = new JButton("->");
		backButton.setEnabled(false);
		forwardButton.setEnabled(false);
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				new Thread(new DataLoader(2)).start();
			}
		});
		forwardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				new Thread(new DataLoader(1)).start();
			}
		});
		backButton.setPreferredSize(new Dimension(50,30));
		forwardButton.setPreferredSize(new Dimension(50,30));

		navigator.add(Box.createRigidArea(new Dimension(5,0))); // padding
		navigator.add(backButton);
		navigator.add(forwardButton);
		navigator.add(Box.createRigidArea(new Dimension(5,0)));

		addressField = new JTextField("http://www.nada.kth.se/~henrik");
		addressField.addActionListener(event -> {
			new Thread(new DataLoader(0)).start();
		});

		navigator.add(addressField);
		navigator.add(Box.createRigidArea(new Dimension(5,0)));

		addBookmarkButton = new JButton("+");
		addBookmarkButton.setEnabled(true);
		addBookmarkButton.setPreferredSize(new Dimension(50,30));
		addBookmarkButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				// Add bookmark
				//Bookmark bookmark = new Bookmark(addressField.getText());
				new BookmarkDialog(true).show();
			}
		});
		navigator.add(addBookmarkButton);
		navigator.add(Box.createRigidArea(new Dimension(5,0)));

		showBookmarksButton = new JButton("*");
		showBookmarksButton.setEnabled(true);
		showBookmarksButton.setPreferredSize(new Dimension(50,30));
		showBookmarksButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				toggleBookmarks();
			}
		});
		navigator.add(showBookmarksButton);
		navigator.add(Box.createRigidArea(new Dimension(5,0)));

		getContentPane().add(navigator,BorderLayout.NORTH);

		webReader = new WebReader();
		JScrollPane readerScrollPane = new JScrollPane(webReader);
		getContentPane().add(readerScrollPane,BorderLayout.CENTER);
		webReader.addHyperlinkListener(hyperlinkEvent -> {
            if(hyperlinkEvent.getEventType() == HyperlinkEvent.EventType.ACTIVATED){
                    addressField.setText(hyperlinkEvent.getURL().toString());
                    new Thread(new DataLoader(0)).start();
                }
            });

		linksTable = new JTable(TABLE_MAX_ROWS,TABLE_NUM_COLUMNS);

		String[] header = {"Web Adress","About"};
		DefaultTableModel defaultTableModel = new DefaultTableModel(new String[][]{},header) {
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
													new Thread(new DataLoader(0)).start();
												}
											}
										}
									});
		linksTable.setModel(defaultTableModel);
		JScrollPane linksScrollPane = new JScrollPane(linksTable);
		getContentPane().add(linksScrollPane,BorderLayout.EAST);
		pack();
	}

	private void updateButtonStates(){

		if(webReader.earlierAdressesExists()){
			backButton.setEnabled(true);
		}else{
			backButton.setEnabled(false);
		}

		if(webReader.laterAdressesExists()){
			forwardButton.setEnabled(true);
		}else{
			forwardButton.setEnabled(false);
		}
	}

	private void updateLinks(String address) throws IOException, BadLocationException {
		InputStream in = new URL(address).openConnection().getInputStream();
		InputStreamReader reader = new InputStreamReader(in,"iso-8859-1"); // have to spec char encoding for links to be loaded nicely

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
		defaultTableModel.setRowCount(TABLE_MAX_ROWS); // this updates UI
		linksTable.setModel(defaultTableModel);
	}

	private void updateUI(String address) throws IOException, BadLocationException {
		updateLinks(address);
		updateButtonStates();
	}

	private void toggleBookmarks(){

		if(showBookmarks){
			// hide, show links instead
			DefaultTableModel defaultTableModel = (DefaultTableModel) linksTable.getModel();
			defaultTableModel.setRowCount(0);
			// update JTable
			int numLinks = (links.size() > TABLE_MAX_ROWS ? TABLE_MAX_ROWS : links.size()); // limit num of links
			for (int i = 0; i < numLinks; i++) {
				defaultTableModel.addRow(new Object[] { links.get(i), titles.get(i)});
			}
			defaultTableModel.setRowCount(TABLE_MAX_ROWS); // this updates UI
			linksTable.setModel(defaultTableModel);
			showBookmarks = false;

		}else{
			//show
			DefaultTableModel defaultTableModel = (DefaultTableModel) linksTable.getModel();
			defaultTableModel.setRowCount(0);
			// update JTable
			//int numLinks = (links.size() > TABLE_MAX_ROWS ? TABLE_MAX_ROWS : links.size()); // limit num of links
			int numLinks = bookmarks.size();
			System.out.print(bookmarks.size());
			for (int i = 0; i < numLinks; i++) {
				defaultTableModel.addRow(new Object[] { bookmarks.get(i).getAddress(), bookmarks.get(i).getName()});
			}
			defaultTableModel.setRowCount(bookmarks.size()); // this updates UI
			linksTable.setModel(defaultTableModel);
			showBookmarks = true;

		}

	}


	private void displayError(String message){
		JOptionPane.showMessageDialog (this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public static void main(String[] args){
		new Window();
	}

	private class DataLoader implements Runnable{

		// 0 - use address in addressfield, 1 - step forward, 2 - step back
		int mode = 0;
		DataLoader(int m){
			mode = m;
			if(mode != 0 && mode != 1 && mode != 2){
				throw(new IllegalArgumentException("Expected range for argument 0-2, was: " + mode));
			}
		}

		@Override
		public void run() {
			try {
				if(mode == 0){
					webReader.showPage(addressField.getText());
				}else if(mode == 1){
					webReader.stepForward();
					addressField.setText(webReader.getCurrentAddress());
				}else if(mode == 2){
					webReader.stepBack();
					addressField.setText(webReader.getCurrentAddress());
				}else{
					throw(new IllegalArgumentException("Expected range for argument 0-2, was: " + mode));
				}
				updateUI(addressField.getText());
			}catch (IOException | BadLocationException e) {
				displayError("Couldn't open page: " + e.getMessage());
			}
		}
	}


	private class BookmarkDialog{

		JTextField dialogAddressField = new JTextField(addressField.getText());
		JTextField dialogNameField = new JTextField();
		JTextField dialogInfoField = new JTextField();
		boolean isAddNewBookmark = true;
		int bookmarkIndex = -1;

		BookmarkDialog(boolean isAddNewBookmark){
			this.isAddNewBookmark = isAddNewBookmark;
		}

		BookmarkDialog(boolean isAddNewBookmark,int bookmarkIndex){
			this.isAddNewBookmark = isAddNewBookmark;
			this.bookmarkIndex = bookmarkIndex;
		}

		void show() {
			if(isAddNewBookmark){
				addNewBookmark();
			}else{
				editExistingBookmark();
			}
		}

		void addNewBookmark(){

			Object[] options = {"Save",
					"Cancel"};

			JPanel panel = new JPanel(new GridLayout(0,1));
			panel.add(new JLabel("Address"));
			panel.add(dialogAddressField);
			panel.add(new JLabel("Name"));
			panel.add(dialogNameField);
			panel.add(new JLabel("Info"));
			panel.add(dialogInfoField);
			int selection = JOptionPane.showOptionDialog(null,panel,"new bookmark",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE,null,options,null);
			if(selection == 0){
				// create new bookmark
				bookmarks.add(new Bookmark(dialogAddressField.getText(),dialogNameField.getText(),dialogInfoField.getText()));
			}
			System.out.println(selection);
		}

		// TODO: pass index of bookmark as parameter
		void editExistingBookmark() {

			Object[] options = {"Save",
					"Delete","Cancel",};

			JPanel panel = new JPanel(new GridLayout(0,1));
			panel.add(new JLabel("Address"));
			panel.add(dialogAddressField);
			panel.add(new JLabel("Name"));
			panel.add(dialogNameField);
			panel.add(new JLabel("Info"));
			panel.add(dialogInfoField);
			int selection = JOptionPane.showOptionDialog(null,panel,"new bookmark",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE,null,options,null);

			System.out.println(selection);

			if(selection == 0){
				// Update bookmark, save changes
			}else if(selection == 1){
				// delete bookmark
			}

		}

	}

}




