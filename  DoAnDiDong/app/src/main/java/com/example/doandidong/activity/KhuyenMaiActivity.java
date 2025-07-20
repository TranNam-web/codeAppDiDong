package com.example.doandidong.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.doandidong.R;

public class KhuyenMaiActivity extends AppCompatActivity {
    TextView noidung;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khuyen_mai);
        initView();
        initData();


    }

    private void initData() {
        String nd = getIntent().getStringExtra("noidung");
        String url = getIntent().getStringExtra("url");
        noidung.setText(nd);
        Glide.with(this).load(url).into(imageView);

    }

    private void initView() {
        noidung = findViewById(R.id.km_noidung);
        imageView = findViewById(R.id.km_image);

    }
}