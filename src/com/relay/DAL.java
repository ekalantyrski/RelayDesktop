package com.relay;

import java.io.*;
import java.util.ArrayList;

/*
This class is in charge of accessing data in storage


 */

/**
 * Created by erickalantyrski on 2017-05-11.
 */
public class DAL {

    private static final String directory = "Contacts";

    //This class gets a list of contacts that were saved
    //Returns a list of contacts that were saved
    public static ArrayList<Contact> getContacts()
    {
        ArrayList<Contact> contacts = new ArrayList<>();
        File folder = new File(directory);
        if(folder.isDirectory())
        {
            File[] listOfFiles = folder.listFiles(); // gets all files in directory
            for(int i = 0; i < listOfFiles.length; i++)
            {
                if(listOfFiles[i].getName().endsWith(".txt")) // accept only files with txt
                {
                    contacts.add(getContactFromFile(listOfFiles[i]));
                }
            }

        }
        else
        {
            folder.mkdir();
        }


        return contacts;
    }

    //Given a file, it creates a contact object with information inside
    //Param is the file to look at
    //Returns a contact based on info from file
    private static Contact getContactFromFile(File file)
    {
        Contact contact;
        String fname = null;
        String lname = null;
        String phone = null;
        ArrayList<Message> messages = new ArrayList<>();
        boolean userSentMessage;

        try
        {
            BufferedReader br = new BufferedReader(new FileReader(file));
            fname = br.readLine();
            lname = br.readLine();
            phone = br.readLine();
            String message = br.readLine();
            while(message != null)
            {
                userSentMessage = (message.charAt(message.length() -1) == '1') ? true : false; // 1= true, 0 = false
                messages.add(new Message(message.substring(0, message.length() - 1), userSentMessage));
                message = br.readLine();
            }


        }
        catch(FileNotFoundException fnfe)
        {
            System.out.println("Damn Eric, you're dumb");
        }
        catch(IOException ioe)
        {
            System.out.println("Okay, nothing you can do here");
        }
        contact = new Contact(fname, lname, phone, messages);
        return contact;
    }


    //Saves a new contact to disk
    //Param is the contact to save
    public static void addContact(Contact contact)
    {
        String fileName = directory + "/" + contact.getFirstName() + contact.getLastName() + ".txt";
        File file = new File(fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {

        }

        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(file));
            pw.println(contact.getFirstName());
            pw.println(contact.getLastName());
            pw.println(contact.getPhoneNumber());
            pw.close();
        } catch (FileNotFoundException e) {

        }


    }
    //Append a single message to existing contact
    //Param is contact to append to and the message to append
    public static void addMessage(Contact contact, Message message)
    {
        String fileName = directory + "/" + contact.getFirstName() + contact.getLastName() + ".txt";
        File file = new File(fileName);
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(file, true));
            char end = (message.ifUserSentMessage()) ? '1' : '0';
            pw.println(message.getMessage() + end);
            pw.close();
        } catch (FileNotFoundException e) {

        }

    }



}
