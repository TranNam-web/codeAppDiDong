package com.manager.doandidong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.manager.doandidong.R;
import com.manager.doandidong.model.GioHang;
import com.manager.doandidong.retrofit.ApiBanHang;
import com.manager.doandidong.retrofit.RetrofitClient;
import com.manager.doandidong.utils.Utils;
import com.google.gson.Gson;

import java.text.DecimalFormat;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThanhToanActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txttongtien, txtsodt, txtemail;
    EditText edtdiachi;
    AppCompatButton btndathang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    long tongtien;
    int totalItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
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
                int trangthai = 0; // 0 = thanh toán khi nhận hàng
                String momo = "cod"; // placeholder cho đơn hàng COD

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
                                userModel -> {
                                    Toast.makeText(getApplicationContext(), "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();

                                    // Xóa sản phẩm đã mua khỏi giỏ hàng
                                    for (int i = 0; i < Utils.mangmuahang.size(); i++) {
                                        GioHang gioHang = Utils.mangmuahang.get(i);
                                        if (Utils.manggiohang.contains(gioHang)) {
                                            Utils.manggiohang.remove(gioHang);
                                        }
                                    }

                                    Utils.mangmuahang.clear();
                                    Paper.book().write("giohang", Utils.manggiohang);

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
        });

    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        toolbar = findViewById(R.id.toobar);
        txttongtien = findViewById(R.id.txttongtien);
        txtemail = findViewById(R.id.txtemail);
        txtsodt = findViewById(R.id.txtsodienthoai);
        edtdiachi = findViewById(R.id.edtdiachi);
        btndathang = findViewById(R.id.btndathang);

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}