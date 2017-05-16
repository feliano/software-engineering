// Fredrik Eliasson
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.ArrayDeque;
import java.util.Deque;
import javax.swing.*;

class WebReader extends JEditorPane{

    Deque<String> laterAdresses = new ArrayDeque();
    Deque<String> earlierAdresses = new ArrayDeque<>();
    String currentAddress = null;

    WebReader(){
        setContentType("text/html");
        setPreferredSize(new Dimension(300,600));
        setVisible(true);
        setEditable(false);
    }

    void stepBack() throws IOException{
        String address = earlierAdresses.pop();
        laterAdresses.push(currentAddress);
        currentAddress = address;
        setPage(new URL(address));
    }

    void stepForward() throws IOException{
        String address = laterAdresses.pop();
        earlierAdresses.push(currentAddress);
        currentAddress = address;
        setPage(new URL(address));
    }

    public boolean earlierAdressesExists(){
        System.out.println(earlierAdresses.size());
        return !earlierAdresses.isEmpty();
    }

    public boolean laterAdressesExists(){
        return !laterAdresses.isEmpty();
    }

    public String getCurrentAddress(){
        return currentAddress;
    }

    void showPage(String address) throws IOException {
        setPage(new URL(address));
        if(currentAddress != null){
            earlierAdresses.push(currentAddress);
        }
        currentAddress = address;
        laterAdresses.clear();
    }
}