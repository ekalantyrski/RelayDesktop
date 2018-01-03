package com.relay;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by erickalantyrski on 2017-10-22.
 */
public class MessagePanel extends JPanel {
    public static final int WIDTH = MessagesView.WIDTH;
    public static final int maxLengthOfMessage = 250;
    public static final int lineHeight = 15;
    public static final int userSentMessageStart = 170; //pixels from left
    public static final int userReceivedMessageStart = 10; // pixels from left
    public static final int messagePadding = 10;
    public static final int verticalPadding = 5;
    public int HEIGHT;
    private int boxHeight;


    private Message message;
    private String[] lines;

    public MessagePanel(Message message)
    {
        this.message = message;
        lines = splitMessage(message.getMessage());
        boxHeight = (lines.length * lineHeight) + lineHeight - 4;
        HEIGHT = boxHeight + verticalPadding * 2;

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }

    @Override
    public Dimension getMinimumSize()
    {
        return new Dimension(WIDTH, HEIGHT);
    }

    @Override
    public Dimension getMaximumSize()
    {
        return new Dimension(WIDTH, HEIGHT);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawMessage(g, message);
    }

    public void drawMessage(Graphics g, Message message)
    {
        //if user sent message, display on right hand side, otherwise display on left hand side
        g.setFont(Screen.font);
        if(message.ifUserSentMessage())
        {
            g.setColor(ContactButton.BLUE);
            g.fillRect(userSentMessageStart, verticalPadding, maxLengthOfMessage + 5, boxHeight);
            g.setColor(Color.WHITE);
            for(int i = 0; i < lines.length; i++)
            {
                g.drawString(lines[i], userSentMessageStart + 5, verticalPadding + (i*(lineHeight + 2)) + lineHeight);
            }
        }
        else
        {
            g.setColor(Color.WHITE);
            g.fillRect(userReceivedMessageStart, verticalPadding, maxLengthOfMessage + 5, boxHeight);
            g.setColor(Color.BLACK);
            for(int i = 0; i < lines.length; i++)
            {
                g.drawString(lines[i], userReceivedMessageStart + 5, verticalPadding + (i*(lineHeight + 2)) + lineHeight);
            }
        }


    }

    /**
     * Given a string of a message, splits the message into lines
     * based on the preset length of a message
     * @param message The message to be split
     * @return String array containing each line
     */
    private String[] splitMessage(String message)
    {
        String[] finalLines;
        if(this.getFontMetrics(Screen.font).stringWidth(message) > maxLengthOfMessage)
        {
            StringTokenizer st = new StringTokenizer(message, " ");
            ArrayList<String> lines = new ArrayList<>();
            StringBuilder sb = new StringBuilder(70);
            while(st.hasMoreTokens()) // splits the message into individual works
            {
                String next = st.nextToken();
                sb.append(next + " ");
                if(this.getFontMetrics(Screen.font).stringWidth(sb.toString()) > maxLengthOfMessage) //appends until the string exceeds limit allowed
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
            finalLines = new String[]{message};
        }
        return finalLines;
    }

}

