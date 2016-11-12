/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Moqsad
 */
public class Data implements Serializable {

    private byte[] buff; //for audio
    private ArrayList<String> available = new ArrayList<String>(); //currently connected to server
    private String text; //for text
    private String from, to, userName;
    private int responseFlag, userNameExistencyFlag; //accepted or denied
    private int type; //1 - userName, 2 - currently connected client, 3 - text message, 4 - send request, 5 - accept or deny request, 6 - audio

    //------------------------------
    //Methods to set data:
    public void setType(int type_) {
        type = type_;
    }

    public void setUserName(String userName_) {
        userName = userName_;
    }

    public void setAvailable(ArrayList<String> available_) {
        available = available_;
    }

    public void setFrom(String from_) {
        from = from_;
    }

    public void setTo(String to_) {
        to = to_;
    }

    public void setResponseFlag(int requestFlag_) {
        responseFlag = requestFlag_;
    }

    public void setText(String text_) {
        text = text_;
    }

    public void setAudioData(byte[] buff_) {
        buff = buff_;
    }
    
    public void setUserNameExistency(int userNameExistencyFlag_)
    {
        userNameExistencyFlag = userNameExistencyFlag_;
    }

    //-------------------------------
    //Get methods to data retrive:
    public int getType() {
        return type;
    }
    
    public String getUserName() {
        return userName;
    }

    public ArrayList<String> getAvailable() {
        return available;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public int getResponseFlag() {
        return responseFlag;
    }

    public String getText() {
        return text;
    }

    public byte[] getAudioData() {
        return buff;
    }
    
    public int getUserNameExistency(){
        return userNameExistencyFlag;
    }
}
