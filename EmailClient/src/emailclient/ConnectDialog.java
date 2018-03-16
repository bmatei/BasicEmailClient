package emailclient;

import com.sun.mail.imap.IMAPStore;
import java.awt.*;
import java.awt.event.*;
import java.util.Properties;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.net.ssl.*;
import javax.net.*;
import java.net.Socket;
import java.io.*;
import java.util.Vector;
import javax.swing.*;

/* This class displays a dialog for entering e-mail
   server connection settings. */
public class ConnectDialog extends JDialog {
    
    // Server and username text fields.
    private JTextField serverTextField, usernameTextField;
    
    //port number text field
    private JTextField portTextField;
    
    // Server and username dropboxes
    private JComboBox serverDropBox, usernameDropBox;
    
    // Parent frame (for updating table entries)
    private Frame thisParent;
    
    // Password text field.
    private JPasswordField passwordField;
    
    // Constructor for dialog.
    public ConnectDialog(Frame parent) {
        // Call super constructor, specifying that dialog is modal.
        super(parent, true);
        
        thisParent = parent;
        
        // Set dialog title.
        setTitle("Connect");
        
        // Handle closing events.
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                actionCancel();
            }
        });
        
        //load data for the JComboBoxes
        Credentials.load("offlinedata");
        Vector<String> servers = (Vector<String>)Credentials.loadServers().clone();
        Vector<String> users = (Vector<String>)Credentials.loadUsers().clone();
        Vector<String> pwds = (Vector<String>)Credentials.loadPasswords().clone();
        
        //add default values
        servers.add(0, "");
        users.add(0, "");
        pwds.add(0, "");
        
        serverDropBox = new JComboBox(servers);
        usernameDropBox = new JComboBox(users);
        
        // Setup settings panel.
        JPanel settingsPanel = new JPanel();
        settingsPanel.setBorder(
        BorderFactory.createTitledBorder("Connection Settings"));
        GridBagConstraints constraints;
        GridBagLayout layout = new GridBagLayout();
        settingsPanel.setLayout(layout);
      
        //Server input:
        JLabel serverLabel = new JLabel("Server:");
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.EAST;
        constraints.insets = new Insets(3, 3, 0, 0);
        layout.setConstraints(serverLabel, constraints);
        settingsPanel.add(serverLabel);
        serverTextField = new JTextField(50);
        constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.CENTER;
        constraints.insets = new Insets(3, 3, 0, 3);
        constraints.weightx = 1.0D;
        layout.setConstraints(serverTextField, constraints);
        settingsPanel.add(serverTextField);
        constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.insets = new Insets(3, 3, 0, 3);
        constraints.weightx = 1.0D;
        layout.setConstraints(serverDropBox, constraints);
        settingsPanel.add(serverDropBox);
        
        //Username input:
        JLabel usernameLabel = new JLabel("Username:");
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.EAST;
        constraints.insets = new Insets(3, 3, 0, 0);
        layout.setConstraints(usernameLabel, constraints);
        settingsPanel.add(usernameLabel);
        usernameTextField = new JTextField();
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.CENTER;
        constraints.insets = new Insets(3, 3, 0, 3);
        constraints.weightx = 1.0D;
        layout.setConstraints(usernameTextField, constraints);
        settingsPanel.add(usernameTextField);
        constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.insets = new Insets(3, 3, 0, 3);
        constraints.weightx = 1.0D;
        layout.setConstraints(usernameDropBox, constraints);
        settingsPanel.add(usernameDropBox);
        
        //Password input:
        JLabel passwordLabel = new JLabel("Password:");
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.EAST;
        constraints.insets = new Insets(3, 3, 3, 0);
        layout.setConstraints(passwordLabel, constraints);
        settingsPanel.add(passwordLabel);
        passwordField = new JPasswordField();
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.insets = new Insets(3, 3, 3, 3);
        constraints.weightx = 1.0D;
        layout.setConstraints(passwordField, constraints);
        settingsPanel.add(passwordField);
        
        //Port number input:
        JLabel portLabel = new JLabel("Port:");
        constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.EAST;
        constraints.insets = new Insets(3, 3, 0, 0);
        constraints.weightx = 1.0D;
        constraints = new GridBagConstraints();
        layout.setConstraints(portLabel, constraints);
        settingsPanel.add(portLabel);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.insets = new Insets(3, 3, 0, 3);
        constraints.weightx = 1.0D;
        portTextField = new JTextField(6);
        layout.setConstraints(portTextField, constraints);
        settingsPanel.add(portTextField);
        
        //link the input text to the DropBox content
        serverDropBox.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                serverTextField.setText(
                    serverDropBox.getSelectedItem().toString()
                );
            }
        });
        usernameDropBox.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                usernameTextField.setText(
                    usernameDropBox.getSelectedItem().toString()
                );
                passwordField.setText(pwds.elementAt(
                        usernameDropBox.getSelectedIndex()
                ));
            }
        });
        
       
        // Setup buttons panel.
        JPanel buttonsPanel = new JPanel();
        JButton connectButton = new JButton("Connect");
        connectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionConnect();
            }
        });
        buttonsPanel.add(connectButton);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionCancel();
            }
        });
        buttonsPanel.add(cancelButton);
        
        // Add panels to display.
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(settingsPanel, BorderLayout.CENTER);
        getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
        
        // Size dialog to components.
        pack();
        
        // Center dialog over application.
        setLocationRelativeTo(parent);
    }
    
    // Validate connection settings and close dialog.
    private void actionConnect() {
        if (serverTextField.getText().trim().length() < 1
                || usernameTextField.getText().trim().length() < 1
                || passwordField.getPassword().length < 1
                || portTextField.getText().trim().length() < 1
            ) {
            JOptionPane.showMessageDialog(this,
                    "One or more settings is missing.",
                    "Missing Setting(s)", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String host = serverTextField.getText();
        String user = usernameTextField.getText();
        String pass = charArrToString(passwordField.getPassword());
        String port = portTextField.getText();
        
        int connected = 1;
        try{
            connectToServer(host, user, pass, port);
        } catch(Exception e){
            e.printStackTrace();
            connected = 0;
        }
        if(connected == 1){
            Credentials.saveInfo("offlinedata", host, user, pass);
        }
        
        // Close dialog.
        dispose();
    }
    
    
    //does the actual connection
    private void connectToServer(String host, String user, String pass, String port) throws MessagingException{
        
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps"); //this protocol is already secure
        properties.put("mail.imaps.port", port); //sets the port
        properties.put("mail.imaps.starttls.enable", "true"); //enable starttls for servers who don't know imaps but know imap
        properties.put("mail.imaps.ssl.enable", "true"); //enable ssl for servers who don't understand TLS
        Session emailSession = Session.getDefaultInstance(properties);
        // create the IMAP3 store object and connect with the pop server
        Store store = emailSession.getStore("imaps");
        store.connect(host, user, pass);
        IMAPStore imapStore = (IMAPStore) store;
        //get folder
        Folder emailFolder = imapStore.getFolder("INBOX");
        emailFolder.open(Folder.READ_ONLY);
        ((ClientGUI)thisParent).getTableModel().setMessages(emailFolder.getMessages());
        ((ClientGUI)thisParent).updateAll(user);
    }
    
    //converts char[] to String
    private String charArrToString(char []arg){
        String rez = "";
        
        for(int i = 0, n = arg.length; i < n; ++i){
            rez += arg[i];
        }
        
        return rez;
    }
    
    //for debugging
    private void printAll(String a, String b, String c){
        System.out.println("Server: " + a);
        System.out.println("Username: " + b);
        System.out.println("Password: " + c);
    }
    
    // Close the dialog
    private void actionCancel() {
        dispose();
    }
    
    // Get e-mail server.
    public String getServer() {
        return serverTextField.getText();
    }
    
    // Get e-mail username.
    public String getUsername() {
        return usernameTextField.getText();
    }
    
    // Get e-mail password.
    public String getPassword() {
        return new String(passwordField.getPassword());
    }
  
    
    //main for testing
    public static void main(String[] args){
        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        ConnectDialog connDiag = new ConnectDialog(frame);
        connDiag.show();
        
        //make it visible enough
        frame.setBounds(0, 0, 100, 100);
    }
}