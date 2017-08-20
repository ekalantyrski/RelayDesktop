package com.relay;

import javax.swing.*;
import java.awt.*;

/**
 * Created by erickalantyrski on 2017-06-09.
 */
public class MessageTextBox extends JTextArea{


    private JScrollPane scrollPane;
    private JTextArea textArea;
    public MessageTextBox()
    {
        super(2, 5);
        setLineWrap(true);
        setWrapStyleWord(true);

       // scrollPane.add(textArea);
        //add(scrollPane);

    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(45, 35);
    }



}
