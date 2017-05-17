// Fredrik Eliasson
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.ArrayDeque;
import java.util.Deque;
import javax.swing.*;

/**
 * Class handling and displaying content of a web page
 */

class WebReader extends JEditorPane{

    private Deque<String> laterAdresses = new ArrayDeque();
    private Deque<String> earlierAdresses = new ArrayDeque<>();
    private String currentAddress = null;

    WebReader(){
        setContentType("text/html");
        setPreferredSize(new Dimension(300,600));
        setVisible(true);
        setEditable(false);
    }

    void stepBack() throws IOException{
        String address = earlierAdresses.pop();
        setPage(new URL(address));
        laterAdresses.push(currentAddress);
        currentAddress = address;
    }

    void stepForward() throws IOException{
        String address = laterAdresses.pop();
        setPage(new URL(address));
        earlierAdresses.push(currentAddress);
        currentAddress = address;
    }

    boolean earlierAdressesExists(){
        return !earlierAdresses.isEmpty();
    }

    boolean laterAdressesExists(){
        return !laterAdresses.isEmpty();
    }

    String getCurrentAddress(){
        return currentAddress;
    }

    void showPage(String address) throws IOException {

        // necessary check that the http response is okay, setPage() doesn't complain about 410s for example.
        HttpURLConnection connection = (HttpURLConnection) new URL(address).openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        int responseCode = connection.getResponseCode();
        if(responseCode >= 400){
            throw new IOException("code: " + responseCode);
        }

        setPage(new URL(address));
        if(currentAddress != null){
            earlierAdresses.push(currentAddress);
        }
        currentAddress = address;
        laterAdresses.clear();
    }
}