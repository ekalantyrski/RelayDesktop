package com.relay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

/*
Contains most of the code that runs the program. Creates screen and all its components, as well as controls all io (network,text, mouse)
 */

/**
 * Created by erickalantyrski on 2017-05-11.
 */
public class Screen implements ActionListener{
    private JFrame jframe;
    private Network n;
    private MessagePane messagePane;
    private ArrayList<Contact> contactList;
    private Contact selectedContact = null;
    private ContactListPane contactListPane;
    private JSplitPane splitPane;
    private JScrollPane scrollPane;
    private Action keyboardAction;
    private boolean running;


    public Screen()
    {

        contactList = DAL.getContacts();


        jframe = new JFrame();
        jframe.setSize(640, 450);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setResizable(false);

        keyboardAction = new AbstractAction() { // Listens for enter keypress from textfield

            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() instanceof JTextArea)
                {
                    System.out.println("enter");
                    JTextArea area = (JTextArea)e.getSource();
                    String input = area.getText(); //the text inputted
                    addReceivedMessage(new Contact(selectedContact.getFirstName(), selectedContact.getLastName(), selectedContact.getPhoneNumber(), new Message(input, true)));
                    messagePane.update();
                    if((selectedContact != null && n != null) && n.isOpen())
                    {
                        Contact contact = new Contact(selectedContact.getFirstName(), // creates object to be sent to phone
                                selectedContact.getLastName(),
                                selectedContact.getPhoneNumber(),
                                new Message(input));
                        sendText(contact);

                    }
                    jframe.revalidate();
                    area.setText("");
                }
            }
        };


//        ActionMap am = new ActionMap();
//        am.put("Enter", keyboardAction);
        messagePane = new MessagePane(keyboardAction);

        contactListPane = new ContactListPane(this); // contacts
        contactListPane.addListOfContacts(contactList);
        scrollPane = new JScrollPane(contactListPane); //scroll thingy
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // removes bottom scroll

        if(contactList.size() > 0) // if contacts were saved on disk, add all of them to the list
        {
            selectedContact = contactList.get(0);
            ArrayList<Message> messageList = selectedContact.getMessages();
            for(int i = 0; i < messageList.size(); i++)
            {
                messagePane.addMessage(messageList.get(i));
            }
            messagePane.update();
        }


        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setPreferredSize(new Dimension(640, 450));
        splitPane.setDividerLocation(0.3);
        splitPane.setResizeWeight(0);
        splitPane.setRightComponent(messagePane);
        splitPane.setLeftComponent(scrollPane);
        splitPane.setDividerSize(1);
        splitPane.setEnabled(false);


        jframe.add(splitPane);

        jframe.pack();
        jframe.setVisible(true);


        running = true;
        while(running) //if connection is lost, will listen for a new connection
        {
            n = new Network("1234");
            System.out.println("Done");
            (new Thread(n)).start();
            listenNetwork();
        }






    }

    @Override
    //Listens for mouseclicks on the contactbuttons
    //If one was clicked, it changes perspective to the one clicked
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() instanceof ContactButton)
        {
            ContactButton c;

            c = ((ContactButton)e.getSource());
            if(!c.getContact().equals(selectedContact))
            {
                contactListPane.deselectAll();
                c.select();
                selectedContact = c.getContact();
                messagePane.switchContact(selectedContact);
                contactListPane.repaint();
                messagePane.update();
                messagePane.revalidate();
            }


        }
    }
    //Polls the network every 10ms
    //If there is input, then it adds the received mesage to proper area
    private void listenNetwork()
    {
        while(true)
        {
            Contact input = null;
            input = n.getNextContact();
            if(input != null)
            {
                addReceivedMessage(input);
                input = null;

            }
            else if(!n.isOpen())
            {
                break;
            }
            try
            {
                Thread.sleep(10);
            }catch(InterruptedException ie)
            {

            }


        }

    }

    //sends a text over network to phone
    //Param is the object to be sent
    public void sendText(Contact contact)
    {
        n.send(contact);
    }
    //This method adds the message to proper area
    // It will either add to current view, create a new contact and add that contact to contactList, or add message to a contact not in view
    //Param is the contact to add
    public void addReceivedMessage(Contact contact)
    {
        System.out.println("Added message");
        if(selectedContact != null && contact.equals(selectedContact)) // if current contact gets a message, adds a message
        {
            selectedContact.addMessage(contact.getFirstMessage());
            //messagePane.addMessage(contact.getFirstMessage());
            messagePane.update();
            jframe.revalidate();
        }
        else
        {
            int contactIndex = Collections.binarySearch(contactList, contact);
            if(contactIndex < 0) // if message received is for a new contact, add the contact to list
            {
                int contactPosition = -(contactIndex + 1);
                contactList.add(contactPosition, contact);
                if(contactList.size() == 1) //only applies if screen was completely blank when app started
                {
                    selectedContact = contact;
                    DAL.addContact(contact);
                    DAL.addMessage(contact, contact.getFirstMessage());


                    contactListPane.addContact(contact, true);
                    messagePane.switchContact(contact);

                    splitPane.setRightComponent(messagePane);
                    splitPane.setLeftComponent(scrollPane);
                    splitPane.repaint();

                }
                else // if not first, to where the contact should be based on alphabetical order
                {
                    contactListPane.addContact(contact, contactPosition);
                    DAL.addContact(contact);
                    DAL.addMessage(contact, contact.getFirstMessage());
                    splitPane.setLeftComponent(scrollPane);
                    splitPane.repaint();
                }
            }
            else // if message received is for an existing contact, that is not selected
            {
                contactList.get(contactIndex).addMessage(contact.getFirstMessage());
            }
        }
    }


}
