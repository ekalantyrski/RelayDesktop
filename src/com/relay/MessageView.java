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
public class MessageView extends JPanel {
    public static final int HEIGHT = 408;
    public static final int WIDTH = 435;
    public static final int maxLengthOfMessage = 200;
    public static final int lineHeight = 15;
    public static final int userSentMessageStart = 220;
    public static final int userReceivedMessageStart = 5;
    public static final int heightBetweenMessages = 20;
    public static final int topOffset = 10;
    private int amountOfLines;
    private ArrayList<Message> messages;

    public MessageView()
    {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        amountOfLines = 0;
        messages = new ArrayList<>();
    }

    public void addNewMessages(ArrayList<Message> messages)
    {
        setPreferredSize(getPreferredSize());
        this.messages = messages;
    }

    @Override
    public void paintComponent(Graphics g)
    {

        super.paintComponent(g);
        g.setColor(ContactButton.GREY);
        int greatestHeight = Math.max(HEIGHT, (amountOfLines*lineHeight + messages.size()*heightBetweenMessages) + topOffset);
        g.fillRect(0,0, WIDTH, greatestHeight);
        g.setColor(Color.BLACK);

        amountOfLines = 0;
        for(int i = 0; i < messages.size(); i++)
        {
            amountOfLines += drawMessage(g, messages.get(i), (amountOfLines*lineHeight + i*heightBetweenMessages) + topOffset);
        }

    }
    @Override
    public Dimension getPreferredSize()
    {
        int greatestHeight = Math.max(HEIGHT, (amountOfLines*lineHeight + messages.size()*heightBetweenMessages) + topOffset);
        return new Dimension(WIDTH, greatestHeight);
    }
    //Draws given message
    //Par: The message to draw
    //Returns amount of lines that were needed to draw message
    public int drawMessage(Graphics g, Message message, int height)
    {
        String[] finalLines;
        if(g.getFontMetrics().stringWidth(message.getMessage()) > maxLengthOfMessage)
        {
            StringTokenizer st = new StringTokenizer(message.getMessage(), " ");
            ArrayList<String> lines = new ArrayList<>();
            StringBuilder sb = new StringBuilder(70);
            while(st.hasMoreTokens()) // splits the message into individual works
            {
                String next = st.nextToken();
                sb.append(next + " ");
                if(g.getFontMetrics().stringWidth(sb.toString()) > maxLengthOfMessage) //appends until the string exceeds limit allowed
                {
                    sb.delete(sb.length() - next.length() - 1, sb.length()); // if exceeds limit, removes the word
                    lines.add(sb.toString());

                    sb = new StringBuilder(70);
                    sb.append(next + " "); // adds removed word to line
                }

            }
            if(sb.length() > 0)
            {
                lines.add(sb.toString());
            }
            finalLines = lines.toArray(new String[lines.size()]);
        }
        else
        {
            finalLines = new String[]{message.getMessage()};
        }

        //if user sent message, display on right hand side, otherwise display on left hand side
        if(message.ifUserSentMessage())
        {
            g.setColor(ContactButton.BLUE);
            g.fillRect(userSentMessageStart, height, maxLengthOfMessage + 5, (finalLines.length * lineHeight) + lineHeight - 4);
            g.setColor(Color.WHITE);
            for(int i = 0; i < finalLines.length; i++)
            {
                g.drawString(finalLines[i], userSentMessageStart + 5, height + (i*(lineHeight + 2)) + lineHeight);
            }
        }
        else
        {
            g.setColor(Color.WHITE);
            g.fillRect(userReceivedMessageStart, height, maxLengthOfMessage + 5, (finalLines.length * lineHeight) + lineHeight - 4);
            g.setColor(Color.BLACK);
            for(int i = 0; i < finalLines.length; i++)
            {
                g.drawString(finalLines[i], userReceivedMessageStart + 5, height + (i*(lineHeight + 2)) + lineHeight);
            }
        }


        return finalLines.length;
    }

    //Adds a message to be displayed
    //Param is the message to be added
    public void addMessage(Message message)
    {
        messages.add(message);
        revalidate();
        repaint();
        System.out.println("Added");
    }

}
