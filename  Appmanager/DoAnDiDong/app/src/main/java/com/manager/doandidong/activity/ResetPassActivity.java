package com.manager.doandidong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.manager.doandidong.R;
import com.manager.doandidong.retrofit.ApiBanHang;
import com.manager.doandidong.retrofit.RetrofitClient;
import com.manager.doandidong.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ResetPassActivity extends AppCompatActivity {
EditText email;
AppCompatButton btnreset;
ApiBanHang apiBanHang;
CompositeDisposable compositeDisposable = new CompositeDisposable();
ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        initView();
        initControl();

    }

    private void initControl() {
        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_email = email.getText().toString().trim();
                if (TextUtils.isEmpty(str_email)){
                    Toast.makeText(getApplicationContext(),"Bạn chưa nhập email",Toast.LENGTH_SHORT).show();

                }else {
                    progressBar.setVisibility(View.VISIBLE);

                    compositeDisposable.add(apiBanHang .ResetPass(str_email)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    userModel -> {
                                        if (userModel.isSuccess()){
                                            Toast.makeText(getApplicationContext(),userModel.getMessage(),Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(),DangNhapActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }else {
                                            Toast.makeText(getApplicationContext(),userModel.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                        progressBar.setVisibility(View.VISIBLE);

                                    },
                                    throwable -> {
                                        Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.VISIBLE);
                                    }

                            ));

                }
            }
        });
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        email = findViewById(R.id.edtresetpass);
        btnreset = findViewById(R.id.btnresetpass);
        progressBar = findViewById(R.id.progreebar);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}