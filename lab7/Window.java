// Fredrik Eliasson
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.table.*;
import javax.swing.text.*;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.Book;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;

/**
 * Web browser main window class
 */

class Window extends JFrame {

	private final static int TABLE_MAX_ROWS = 50;
	private final static int TABLE_NUM_COLUMNS = 2;
	private final static Color ACTIVATED_COLOR = Color.GRAY;

	private FileManager fileManager = FileManager.getInstance();

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
	private JButton editBookmarksButton;

	private JLabel tableLabel;
	private boolean displayBookmarks = false;
	private boolean canEditBookmarks = false;

	private String[] header = {"Web Adress","Name"};

	public Window(){
		setTitle("Browser");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setVisible(true);
		setSize(800,800);

		bookmarks = fileManager.loadBookMarks("bookmarks.json");

		// saves bookmarks on exit
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				fileManager.saveBookmarks(bookmarks);
			}
		});

		JPanel navigator = new JPanel();
		BoxLayout navigatorLayout = new BoxLayout(navigator,BoxLayout.X_AXIS);
		navigator.setLayout(navigatorLayout);

		backButton = new JButton();
		backButton.setIcon(new ImageIcon("arrow_left.png"));
		forwardButton = new JButton();
		forwardButton.setIcon(new ImageIcon("arrow_right.png"));

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
		backButton.setPreferredSize(new Dimension(30,30));
		forwardButton.setPreferredSize(new Dimension(30,30));

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

		addBookmarkButton = new JButton();
		addBookmarkButton.setIcon(new ImageIcon("star.png"));
		addBookmarkButton.setEnabled(true);
		addBookmarkButton.setPreferredSize(new Dimension(40,40));
		addBookmarkButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				// Add bookmark
				new BookmarkDialog().addNewBookmark();
			}
		});
		navigator.add(addBookmarkButton);
		navigator.add(Box.createRigidArea(new Dimension(5,0)));

		editBookmarksButton = new JButton();
		editBookmarksButton.setIcon(new ImageIcon("edit.png"));
		editBookmarksButton.setVisible(false);
		editBookmarksButton.setPreferredSize(new Dimension(40,40));
		editBookmarksButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				// toggle edit mode
				if(canEditBookmarks){
					canEditBookmarks = false;
				}else{
					canEditBookmarks = true;
				}
				updateButtonStates();
			}
		});
		navigator.add(editBookmarksButton);
		navigator.add(Box.createRigidArea(new Dimension(5,0)));

		showBookmarksButton = new JButton();
		showBookmarksButton.setIcon(new ImageIcon("list.png"));
		showBookmarksButton.setEnabled(true);
		showBookmarksButton.setPreferredSize(new Dimension(40,40));
		showBookmarksButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				if(displayBookmarks){
					toggleBookmarks(false);
				}else{
					toggleBookmarks(true);
				}
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

		DefaultTableModel defaultTableModel = new DefaultTableModel(new String[][]{},header) {
			@Override
			public boolean isCellEditable(int i, int i1) {
				return false;
			}

			@Override
			public String getColumnName(int index){
				return header[index];
			}
		};

		defaultTableModel.setRowCount(TABLE_MAX_ROWS);
		defaultTableModel.setColumnCount(TABLE_NUM_COLUMNS);

		linksTable.setAutoCreateRowSorter(true); // allows sorting of the rows in the table
		linksTable.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent mouseEvent) {
						if (mouseEvent.getClickCount() == 2) {
							int row = linksTable.rowAtPoint(mouseEvent.getPoint());
							int col = linksTable.columnAtPoint(mouseEvent.getPoint());
							if(canEditBookmarks){
								// edit the bookmarks
								int index = linksTable.convertRowIndexToModel(row);
								new BookmarkDialog().editExistingBookmark(index);
							}else{
								if (col == 0) {
									String address = (String) linksTable.getValueAt(row, col);
									addressField.setText(address);
									new Thread(new DataLoader(0)).start();
									canEditBookmarks = false;
									displayBookmarks = false;
									updateButtonStates();
								}
							}
						}
					}
				});
		linksTable.setModel(defaultTableModel);

		JPanel tableView = new JPanel();
		BoxLayout tableViewLayout = new BoxLayout(tableView,BoxLayout.Y_AXIS);
		tableView.setLayout(tableViewLayout);
		JScrollPane linksScrollPane = new JScrollPane(linksTable);
		tableLabel = new JLabel("Site Links");
		tableLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		tableView.add(tableLabel);
		tableView.add(linksScrollPane);

		getContentPane().add(tableView,BorderLayout.EAST);
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

		if(displayBookmarks){
			showBookmarksButton.setBackground(ACTIVATED_COLOR);
			editBookmarksButton.setVisible(true);
		}else{
			showBookmarksButton.setBackground(null);
			editBookmarksButton.setVisible(false);
		}

		if(canEditBookmarks){
			editBookmarksButton.setBackground(ACTIVATED_COLOR);
		}else{
			editBookmarksButton.setBackground(null);
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

	private void toggleBookmarks(boolean showBookmarks){
		displayBookmarks = showBookmarks;
		if(!showBookmarks){
			tableLabel.setText("Site Links");
			// hide, show links instead
			canEditBookmarks = false;
			DefaultTableModel defaultTableModel = (DefaultTableModel) linksTable.getModel();
			defaultTableModel.setRowCount(0);
			// update JTable
			int numLinks = (links.size() > TABLE_MAX_ROWS ? TABLE_MAX_ROWS : links.size()); // limit num of links
			for (int i = 0; i < numLinks; i++) {
				defaultTableModel.addRow(new Object[] { links.get(i), titles.get(i)});
			}
			defaultTableModel.setRowCount(TABLE_MAX_ROWS); // this updates UI
			linksTable.setModel(defaultTableModel);
		}else{
			tableLabel.setText("Bookmarks");
			//show bookmarks
			canEditBookmarks = false;
			DefaultTableModel defaultTableModel = (DefaultTableModel) linksTable.getModel();
			defaultTableModel.setRowCount(0);

			// update JTable
			int numLinks = bookmarks.size();
			for (int i = 0; i < numLinks; i++) {
				defaultTableModel.addRow(new Object[] { bookmarks.get(i).getAddress(), bookmarks.get(i).getName()});
			}
			defaultTableModel.setRowCount(bookmarks.size()); // this updates UI
			linksTable.setModel(defaultTableModel);
		}
		updateButtonStates();
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
				// hide bookmarks
				editBookmarksButton.setVisible(false);
				showBookmarksButton.setBackground(null);
			}catch (IOException | BadLocationException e) {
				displayError("Couldn't open page: " + e.getMessage());
			}
		}
	}


	private class BookmarkDialog{

		JTextField dialogAddressField = new JTextField(addressField.getText());
		JTextField dialogNameField = new JTextField();

		BookmarkDialog(){}

		private void addNewBookmark(){

			Object[] options = {"Save",
					"Cancel"};

			JPanel panel = new JPanel(new GridLayout(0,1));
			panel.add(new JLabel("Address"));
			panel.add(dialogAddressField);
			panel.add(new JLabel("Name"));
			panel.add(dialogNameField);
			int selection = JOptionPane.showOptionDialog(null,panel,"New Bookmark",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE,null,options,null);
			if(selection == 0){
				// create new bookmark
				bookmarks.add(new Bookmark(dialogAddressField.getText(),dialogNameField.getText()));
			}

			// update table
			if(displayBookmarks){
				toggleBookmarks(true);
			}

		}

		private void editExistingBookmark(int index) {

			Bookmark oldBookmark = bookmarks.get(index);

			Object[] options = {"Save",
					"Delete","Cancel",};

			JPanel panel = new JPanel(new GridLayout(0,1));
			panel.add(new JLabel("Address"));
			dialogAddressField.setText(oldBookmark.getAddress());
			panel.add(dialogAddressField);
			panel.add(new JLabel("Name"));
			dialogNameField.setText(oldBookmark.getName());
			panel.add(dialogNameField);
			int selection = JOptionPane.showOptionDialog(null,panel,"Edit Bookmark",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE,null,options,null);

			if(selection == 0){
				// Update bookmark, save changes
				bookmarks.remove(index);
				bookmarks.add(index,new Bookmark(dialogAddressField.getText(),dialogNameField.getText()));
			}else if(selection == 1){
				// delete bookmark
				bookmarks.remove(index);
			}

			// update table
			if(displayBookmarks){
				toggleBookmarks(true);
			}

		}
	}

}




