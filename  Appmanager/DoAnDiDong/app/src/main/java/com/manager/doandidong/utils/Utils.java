package com.manager.doandidong.utils;

import com.manager.doandidong.model.GioHang;
import com.manager.doandidong.model.User;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static final String BASE_URL="http://192.168.1.3/banhang/";//ipconfig getifaddr en0
    public static List <GioHang> manggiohang;
    public static List <GioHang> mangmuahang =  new ArrayList<>();

    public static User user_current = new User();
    public static  String ID_RECEIVED;
    public static final String SENDID = "idsend";
    public static final String RECEIVEDID = "idreceived";
    public static final String MESS = "message";
    public static final String DATETIME = "datetime";
    public static final String PATH_CHAT = "chat";



}
