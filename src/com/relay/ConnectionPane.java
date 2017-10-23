package com.relay;

import javax.swing.*;
import java.awt.*;

/**
 * Created by erickalantyrski on 2017-10-08.
 */
public class ConnectionPane extends JPanel{
    /*
    This class contains info on how to connect
    the phone to the computer.
    Has the randomly generated password and
    where to put the password
     */
    private static final int WIDTH = MessagePane.WIDTH;
    private static final int HEIGHT = MessagePane.HEIGHT;
    public static final Font font = new Font("Georgia", Font.PLAIN, 20);


    public ConnectionPane(String password)
    {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Your password is: " + password);
        label.setFont(font);
        label.setHorizontalAlignment(JLabel.CENTER);
        add(label, BorderLayout.CENTER);
    }

}
