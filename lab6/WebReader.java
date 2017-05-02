import java.awt.*;
import java.io.*;
import java.net.*;
import javax.swing.*;


class WebReader extends JEditorPane{

    public WebReader(){

        setContentType("text/html");
        setPreferredSize(new Dimension(300,600));
        setVisible(true);
        setEditable(false);
    }

    public void showPage(String address) throws IOException {
        setPage(new URL(address));
    }

}