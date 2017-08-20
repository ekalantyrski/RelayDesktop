package com.relay;

import javax.swing.*;
import java.awt.*;

/**
 * Created by erickalantyrski on 2017-06-02.
 */
public class ContactButton extends JButton{
    public static final int HEIGHT = 50;
    public static final int WIDTH = 192 - 15;
    public static final Color GREY = new Color(0,0,0, 10);
    public static final Color BLUE = new Color(30, 144, 255, 255);
    private boolean selected;

    private Contact contact;
    public ContactButton(Contact contact)
    {
        super();
        setBorderPainted(false);
        this.contact = contact;
//        System.out.println(contact.getFirstName());
        selected = false;
//        System.out.println("Created");

    }

    @Override
    //Paints the button
    //Param is graphics object to paint with
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintBackground(g);
        g.drawString(contact.getFirstName() + " " + contact.getLastName(), 5, 20);
        g.drawString(contact.getPhoneNumber(), 5, 40);
        g.drawLine(0, HEIGHT - 1, WIDTH, HEIGHT - 1);
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
    public Point getLocation(Point rv) {
        return new Point(0,0);
    }

    public Contact getContact()
    {
        return contact;
    }

    private void paintBackground(Graphics g)
    {
        Color color = (selected) ? BLUE : GREY;
        g.setColor(color);
        g.fillRect(0,0, WIDTH, HEIGHT);
        g.setColor(Color.BLACK);
    }


    //Changes value to selected (button will now be blue)
    public void select()
    {
        selected = true;
    }
    //changes value to not selected (button will be grey)
    public void deselect()
    {
        selected = false;
    }

}
