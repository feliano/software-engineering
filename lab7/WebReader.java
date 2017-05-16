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

    WebReader(){
        setContentType("text/html");
        setPreferredSize(new Dimension(300,600));
        setVisible(true);
        setEditable(false);
    }

    void stepBack() throws IOException{
        showPage(earlierAdresses.getFirst());
    }

    void stepForward() throws IOException{
        showPage(laterAdresses.getFirst());
    }

    public boolean earlierAdressesExists(){
        return !earlierAdresses.isEmpty();
    }

    public boolean laterAdressesExists(){
        return !laterAdressesExists();
    }




    void showPage(String address) throws IOException {
        setPage(new URL(address));
        laterAdresses.push(address);
    }
}