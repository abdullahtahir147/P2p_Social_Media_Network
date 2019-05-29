package com.example.dropboxtest.Objects;

public class MessageSample {
    private String messageText;
    private String time;
    private String name;

    public MessageSample(String messageText,String time,String name){
        this.messageText=messageText;
        this.name=name;
        this.time=time;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
