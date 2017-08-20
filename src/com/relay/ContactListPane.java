package com.relay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.util.ArrayList;


/*
This class contains all the contactbuttons, and controls them


 */

/**
 * Created by erickalantyrski on 2017-06-02.
 */
public class ContactListPane extends JPanel {
    private int numberOfContactLabels = 0;
    private ActionListener contactButtonListener;

    public ContactListPane(ActionListener contactButtonListener) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.contactButtonListener = contactButtonListener;

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(ContactButton.WIDTH, (numberOfContactLabels * ContactButton.HEIGHT));
    }

    public Dimension getMinimumSize()
    {
        return new Dimension(ContactButton.WIDTH, (numberOfContactLabels * ContactButton.HEIGHT));
    }
    //Adds a contact to the end of the list
    //param is contact to add
    public void addContact(Contact contact)
    {
        ContactButton contactButton = new ContactButton(contact);
        contactButton.addActionListener(contactButtonListener);
        this.add(contactButton);

        repaint();

        numberOfContactLabels++;
    }
    //Adds a contact to end of list, also has the contact be selected
    //Param is the contact to add, and whether or not to be selected
    public void addContact(Contact contact, boolean selectAddedContact)
    {
        ContactButton contactButton = new ContactButton(contact);
        contactButton.addActionListener(contactButtonListener);
        this.add(contactButton);

        if(selectAddedContact)
            contactButton.select();

        repaint();

        numberOfContactLabels++;
    }
    //Add a contact to a specific point in list
    //Param is contact to add and the index of where to add
    public void addContact(Contact contact, int index)
    {

        ContactButton contactButton = new ContactButton(contact);
        contactButton.addActionListener(contactButtonListener);


        Component[] comp = getComponents();
        for(int i = 0; i < comp.length; i++)
        {
            if(i == index)
            {
                add(contactButton);
            }
            add(comp[i]);
        }

        if(index == comp.length) //this makes sure that if index is equal to length (i.e add to end of list), then it is added
        {
            add(contactButton);
        }

        repaint();

        numberOfContactLabels++;
    }
    //deselects all contacts (makes all grey)
    public void deselectAll()
    {
        Component[] comp = getComponents();
        for(int i = 0; i < comp.length; i++)
        {
            ((ContactButton)comp[i]).deselect();
        }
    }
    //Adds a list of contacts to the list
    //Param is the list of contacts to add
    public void addListOfContacts(ArrayList<Contact> list)
    {
        for(int i = 0; i < list.size(); i++)
        {
            addContact(list.get(i), i==0); // (i==0) is only true for the very first contact, that will be the one to be selected

            repaint();
        }
    }




}
