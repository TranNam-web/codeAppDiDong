package com.example.doandidong.utils;

import com.example.doandidong.model.GioHang;
import com.example.doandidong.model.User;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static final String BASE_URL="http://192.168.1.3/banhang/";//ipconfig getifaddr en0
    public static List <GioHang> manggiohang;
    public static String tokenSend;
    public static List <GioHang> mangmuahang =  new ArrayList<>();
    public static User user_current = new User();
    public static  String ID_RECEIVED;
    public static final String SENDID = "idsend";
    public static final String RECEIVEDID = "idreceived";
    public static final String MESS = "message";
    public static final String DATETIME = "datetime";
    public static final String PATH_CHAT = "chat";
    public static String statusOrder(int status){
        String result = "";
        switch (status){
            case 0 :
            result = "Đơn hàng đang được xử lý";
            break;
            case 1 :
                result = " Được tiếp nhận";
                break;
            case 2 :
                result = " Đơn hàng đã giao cho đơn vị vận chuyển ";
               break;
            case 3 :
                result = "Giao hàng thành công";
                break;
            case 4 :
                result = "Đơn hàng đã bị huỷ";
                break;
            default:result = "....";


        }




        return result;

    }




}
