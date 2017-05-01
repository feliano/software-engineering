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

        setSize(300,200);
        setEditable(false);
        setText("SOME RANDOM TEXT HERE\n");
        String tempContent = "";
        for(int i = 0;i<50;i++){
            tempContent += "\n";
        }
        setText("SOME RANDOM TEXT HERE" + tempContent + "end");
        //setVisible(true);
    }

    public void showPage(){

    }



}