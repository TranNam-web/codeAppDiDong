package com.example.doandidong.model;

import java.util.Date;

public class ChatMessage {
    public String sendid;
    public String receivedid;
    public String mess;
    public Date dateObj;          // kiểu Date, không phải String
    public String datetime;       // dùng để hiển thị đã format
}
