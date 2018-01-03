package com.relay;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by erickalantyrski on 2017-06-09.
 */

/*
This class renders all the messages
 */
public class MessagesView extends JPanel {
    public static final int HEIGHT = 408;
    public static final int WIDTH = 435;

    public static final int topOffset = 10;
    private int amountOfMessages;
    private ArrayList<Message> messages;

    public MessagesView()
    {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        amountOfMessages = 0;
        messages = new ArrayList<>();
    }

    public void addNewMessages(ArrayList<Message> messages)
    {
        this.removeAll();
        setPreferredSize(getPreferredSize());
        this.messages = messages;
        for(int i = 0; i < messages.size(); i++)
        {
            MessagePanel mPanel = new MessagePanel(messages.get(i));
            add(mPanel);
        }
        amountOfMessages = messages.size();
    }

//    @Override
//    public void paintComponent(Graphics g)
//    {
//
//        super.paintComponent(g);
//        g.setColor(ContactButton.GREY);
//        int greatestHeight = Math.max(HEIGHT, (amountOfLines*lineHeight + messages.size()*heightBetweenMessages) + topOffset);
//        g.fillRect(0,0, WIDTH, greatestHeight);
//        g.setColor(Color.BLACK);
//
//        amountOfLines = 0;
//        for(int i = 0; i < messages.size(); i++)
//        {
//            amountOfLines += drawMessage(g, messages.get(i), (amountOfLines*lineHeight + i*heightBetweenMessages) + topOffset);
//        }
//
//    }
    @Override
    public Dimension getPreferredSize()
    {
        Component[] components = getComponents();
        int componentHeight = 0;
        for(int i = 0; i < components.length; i++)
        {
            componentHeight += components[i].getPreferredSize().height;
        }
        int greatestHeight = Math.max(HEIGHT, componentHeight);
        return new Dimension(WIDTH, greatestHeight);
    }
    //Draws given message
    //Par: The message to draw
    //Returns amount of lines that were needed to draw message


    //Adds a message to be displayed
    //Param is the message to be added
    public void addMessage(Message message)
    {
        messages.add(message);
        amountOfMessages++;
        MessagePanel mPanel = new MessagePanel(message);
        add(mPanel);
        revalidate();
        repaint();
        System.out.println("Added");
    }

}
