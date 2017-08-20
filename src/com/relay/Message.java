package com.relay;

import java.io.Serializable;

/**
 * Created by erickalantyrski on 2017-06-06.
 */

/*

Holds info on a message
 */


public class Message implements Serializable{
    private static final long serialVersionUID = 2L;
    private boolean messageSent; // delivered successfully
    private boolean userSentMessage; // received or sent
    private String message;

    public Message(String message) {
        this.message = message;
    }

    public Message(String message, boolean userSentMessage)
    {
        this.message = message;
        this.userSentMessage = userSentMessage;
    }

    public String getMessage()
    {
        return message;
    }

    public boolean ifUserSentMessage()
    {
        return userSentMessage;
    }
}
