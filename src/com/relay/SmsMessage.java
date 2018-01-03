package com.relay;

import java.io.Serializable;

public class SmsMessage extends LocalMessage implements Serializable {

    private static final long serialVersionUID = 3L;
    private Contact contact;

    public SmsMessage(Contact contact)
    {
        super(MessageType.SMS);
        this.contact = contact;
    }

    /**
     * Get the message
     */
    public Message getMessage()
    {
        return contact.getFirstMessage();
    }

    public String getPhoneNumber()
    {
        return contact.getPhoneNumber();
    }

    public Contact getContact()
    {
        return contact;
    }
}
