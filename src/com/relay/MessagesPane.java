package com.relay;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;

/**
 * Created by erickalantyrski on 2017-06-06.
 */


/*
This is the overal component that has the textbox and the messages of a contact
 */

public class MessagesPane extends JPanel {
    public static final int WIDTH = 448;
    public static final int HEIGHT = 450;
    private MessagesView messageView;
    private JScrollPane messageScroll;
    private MessageTextBox messageTextBox;
    private Action keyboardAction;


    public MessagesPane(Action keyboardAction)
    {
        this.keyboardAction = keyboardAction;

        setLayout(new BorderLayout(0,0));
        messageTextBox = new MessageTextBox();

        messageView= new MessagesView();
        messageTextBox.getActionMap().put("Enter", keyboardAction); //this code adds the listener to the textbox
        messageTextBox.getInputMap(JLabel.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
        messageScroll = new JScrollPane(messageView);
        messageScroll.getVerticalScrollBar().setUnitIncrement(12);
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
    }
    //Repaints current component and the messageview component
    public void update()
    {
        this.revalidate();
        messageView.revalidate();
        repaint();
        scrollDown();
    }
    //Switches the messagePane to a new contact
    //Param is the contact to switch to
    public void switchContact(Contact contact)
    {
        messageView.addNewMessages(contact.getMessages());
        update();
    }

    public void scrollDown()
    {
        JScrollBar verticalBar = messageScroll.getVerticalScrollBar();
        AdjustmentListener downScroller = new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                Adjustable adjustable = e.getAdjustable();
                adjustable.setValue(adjustable.getMaximum());
                verticalBar.removeAdjustmentListener(this);
            }
        };
        verticalBar.addAdjustmentListener(downScroller);
    }

}
