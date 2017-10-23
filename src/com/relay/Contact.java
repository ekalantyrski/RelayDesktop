package com.relay;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by erickalantyrski on 2017-05-11.
 */
public class Contact implements Serializable, Comparable<Contact>{
    private static final long serialVersionUID = 1L;

    private String lastName;
    private String firstName;
    private String phoneNumber;
    private ArrayList<Message> messageList;

    public Contact(String firstName, String lastName, String phoneNumber, Message message)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        messageList = new ArrayList<Message>();
        messageList.add(message);
    }


    public Contact(String firstName, String lastName, String phoneNumber, ArrayList<Message> messages)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.messageList = messages;
    }
    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public Message getFirstMessage()
    {
        return messageList.get(0);
    }
    //Gets all messages associated with contact
    public ArrayList<Message> getMessages()
    {
        return messageList;
    }
    //Add a single message
    public void addMessage(Message message)
    {
        messageList.add(message);
        DAL.addMessage(this, message);
    }
    //Sees if this contact is equal to another
    //Param is other contact
    //Returns true if equal
    public boolean equals(Contact contact)
    {
        if(!contact.getPhoneNumber().equals(phoneNumber))
            return false;
        if(!contact.getFirstName().equals(firstName))
            return false;
        if(!contact.getLastName().equals(lastName))
            return false;
        return true;
    }


    @Override
    //Compares the two contacts based on firstname, then lastname, then phonenumber
    //Param is the other contact
    //Returns in based on their relationship
    public int compareTo(Contact contact) {
        if(firstName.toLowerCase().equals(contact.firstName.toLowerCase()))
        {
            if(lastName.toLowerCase().equals(contact.lastName.toLowerCase()))
            {
                return phoneNumber.compareTo(contact.getPhoneNumber());
            }
            else
            {
                return lastName.toLowerCase().compareTo(contact.lastName.toLowerCase());
            }
        }
        else
        {
            return firstName.toLowerCase().compareTo(contact.firstName.toLowerCase());
        }
    }
}
