// Fredrik Eliasson
import java.awt.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

class WebReader extends JEditorPane{

    WebReader(){
        setContentType("text/html");
        getDocument().putProperty("IgnoreCharsetDirective",true);
        setPreferredSize(new Dimension(300,600));
        setVisible(true);
        setEditable(false);
    }

    void showPage(String address) throws IOException {
        setPage(new URL(address));
    }

}