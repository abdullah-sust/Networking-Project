/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.InetAddress;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author Moqsad
 */
public class ConnectToServer extends javax.swing.JFrame {

    public static ConnectToServer connectToServer;
    public static Socket clientConnection;
    Client client;
    boolean forTheFirstTime = true;

    /**
     * Creates new form ConnectToServer
     */
    public ConnectToServer() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        ipField = new javax.swing.JTextField();
        portField = new javax.swing.JTextField();
        serverConnectionButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));
        jPanel1.setPreferredSize(new java.awt.Dimension(550, 500));
        jPanel1.setLayout(null);
        jPanel1.add(ipField);
        ipField.setBounds(320, 160, 120, 30);
        jPanel1.add(portField);
        portField.setBounds(320, 210, 120, 30);

        serverConnectionButton.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        serverConnectionButton.setText("Connect");
        serverConnectionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serverConnectionButtonActionPerformed(evt);
            }
        });
        jPanel1.add(serverConnectionButton);
        serverConnectionButton.setBounds(220, 320, 100, 23);

        jLabel1.setFont(new java.awt.Font("Tahoma", 3, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 255));
        jLabel1.setText("Audio Streaming");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(200, 50, 160, 22);
        jPanel1.add(nameField);
        nameField.setBounds(320, 260, 120, 30);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("*");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(440, 200, 10, 17);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("*");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(440, 250, 10, 17);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("*");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(440, 150, 10, 17);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setText("Server IP Address : ");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(140, 160, 130, 30);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel6.setText("Server Port Address : ");
        jPanel1.add(jLabel6);
        jLabel6.setBounds(140, 210, 130, 30);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel7.setText("Username : ");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(140, 254, 100, 30);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void serverConnectionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_serverConnectionButtonActionPerformed
        // TODO add your handling code here:
        try {

            new Thread() {
                public void run() {
                    try {
                        if (forTheFirstTime == true) {
                            System.out.println(ipField.getText() + " " + portField.getText());
                            clientConnection = new Socket(InetAddress.getByName(ipField.getText()), Integer.parseInt(portField.getText()));
                            client = new Client(clientConnection, nameField.getText());
                            client.sendUserName();
                            forTheFirstTime = false;
                        } else {
                            System.out.println("Not the first time()");
                            client.setUserName(nameField.getText());
                            client.sendUserName();
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "IP Address or Port Address is wrong", "Error", JOptionPane.ERROR_MESSAGE);
                        //ex.printStackTrace();
                    }
                }
            }.start();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ip or port address is invalid.", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception in serverConnectionButtonActionPerformed");
        }
    }//GEN-LAST:event_serverConnectionButtonActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        try
        {
        System.out.println("amare bondho koira dilo...... :( :(");
        client.deleteTheConnection();            
        }
        catch(Exception e)
        {
            System.out.println("Exception in formWindowClosing()");
            e.printStackTrace();
        }
    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ConnectToServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ConnectToServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ConnectToServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ConnectToServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                connectToServer = new ConnectToServer();
                connectToServer.setLocationRelativeTo(null);
                connectToServer.setVisible(true);
                connectToServer.setResizable(false);
                connectToServer.setSize(550, 500);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField ipField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField nameField;
    private javax.swing.JTextField portField;
    private javax.swing.JButton serverConnectionButton;
    // End of variables declaration//GEN-END:variables
}
