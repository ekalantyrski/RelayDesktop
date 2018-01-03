package com.relay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/*
Contains most of the code that runs the program. Creates screen and all its components, as well as controls all io (network,text, mouse)
 */

/**
 * Created by erickalantyrski on 2017-05-11.
 */
public class Screen implements ActionListener{
    public static final Font font = new Font("Arial", Font.PLAIN, 12);
    private JFrame jframe;
    private Network n;
    private MessagesPane messagePane;
    private ArrayList<Contact> contactList;
    private Contact selectedContact = null;
    private ContactListPane contactListPane;
    private JSplitPane splitPane;
    private JScrollPane scrollPane;
    private Action keyboardAction;
    private boolean running;
    private JMenuBar menuBar;
    private String password;
    private ConnectionPane connectionPane;


    public Screen()
    {

        contactList = DAL.getContacts();

        jframe = new JFrame();
        jframe.setFont(font);
        jframe.setSize(640, 450);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setResizable(false);

        keyboardAction = new AbstractAction() { // Listens for enter keypress from textfield

            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() instanceof JTextArea)
                {
                    JTextArea area = (JTextArea)e.getSource();
                    String input = area.getText(); //the text inputted
                    Message m = new Message(input, true);
                    Contact c = new Contact(selectedContact.getFirstName(), selectedContact.getLastName(), selectedContact.getPhoneNumber(), m);
                    SmsMessage message = new SmsMessage(c);
                    addReceivedMessage(message);
                    messagePane.update();
                    if((selectedContact != null && n != null) && n.isOpen())
                    {

                        sendText(message);

                    }
                    jframe.revalidate();
                    area.setText("");
                }
            }
        };

        messagePane = new MessagesPane(keyboardAction);

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
        password = checkPassword();
        connectionPane = new ConnectionPane(password);
        splitPane.setRightComponent(connectionPane);
        splitPane.setLeftComponent(scrollPane);
        splitPane.setDividerSize(1);
        splitPane.setEnabled(false);


        jframe.add(splitPane);

        jframe.pack();
        jframe.setVisible(true);


        running = true;
        while(running) //if connection is lost, will listen for a new connection
        {
            n = new Network(password);
            System.out.println("Done searching for connection");
            splitPane.setRightComponent(messagePane);
            splitPane.validate();
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
        boolean continueListening = true;
        while(continueListening)
        {
            LocalMessage input = null;
            input = n.getNextLocalMessage();
            if (input != null) {
                if(input.getMessageType() == MessageType.SMS)
                {
                    addReceivedMessage((SmsMessage) input);
                    input = null;
                }

            } else if (!n.isOpen()) {
                continueListening = false;
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
    public void sendText(SmsMessage message)
    {
        n.send(message);
    }
    //This method adds the message to proper area
    // It will either add to current view, create a new contact and add that contact to contactList, or add message to a contact not in view
    //Param is the contact to add
    public void addReceivedMessage(SmsMessage smsMessage)
    {
        System.out.println("Added message");
        Contact contact = smsMessage.getContact();
        Message message = smsMessage.getMessage();
        if(selectedContact != null && contact.equals(selectedContact)) // if current contact gets a message, adds a message
        {
            selectedContact.addMessage(message);
            messagePane.addMessage(message);
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
                    DAL.addMessage(contact, message);


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
                    DAL.addMessage(contact, message);
                    splitPane.setLeftComponent(scrollPane);
                    splitPane.repaint();
                }
            }
            else // if message received is for an existing contact, that is not selected
            {
                contactList.get(contactIndex).addMessage(message);
            }
        }
    }


    public String checkPassword()
    {

        //TODO Create a new pane that will be put into the splitpane, with more detailed instructions on connection to phone
        String password = DAL.getPassword();
        if(password == null) // then no password stored
        {
            password = generatePassword();
            DAL.setPassword(password);
        }
        return password;
    }

    /**
     * Creates a 10 character random password of lower and capital case letters
     * 52^10 possibiltiies
     *
     * @return String
     */
    private String generatePassword()
    {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        int n;
        for(int i = 0; i < 10; i++)
        {
            n = rand.nextInt(52);
            if(n < 26) // then upper case char
            {
                sb.append((char)(n + 65));
            }
            else // lower case char
            {
                sb.append((char)(n + 71));
            }
        }
        return sb.toString();
    }


}
