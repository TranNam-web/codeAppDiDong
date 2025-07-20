package com.example.doandidong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.example.doandidong.R;
import com.example.doandidong.model.GioHang;
import com.example.doandidong.model.Message;
import com.example.doandidong.model.MessageData;
import com.example.doandidong.model.Notification;
import com.example.doandidong.retrofit.ApiBanHang;
import com.example.doandidong.retrofit.ApiPushNofication;
import com.example.doandidong.retrofit.AuthorizationIntercetor;
import com.example.doandidong.retrofit.RetrofitClient;
import com.example.doandidong.retrofit.RetrofitClientNoti;
import com.example.doandidong.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import vn.momo.momo_partner.AppMoMoLib;

public class ThanhToanActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txttongtien, txtsodt, txtemail;
    EditText edtdiachi;
    AppCompatButton btndathang,btnmomo;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    long tongtien;
    int totalItem;
    int iddonhang;
    OkHttpClient client;
    private String amount = "10000";
    private String fee = "0";
    int environment = 0;//Thanh toán đơn hàng
    private String merchantName = "Thanh toán đơn hàng";//ten trong momo d.nghiep
    private String merchantCode = "TPBANK";//cai nay cung vay
    private String merchantNameLabel = "Trần Đình Nam ";
    private String description = "Mua hàng Online";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT); // AppMoMoLib.ENVIRONMENT.PRODUCTION

        initView();
        initControl();
        countItem();
    }

    private void countItem() {
        totalItem = 0;
        for (int i =0;i<Utils.mangmuahang.size();i++){
            totalItem = totalItem+ Utils.mangmuahang.get(i).getSoluong();
        }


    }
    //Get token through MoMo app
    private void requestPayment(String iddonhang) {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);


        Map<String, Object> eventValue = new HashMap<>();
        //client Required
        eventValue.put("merchantname", merchantName); //Tên đối tác. được đăng ký tại https://business.momo.vn. VD: Google, Apple, Tiki , CGV Cinemas
        eventValue.put("merchantcode", merchantCode); //Mã đối tác, được cung cấp bởi MoMo tại https://business.momo.vn
        eventValue.put("amount", amount); //Kiểu integer
        eventValue.put("orderId", iddonhang); //uniqueue id cho Bill order, giá trị duy nhất cho mỗi đơn hàng
        eventValue.put("orderLabel", iddonhang); //gán nhãn

        //client Optional - bill info
        eventValue.put("merchantnamelabel", "Dịch vụ");//gán nhãn
        eventValue.put("fee", "0"); //Kiểu integer
        eventValue.put("description", description); //mô tả đơn hàng - short description

        //client extra data
        eventValue.put("requestId",  merchantCode+"merchant_billId_"+System.currentTimeMillis());
        eventValue.put("partnerCode", merchantCode);
        //Example extra data
        JSONObject objExtraData = new JSONObject();
        try {
            objExtraData.put("site_code", "008");
            objExtraData.put("site_name", "CGV Cresent Mall");
            objExtraData.put("screen_code", 0);
            objExtraData.put("screen_name", "Special");
            objExtraData.put("movie_name", "Kẻ Trộm Mặt Trăng 3");
            objExtraData.put("movie_format", "2D");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        eventValue.put("extraData", objExtraData.toString());

        eventValue.put("extra", "");
        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue);


    }
    //Get token callback from MoMo app an submit to server side
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
            if(data != null) {
                if(data.getIntExtra("status", -1) == 0) {
                    //TOKEN IS AVAILABLE
                    Log.d("thanhcong",data.getStringExtra("message"));
                    String token = data.getStringExtra("data"); //Token response
                    compositeDisposable.add(apiBanHang.updateMomo((iddonhang),token)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(


                                    messageModel -> {
                                        if (messageModel.isSuccess()){
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    },
                                    throwable -> {
                                        Log.d("erro",throwable.getMessage());

                                    }


                            ));
                    String phoneNumber = data.getStringExtra("phonenumber");
                    String env = data.getStringExtra("env");
                    if(env == null){
                        env = "app";
                    }

                    if(token != null && !token.equals("")) {
                        // TODO: send phoneNumber & token to your server side to process payment with MoMo server
                        // IF Momo topup success, continue to process your order
                    } else {
                        Log.d("thanhcong","khongthanhcong");
                    }
                } else if(data.getIntExtra("status", -1) == 1) {
                    //TOKEN FAIL
                    String message = data.getStringExtra("message") != null?data.getStringExtra("message"):"Thất bại";
                    Log.d("thanhcong","khongthanhcong");
                } else if(data.getIntExtra("status", -1) == 2) {
                    //TOKEN FAIL
                    Log.d("thanhcong","khongthanhcong");
                } else {
                    //TOKEN FAIL
                    Log.d("thanhcong","khongthanhcong");
                }
            } else {
                Log.d("thanhcong","khongthanhcong");
            }
        } else {
            Log.d("thanhcong","khongthanhcong");
        }
    }

    private void initControl() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(view -> finish());

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongtien = getIntent().getLongExtra("tongtien", 0);
        txttongtien.setText(decimalFormat.format(tongtien) + " đ");

        if (Utils.user_current != null) {
            txtemail.setText(Utils.user_current.getEmail());
            txtsodt.setText(Utils.user_current.getMobile());
        }

        btndathang.setOnClickListener(view -> {
            String str_diachi = edtdiachi.getText().toString().trim();

            if (TextUtils.isEmpty(str_diachi)) {
                Toast.makeText(getApplicationContext(), "Bạn chưa nhập địa chỉ", Toast.LENGTH_SHORT).show();
            } else {
                String str_email = Utils.user_current.getEmail();
                String str_sdt = Utils.user_current.getMobile();
                int id = Utils.user_current.getId();
                int trangthai = 0; // mặc định 0 là chưa xử lý
                String momo = "";  // nếu có mã giao dịch Momo thì truyền vào


                Log.d("GioHang", new Gson().toJson(Utils.mangmuahang));

                compositeDisposable.add(apiBanHang.createOder(
                                str_email,
                                str_sdt,
                                String.valueOf(tongtien),
                                id,
                                str_diachi,
                                totalItem,
                                new Gson().toJson(Utils.mangmuahang),
                                trangthai,
                                momo)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                messageModel -> {
                                    if (messageModel.isSuccess()) {
                                        pushNotiToUser();
                                        Toast.makeText(getApplicationContext(), "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();

                                        for (int i = 0; i < Utils.mangmuahang.size(); i++) {
                                            GioHang gioHang = Utils.mangmuahang.get(i);
                                            if (Utils.manggiohang.contains(gioHang)) {
                                                Utils.manggiohang.remove(gioHang);
                                            }
                                        }

                                        Utils.mangmuahang.clear();
                                        Paper.book().write("giohang", Utils.mangmuahang);

                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                },
                                throwable -> {
                                    Toast.makeText(getApplicationContext(), "Lỗi: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                        ));
            }
        });


        btnmomo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_diachi = edtdiachi.getText().toString().trim();
                if (TextUtils.isEmpty(str_diachi)) {
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập địa chỉ", Toast.LENGTH_SHORT).show();
                } else {
                    String str_email = Utils.user_current.getEmail();
                    String str_sdt = Utils.user_current.getMobile();
                    int id = Utils.user_current.getId();
                    int trangthai = 1; // 1 = thanh toán qua Momo
                    String momo = "momo"; // hoặc mã giao dịch sau khi thanh toán thành công

                    Log.d("GioHang", new Gson().toJson(Utils.mangmuahang));

                    compositeDisposable.add(apiBanHang.createOder(
                                    str_email,
                                    str_sdt,
                                    String.valueOf(tongtien),
                                    id,
                                    str_diachi,
                                    totalItem,
                                    new Gson().toJson(Utils.mangmuahang),
                                    trangthai,
                                    momo)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(

                                    messageModel -> {
                                        pushNotiToUser();
                                        Toast.makeText(getApplicationContext(), "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();

                                        iddonhang = Integer.parseInt(messageModel.getIddonhang());
                                        requestPayment(messageModel.getIddonhang());

                                        for (int i = 0; i < Utils.mangmuahang.size(); i++) {
                                            GioHang gioHang = Utils.mangmuahang.get(i);
                                            if (Utils.manggiohang.contains(gioHang)) {
                                                Utils.manggiohang.remove(gioHang);
                                            }
                                        }

                                        Utils.mangmuahang.clear();
                                        Paper.book().write("giohang", Utils.manggiohang);

                                        // Quay về MainActivity
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    },
                                    throwable -> {
                                        Toast.makeText(getApplicationContext(), "Lỗi: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                            ));
                }
            }
        });

    }

    private void pushNotiToUser() {

        if (Utils.tokenSend !=null){
             client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthorizationIntercetor(Utils.tokenSend))
                    .build();
        }
        //gettoken
        compositeDisposable.add((Disposable) apiBanHang.gettoken(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
              if (userModel.isSuccess()){
                for(int i =0; i<userModel.getResult().size(); i++){
                    Notification notification = new Notification("Thông báo","Bạn có một đơn hàng mới ");
                    Message message = new Message(notification, userModel.getResult().get(i).getToken());
                    MessageData messageData = new MessageData(message);
                    ApiPushNofication apiPushNofication = RetrofitClientNoti.getInstance(client).create(ApiPushNofication.class);
                    compositeDisposable.add(apiPushNofication.sendNofitication(messageData)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    notiResponse -> {

                                    },
                                    throwable -> {
                                        Log.d("logg",throwable.getMessage());
                                    }
                            ));

                }

              }
                        },
                        throwable -> {
                            Log.d("loggg",throwable.getMessage());
                        }
                ));


    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        toolbar = findViewById(R.id.toobar);
        txttongtien = findViewById(R.id.txttongtien);
        txtemail = findViewById(R.id.txtemail);
        txtsodt = findViewById(R.id.txtsodienthoai);
        edtdiachi = findViewById(R.id.edtdiachi);
        btndathang = findViewById(R.id.btndathang);
        btnmomo=findViewById(R.id.btnmomo);


    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}