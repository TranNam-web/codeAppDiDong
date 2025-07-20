package com.manager.doandidong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.manager.doandidong.R;
import com.manager.doandidong.retrofit.ApiBanHang;
import com.manager.doandidong.retrofit.RetrofitClient;
import com.manager.doandidong.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangKiActivity extends AppCompatActivity {
    EditText email, pass, repass, mobile, username;
    AppCompatButton button;
    ApiBanHang apiBanHang;
    FirebaseAuth firebaseAuth;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ki);
        initView();
        initControl();
    }

    private void initControl() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangki();
            }
        });
    }

    private void dangki() {
        String str_email = email.getText().toString().trim();
        String str_pass = pass.getText().toString().trim();
        String str_repass = repass.getText().toString().trim();
        String str_mobile = mobile.getText().toString().trim();
        String str_user = username.getText().toString().trim();

        if (TextUtils.isEmpty(str_email)) {
            showToast("Bạn chưa nhập email");
        } else if (TextUtils.isEmpty(str_pass)) {
            showToast("Bạn chưa nhập mật khẩu");
        } else if (TextUtils.isEmpty(str_repass)) {
            showToast("Bạn chưa nhập lại mật khẩu");
        } else if (!str_pass.equals(str_repass)) {
            showToast("Mật khẩu nhập lại không khớp");
        } else if (TextUtils.isEmpty(str_user)) {
            showToast("Bạn chưa nhập tên người dùng");
        } else if (TextUtils.isEmpty(str_mobile)) {
            showToast("Bạn chưa nhập số điện thoại");
        } else {
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.createUserWithEmailAndPassword(str_email, str_pass)
                    .addOnCompleteListener(DangKiActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                if (user != null) {
                                    // Đăng ký Firebase xong, giờ mới gọi server PHP
                                    postData(str_email, str_pass, str_user, str_mobile, user.getUid());
                                }
                            } else {
                                showToast("Đăng ký Firebase thất bại: " + task.getException().getMessage());
                            }
                        }
                    });
        }
    }

    private void postData(String str_email, String str_pass, String str_user, String str_mobile, String uid) {
        // 🔥 Kiểm tra apiBanHang null
        if (apiBanHang == null) {
            apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        }

        compositeDisposable.add(apiBanHang.dangki(str_email, str_pass, str_user, str_mobile, uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if (userModel.isSuccess()) {
                                Utils.user_current.setEmail(str_email);
                                Utils.user_current.setPass(str_pass);
                                showToast("Đăng ký thành công");
                                Intent intent = new Intent(getApplicationContext(), DangNhapActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                showToast(userModel.getMessage());
                            }
                        },
                        throwable -> {
                            showToast("Lỗi server: " + throwable.getMessage());
                        }
                ));
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        repass = findViewById(R.id.repass);
        mobile = findViewById(R.id.mobile);
        username = findViewById(R.id.username);
        button = findViewById(R.id.btndangki);
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
