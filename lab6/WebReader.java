import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.html.*;
import javax.swing.text.*;


class WebReader extends JEditorPane{

    public WebReader(){

        setContentType("text/html");
        setEditable(false);
    }

    public void showPage(String address) throws IOException {
        setPage(new URL(address));
    }



}