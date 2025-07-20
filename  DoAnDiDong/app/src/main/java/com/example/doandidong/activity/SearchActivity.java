package com.example.doandidong.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doandidong.R;
import com.example.doandidong.adapter.DienThoaiAdapter;
import com.example.doandidong.model.SanPhamMoi;
import com.example.doandidong.retrofit.ApiBanHang;
import com.example.doandidong.retrofit.RetrofitClient;
import com.example.doandidong.utils.Utils;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    EditText edtsearch;
    DienThoaiAdapter adapterDt;
    List<SanPhamMoi> sanPhamMoiList;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        ActionToolBar();


    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);

        edtsearch = findViewById(R.id.edtsearch);
        toolbar = findViewById(R.id.toobar);
        recyclerView = findViewById(R.id.recycleview_rearch);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        edtsearch . addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getDataSearch();

            }
        });




    }

    private void getDataSearch() {
        String str_search = edtsearch.getText().toString().trim();
        compositeDisposable.add(apiBanHang.search(str_search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if (sanPhamMoiModel.isSuccess()){
                                adapterDt = new DienThoaiAdapter(sanPhamMoiModel.getResult(), getApplication());
                                recyclerView.setAdapter(adapterDt);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
                        }

                ));
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}