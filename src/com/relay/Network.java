package com.relay;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by erickalantyrski on 2017-05-08.
 */

/*
This class is in charge of communication between phone and computer
 */
public class Network implements Runnable{
    private ServerSocket serverSocket;
    private Socket connection;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private final static int port = 43567;
    private Queue<Contact> messageList;
    private String password;
    private boolean running;
    private boolean open = false;


    public Network(String password)
    {
        messageList = new LinkedList<>();
        try
        {
            this.serverSocket = new ServerSocket(port);
        }
        catch(IOException ioe)
        {
            System.out.println("Error in creating server socket.");
        }
        this.password = password;
        try
        {
            boolean goodPassword = false;
            while(!goodPassword)
            {
                System.out.println("Waiting for a connection");
                connection = serverSocket.accept(); // waits for connection from phone
                System.out.println("Accepted");
                BufferedReader initialInput = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String input = initialInput.readLine(); // reads password given
                if (input.equals(password))
                {
                    goodPassword = true;
                    PrintWriter pw = new PrintWriter(connection.getOutputStream(), true);
                    pw.println("true");
                    System.out.println("Password Accepted");
                    in = new ObjectInputStream(connection.getInputStream()); // input of objects
                    out = new ObjectOutputStream(connection.getOutputStream()); // output of objects
                }
                else
                {
                    System.out.println("Rejected");
                }
            }
        }
        catch(IOException ioe)
        {
            System.out.println("Error in accepting connection.");
        }
        open = true;






    }
    //Sets the password to a given password
    //Param is the password to set to
    public void setPassword(String password)
    {
        this.password = password;
    }

    @Override
    //The network loop that listens for input, if there is input, adds that to a queue that is then polled for the message
    public void run() {
        running = true;
        Contact input = null;
        while (running) {
            if (open) {
                try {
                    input = (Contact) in.readObject(); // waits for input
                    messageList.add(input);
                    input = null;
                } catch (ClassNotFoundException cnfe) {
                    System.out.println("Class not found exception. (Somehow a contact was not received)");
                    open = false;
                    running = false;
                    break;
                } catch (IOException ioe) {
                    System.out.println("Connection lost");
                    open = false;
                    running = false;
                    break;
                }
            }


        }
        try
        {
            serverSocket.close();
        } catch (IOException e)
        {
            System.out.println("Could not close server socket after connection loss.");
        }

    }
    //stops the network
    public void stop()
    {
        running = false;
    }
    //Returns the contact at head of queue
    //Return is the contact at head
    public Contact getNextContact()
    {
        return messageList.poll();
    }
    //Sends a contact over the network to the phone
    //Param is the contact to send
    public void send(Contact contact)
    {
        try
        {
            out.writeObject(contact);
        }
        catch(IOException e)
        {
            System.out.println("Was not able to send object.");
        }
    }
    //Returns if network is open (can stuff be sent)
    //Returns boolean based on if it is open or not
    public boolean isOpen()
    {
        return open;
    }

}
