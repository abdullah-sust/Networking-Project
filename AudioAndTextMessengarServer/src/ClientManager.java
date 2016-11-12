/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Abdullah Al Awal
 */
public class ClientManager implements Runnable {

    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket connection2;
    private String userName;
    private Server server;

    ClientManager(Socket connection_, Server server_) {
        connection2 = connection_;
        server = server_;
        Thread t = new Thread(this);   //eikhane this dea lagbo......................................
        t.start();
    }

    public void run() {
        try {
            getStreams();
            receiveFromClient();
        } catch (Exception e) {
            System.out.println("Exception in run() method, after calling the processConnection() method");
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    private void getStreams() throws IOException {
        output = new ObjectOutputStream(connection2.getOutputStream());  // server er output na thakle client er input create hoite pare na.......
        output.flush();
        input = new ObjectInputStream(connection2.getInputStream());

        System.out.println("\nGot I/O streams\n");
    }

    public void receiveFromClient() {
        while (true) {
            try {
                Data data = (Data) input.readObject();
                System.out.println("receiveFromClient():");
                if (data.getType() == 1) {
                    userName = data.getUserName();
                    System.out.println("userName: " + userName);
                    server.checkClientName(this, userName);
                }
                else if(data.getType() == 8)
                {
                    server.deleteClient(data.getUserName(), this);
                }
                else{
                    System.out.println("\nIn the else........\n");
                    server.interClientManagement(data);
                }
            } catch (Exception e) {
                System.out.println(e + "\nreceiveFromClient()");
                break;
            }
        }
    }

    public String getUserName() {
        return userName;
    }

    public void sendToThisClient(Data data) {
        try {
            System.out.println("sendToThisClient()\n" + "userName : " + userName);
            System.out.println("availble length: " + data.getAvailable().size());
            output.reset();
            output.writeObject(data);
            output.flush();
        } catch (Exception e) {
            System.out.println(e + "\nException in sendToClient");
        }
    }

    // close streams and socket
    private void closeConnection() {
        try {
            System.out.println(".......C.......L.......O.......S.......E.......D.......");
            output.close(); // close output stream
            input.close(); // close input stream
            connection2.close(); // close socket
        } // end try
        catch (Exception e) { //changed..............................................
            System.out.println("Exception in closeConnection() method, after calling the connection2.close()");
            e.printStackTrace();
        } // end catch
    } // end method closeConnection

}
