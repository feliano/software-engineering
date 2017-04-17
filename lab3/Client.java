import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Client extends JPanel implements Runnable{

	// make it singleton
	private static Client mClient = new Client();

    private BufferedReader mIn;
    private PrintWriter mOut;
    private String mRandomPick = "STEN"; // placeholder, needs to be fetched from server
    private JTextField mAddressField;
    private JTextField mPortField;
    private JButton mConnectBtn;
    private JLabel mConnectionStatus;
    private boolean mIsConnected = false;
    private Socket mSocket = null;

	private Client(){
		setLayout(new GridLayout(6,1));
		mAddressField = new JTextField("localhost");
		mPortField = new JTextField("4713");
		mConnectBtn = new JButton("Connect");
		mConnectionStatus = new JLabel("Not Connected");

		mConnectBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JButton btn = (JButton) e.getSource();
				if(!mIsConnected){
					mConnectionStatus.setText("Connecting...");				
				}
				connect();
			}
		});

		add(mConnectionStatus,BorderLayout.SOUTH);
		add(new JLabel("Server Name:"));
		add(mAddressField,BorderLayout.SOUTH);
		add(new JLabel("Port Number:"));
		add(mPortField,BorderLayout.SOUTH);
		add(mConnectBtn,BorderLayout.SOUTH);
	}

	public static Client getInstance(){
		return mClient;
	}

	private void connect(){
		// use new thread for connecting to avoid blocking the UI 
		(new Thread(){
			public void run(){
				if(mIsConnected == false){
					try {
						int timeout = 5000; // timeout to avoid freeze if connection fails
						mSocket = new Socket();
						mSocket.connect(new InetSocketAddress(mAddressField.getText(),Integer.parseInt(mPortField.getText())),timeout);
						mIn=new BufferedReader
						   (new InputStreamReader(mSocket.getInputStream()));
						mOut=new PrintWriter(mSocket.getOutputStream());
						mOut.println("felia"); 
						mOut.flush();
						String line = null;
						while(true){
							if( (line = mIn.readLine()) != null){
						  		System.out.println(line);
						   		break;
							}
						}
						// fetch next random pick
						(new Thread(mClient)).start();
						mConnectBtn.setText("Disconnect");
						mConnectionStatus.setText("Connected to server");
						mIsConnected = true;
					}catch(Exception ex){
						mConnectionStatus.setText("Could not connect to server...");
						mConnectBtn.setText("Connect");
						System.out.println(ex);
					}							
				}else{
					// disconnect from socket
					try{
						mSocket.close();
						mConnectBtn.setText("Connect");
						mConnectionStatus.setText("Not Connected");
						mIsConnected = false;
					}catch(Exception ex){
						System.out.println(ex);
					}
				}
			}
		}).start();
	}


	public void run(){
	
		try{
			String inputData = null;
			mOut.println("Random Pick");
			mOut.flush();
			inputData = mIn.readLine();
			while(true){
				if(inputData != null){
					mRandomPick = inputData;
			   		break;	   	
				}
			}
		}catch(Exception e){
			System.out.println(e);
		}
	}

	public String getRPSPick(){

		(new Thread(this)).start();
		return mRandomPick;
	}
}


