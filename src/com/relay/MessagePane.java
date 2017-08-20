package com.relay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by erickalantyrski on 2017-06-06.
 */


/*
This is the overal component that has the textbox and the messages of a contact
 */

public class MessagePane extends JPanel {
    public static final int WIDTH = 448;
    public static final int HEIGHT = 450;
    private MessageView messageView;
    private JScrollPane messageScroll;
    private MessageTextBox messageTextBox;
    private Action keyboardAction;
    private String m1 = "Hello my name is Eric, what is up?";
    private String m2 = "Hello my name is not Eric, what is not up? Because I am up doing this awesome project.";


    public MessagePane(Action keyboardAction)
    {
        this.keyboardAction = keyboardAction;

        setLayout(new BorderLayout(0,0));
        messageTextBox = new MessageTextBox();

        messageView= new MessageView();
        messageTextBox.getActionMap().put("Enter", keyboardAction); //this code adds the listener to the textbox
        messageTextBox.getInputMap(JLabel.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
        messageScroll = new JScrollPane(messageView);
        messageScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);


        add(messageScroll, BorderLayout.CENTER);
        add(messageTextBox, BorderLayout.SOUTH);

    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(WIDTH, HEIGHT);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
    }
    //Adds a message to view
    //Param is the message to add
    public void addMessage(Message message)
    {
        messageView.addMessage(message);
        update();
    }
    //Repaints current component and the messageview component
    public void update()
    {

        revalidate();
        repaint();
        messageView.repaint();
        messageScroll.repaint();
        JScrollBar bar = messageScroll.getVerticalScrollBar();
        bar.setValue(bar.getMinimum());
        System.out.println(bar.getMaximum());
        System.out.println(bar.getVisibleAmount());
        bar.setValue(bar.getMaximum() - bar.getVisibleAmount());

    }
    //Switches the messagePane to a new contact
    //Param is the contact to switch to
    public void switchContact(Contact contact)
    {
        messageView.addNewMessages(contact.getMessages());
        update();
    }

}
