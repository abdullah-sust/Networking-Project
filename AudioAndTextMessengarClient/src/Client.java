/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;

/**
 *
 * @author Moqsad
 */
public class Client extends javax.swing.JFrame {

    Client client;
    AudioMethods audioMethods;

    Socket clientConnection;

    ObjectInputStream fromServer;
    ObjectOutputStream toServer;

    String userName;
    String serverIP, serverPort;

    public Client(Socket connection, String userName_) {
        initComponents();
        try {
            userName = userName_;
            audioMethods = new AudioMethods(this, availableUserList, displayArea, callButton, userName, jScrollPane1);
            processClient(connection);
        } catch (Exception e) {
            System.out.println(e + "\nException in Server Constructor");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        clientPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        displayArea = new javax.swing.JTextArea();
        sendField = new javax.swing.JTextField();
        callButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        availableUserList = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        clientPanel.setBackground(new java.awt.Color(255, 255, 204));
        clientPanel.setPreferredSize(new java.awt.Dimension(550, 500));
        clientPanel.setLayout(null);

        displayArea.setColumns(20);
        displayArea.setRows(5);
        jScrollPane1.setViewportView(displayArea);

        clientPanel.add(jScrollPane1);
        jScrollPane1.setBounds(30, 160, 300, 210);

        sendField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendFieldActionPerformed(evt);
            }
        });
        clientPanel.add(sendField);
        sendField.setBounds(30, 430, 300, 30);

        callButton.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        callButton.setText("Call");
        callButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                callButtonActionPerformed(evt);
            }
        });
        clientPanel.add(callButton);
        callButton.setBounds(390, 310, 100, 23);

        jLabel1.setFont(new java.awt.Font("Tahoma", 3, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 255));
        jLabel1.setText("Audio Streaming");
        clientPanel.add(jLabel1);
        jLabel1.setBounds(200, 60, 160, 22);

        jLabel2.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel2.setText("All messages:");
        clientPanel.add(jLabel2);
        jLabel2.setBounds(30, 120, 100, 30);

        jLabel3.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel3.setText("Available Users:");
        clientPanel.add(jLabel3);
        jLabel3.setBounds(380, 130, 110, 17);

        jLabel4.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel4.setText("Messages to send: ");
        clientPanel.add(jLabel4);
        jLabel4.setBounds(30, 400, 140, 20);

        availableUserList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane5.setViewportView(availableUserList);

        clientPanel.add(jScrollPane5);
        jScrollPane5.setBounds(360, 160, 160, 130);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(clientPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(clientPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    void processClient(Socket connection) {
        try {
            clientConnection = connection;
            fromServer = new ObjectInputStream(clientConnection.getInputStream());
            toServer = new ObjectOutputStream(clientConnection.getOutputStream());
            toServer.flush();
            understandTheData();
        } catch (IOException e) {
            System.out.println(e + "\nException in processServer()");
        }
    }

    void setUserName(String userName_) {
        userName = userName_;
    }

    void sendUserName() {
        System.out.println("sendUserName():");
        System.out.println("userName: " + userName);
        Data data = new Data();
        data.setType(1);
        data.setUserName(userName);
        sendToServer(data);
    }

    void receiveUserNameExistency(int existencyFlag) {
        System.out.println("receiveUserNameExistency()\nexistencyFlag: " + existencyFlag);
        if (existencyFlag == 0) {
            JOptionPane.showMessageDialog(null, "This user name is already exit!!!, please change it.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            System.out.println("kemne ki????????");
            new Thread() {
                public void run() {
                    try {
                        ConnectToServer.connectToServer.getContentPane().removeAll();
                        ConnectToServer.connectToServer.getContentPane().add(clientPanel);
                        ConnectToServer.connectToServer.repaint();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }.start();
        }
    }

    synchronized void sendToServer(Data data) {
        System.out.println("sendToServer():");
        try {
            toServer.reset();
            toServer.writeObject(data);
            toServer.flush();
        } catch (Exception e) {
            System.out.println(e + "\nException insendToServer()");
        }
    }

    void understandTheData() {
        System.out.println("understandTheData():");
        new Thread(new Runnable() {
            public void run() {
                try {
                    while (true) {
                        Data data = (Data) fromServer.readObject();
                        int type = data.getType();
                        if (type == 2) {
                            updateAvailableUsers(data.getAvailable());
                        } else if (type == 3) {
                            audioMethods.display("\n" + data.getFrom() + ": " + data.getText() + "\n");
                        } else if (type == 4) {
                            audioMethods.audioRequestReceived(data.getFrom());
                        } else if (type == 5) {
                            audioMethods.audioResponseReceived(data.getResponseFlag());
                        } else if (type == 6) {
                            audioMethods.playAudio(data.getAudioData());
                        } else if (type == 7) {
                            receiveUserNameExistency(data.getUserNameExistency());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e + "\nException in understandData()");
                }
            }
        }).start();
    }

    private void updateAvailableUsers(ArrayList<String> available) {
        int sz = available.size();
        String[] names = new String[sz];
        int i = 0;
        for (String s : available) {
            if (s.equals(userName)) {
                names[i++] = "Me";
            } else {
                names[i++] = s;
            }
        }
        availableUserList.setListData(names);//tmep....................................
    }

    private void callButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_callButtonActionPerformed
        Object obj[] = availableUserList.getSelectedValues();
        for (int i = 0; i < obj.length; i++) {
            System.out.println((String) obj[i]);
        }

        if (callButton.getText().equals("Call")) {
            if (availableUserList.isSelectionEmpty()) {
                JOptionPane.showMessageDialog(null, "You have to select atleast one available user.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                audioMethods.display("\n\nYour request is on the process.............");
                audioMethods.sendAudioRequest();
            }
        } else {
            audioMethods.conversationStopping();
        }
    }//GEN-LAST:event_callButtonActionPerformed

    private void sendFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendFieldActionPerformed
        if (sendField.getText().equals("")) {
            System.out.println("Vai hudai enter chapen ken???");
        } else if (availableUserList.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(null, "You have to select atleast one available user.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            sendText();
        }
    }//GEN-LAST:event_sendFieldActionPerformed

    void deleteTheConnection() {
        try {
            Data data = new Data();
            data.setType(8);
            data.setUserName(userName);
            sendToServer(data);
        } catch (Exception e) {
            System.out.println("Exception in deleteTheConnection()");
            e.printStackTrace();
        }
    }

    private void sendText() {
        Data data = new Data();
        data.setType(3);
        data.setFrom(userName);
        data.setTo(availableUserList.getSelectedValue().toString());
        data.setText(sendField.getText());
        sendToServer(data);

        audioMethods.display("\nMe : " + sendField.getText() + "\n");
        sendField.setText("");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList availableUserList;
    private javax.swing.JButton callButton;
    public static javax.swing.JPanel clientPanel;
    private javax.swing.JTextArea displayArea;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTextField sendField;
    // End of variables declaration//GEN-END:variables
}
