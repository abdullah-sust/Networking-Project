
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Moqsad
 */
public class AudioMethods {

    Client client;

    JList availableUserList;
    JTextArea displayArea;
    JButton callButton;
    JScrollPane jScrollPane1;

    AudioFormat format;

    DataLine.Info targetInfo;
    DataLine.Info sourceInfo;
    TargetDataLine targetLine;
    SourceDataLine sourceLine;

    Thread receive, send;

    String userName;

    String audioTo;

    boolean audioFlag = false;

    int dataLength = 1200;

    AudioMethods(Client client_, JList availableUserList_, JTextArea displayArea_, JButton callButton_, String userName_, JScrollPane jScrollPane1_) {
        client = client_;
        availableUserList = availableUserList_;
        displayArea = displayArea_;
        callButton = callButton_;
        userName = userName_;
        jScrollPane1 = jScrollPane1_;
        initAudio();
    }

    void initAudio() {
        try {
            format = new AudioFormat(11025f, 8, 1, true, true);

            targetInfo = new DataLine.Info(TargetDataLine.class, format);
            sourceInfo = new DataLine.Info(SourceDataLine.class, format);

            targetLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
            targetLine.open(format);
            targetLine.start();

            sourceLine = (SourceDataLine) AudioSystem.getLine(sourceInfo);
            sourceLine.open(format);
            sourceLine.start();

        } catch (LineUnavailableException e) {
            System.out.println(e + "\nException in initAudio");
        }
    }

    void sendAudioRequest() {
        audioTo = availableUserList.getSelectedValue().toString();
        System.out.println("sendAudioRequest()\naudioTo: " + audioTo);
        Data data = new Data();
        data.setType(4);
        data.setFrom(userName);
        data.setTo(audioTo);
        client.sendToServer(data);
    }

    void conversationAccepting(String to) {
        audioTo = to;
        sendAudioResponse(1); //We have to must call the sendAudioResponse() after assigning the to's value in audioTo
        System.out.println("conversationAccepting()\nFrom: " + userName + "\nTo: " + to);
        displayInAConversation();
        callButton.setText("Stop");
        audioFlag = true;
        audioRecord();
    }

    void conversationStopping() {
        sendAudioResponse(0);
        displayStoppedByYourself();
        callButton.setText("Call");
        audioFlag = false;
    }

    void conversationDenying(String to) { //In this case we have to store the to's value in audio to
        String temp = audioTo;
        audioTo = to;
        sendAudioResponse(0);
        displayDeniedByYourself(to);
        audioTo = temp;
    }

    void sendAudioResponse(int responseFlag_) {
        System.out.println("send AudioResponse().......\naudioTo: " + audioTo);

        Data data = new Data();
        data.setType(5);
        data.setFrom(userName);
        data.setTo(audioTo);
        data.setResponseFlag(responseFlag_);
        client.sendToServer(data);
    }

    void audioRequestReceived(String to) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int yes = 1 - JOptionPane.showConfirmDialog(null, "Do you want to audio chat with " + to, "Live Audio Stream Request", JOptionPane.YES_NO_OPTION);

                System.out.println("audioRequestReceived()\nyes : " + yes);

                if (callButton.getText().equals("Call")) {
                    if (yes == 1) {
                        conversationAccepting(to); //To whom i am accepting is not in the audioTo variable...........
                    } else {
                        conversationDenying(to); //The people whom i have denied, is not in the audioTo variable.............
                    }
                } else {
                    if (yes == 1) {
                        conversationStopping();
                        conversationAccepting(to); //To whom i am accepting is not in the audioTo variable...........
                    } else {
                        conversationDenying(to); //The people whom i have denied, is not in the audioTo variable.............
                    }
                }
            }
        }).start();
    }

    void audioResponseReceived(int response) {
        if (callButton.getText().equals("Call")) {
            if (response == 1) {
                displayAccepted();
                callButton.setText("Stop");
                audioFlag = true;
                audioRecord();
            } else {
                displayDeniedByOtherSide();
            }
        } else {
            displayStoppedByOtherSide();
            callButton.setText("Call");
            audioFlag = false;
        }
    }

    void audioRecord() {
        send = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (audioFlag == true) {
                        //System.out.println("Currently available to mic : " + targetLine.available());
                        if (targetLine.available() > dataLength) {
                            byte[] buff = new byte[dataLength];
                            while (targetLine.available() >= dataLength) { //flush old data from mic to reduce lag, and read most recent data
                                targetLine.read(buff, 0, buff.length); //read from microphone
                            }
                            sendAudio(buff);
                        } else {
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                System.out.println(e + "\nException in audioReceiveAndPlay()");
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e + "\nException in audioReceiveAndPlay()");
                }
            }
        });
        send.start();
    }

    void sendAudio(byte[] buff) {
        System.out.println("Sending Audio.......");
        Data data = new Data();
        data.setType(6);
        data.setAudioData(buff);
        data.setFrom(userName);
        data.setTo(audioTo);
        client.sendToServer(data);
    }

    void playAudio(byte[] buff) {
        System.out.println("playAudio()\nuserName: " + userName);
        sourceLine.write(buff, 0, dataLength);
    }

    void displayInAConversation() {
        display("\n-----------------------------------------------------------\n" + "In a audio conversation with: " + audioTo
                + ".\n-----------------------------------------------------------\n");
    }

    void displayAccepted() {
        display("\n-----------------------------------------------------------\n" + audioTo
                + " have accepted your audio request!!!\nHave a nice audio conversation!!!!"
                + "\n-----------------------------------------------------------\n");
    }

    void displayStoppedByYourself() {
        display("\n-----------------------------------------------------------\nAudio conversation stopped by yourself."
                + "\n-----------------------------------------------------------\n");
    }

    void displayStoppedByOtherSide() {
        display("\n-----------------------------------------------------------\nAudio conversation stopped by: " + audioTo
                + ".\n-----------------------------------------------------------\n");
    }

    void displayDeniedByYourself(String to) {
        display("\n-----------------------------------------------------------\nYou denied a audio request from: " + to
                + ".\n-----------------------------------------------------------\n");
    }

    void displayDeniedByOtherSide() {
        display("\n-----------------------------------------------------------\n" + audioTo + " denied your audio request :( :("
                + "\n-----------------------------------------------------------\n");
    }

    void display(String s) {
        displayArea.append(s);
        JScrollBar vertical = jScrollPane1.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }

}
