package emailclient;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import javax.mail.Message;
import javax.mail.Address;
import java.util.Date;

public class CustomTableModel extends AbstractTableModel {
    
    //collumn names for our table
    private final String[] collumns = {
        "Sender", "Subject", "Date"
    };
    
    //email list
    private ArrayList messageList = new ArrayList();
    
    //setter for the email list
    public void setMessages(Message[] arg){
        messageList = new ArrayList();
        for(int i = arg.length-1; i >= 0; --i){
            messageList.add(arg[i]);
        }
        
        fireTableDataChanged();
    }
    
    //get a particular message
    public Message getMessage(int row){
        return (Message) messageList.get(row);
    }
    
    //how many collumns?
    @Override
    public int getColumnCount(){
        return 3;
    }
    
    //how many rows?
    @Override
    public int getRowCount(){
        return messageList.size();
    }
    
    //what collumn is this?
    @Override
    public String getColumnName(int col){
        return collumns[col];
    }
    
    //what data does this cell hold?
    @Override
    public Object getValueAt(int row, int col){
        try {
            Message message = (Message) messageList.get(row);
            switch (col) {
                case 0: // Sender
                    Address[] senders = message.getFrom();
                    if (senders != null || senders.length > 0) {
                        return senders[0].toString();
                    } else {
                        return "[none]";
                    }
                case 1: // Subject
                    String subject = message.getSubject();
                    if (subject != null && subject.length() > 0) {
                        return subject;
                    } else {
                        return "[none]";
                    }
                case 2: // Date
                    Date date = message.getSentDate();
                    if (date != null) {
                        return date.toString();
                    } else {
                        return "[none]";
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        
        return "";
    }
        
}
