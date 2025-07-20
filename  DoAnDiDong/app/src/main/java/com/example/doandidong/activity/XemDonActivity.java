package com.example.doandidong.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar; // ✅ Dùng AppCompat Toolbar
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doandidong.Interface.ItemClickDeleteListener;
import com.example.doandidong.R;
import com.example.doandidong.adapter.DonHangAdapter;
import com.example.doandidong.retrofit.ApiBanHang;
import com.example.doandidong.retrofit.RetrofitClient;
import com.example.doandidong.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class XemDonActivity extends AppCompatActivity {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    RecyclerView redonhang;
    Toolbar toolbar;  // ✅ Đây là Toolbar chuẩn AppCompat

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_don);
        initView();
        initToolbar();
        getOrder();
    }

    private void getOrder() {
        compositeDisposable.add(apiBanHang.xemDonHang(Utils.user_current.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        donHangModel -> {
                            DonHangAdapter adapter = new DonHangAdapter(getApplicationContext(), donHangModel.getResult(), new ItemClickDeleteListener() {
                                @Override
                                public void onClickDelete(int iddonhang) {
                                    showDeleteOrder(iddonhang);
                                }
                            });
                            redonhang.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        },
                        throwable -> {
                            // Thêm dòng log lỗi hoặc thông báo nếu muốn
                        }
                ));
    }

    private void showDeleteOrder(int iddonhang) {
        PopupMenu popupMenu = new PopupMenu(this,redonhang.findViewById(R.id.id_trangthaidon));
        popupMenu.inflate(R.menu.menu_delete);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.deleteOrder){
                    deleteOrder(iddonhang);
                }

                return false;
            }
        });
        popupMenu.show();
    }

    private void deleteOrder(int iddonhang) {
        compositeDisposable.add(apiBanHang.deleteorder(iddonhang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messageModel -> {

                            if (messageModel.isSuccess()){
                                getOrder();
                            }


                        },
                        throwable -> {

                            Log.d("loggg",throwable.getMessage());

                        }

                ));


    }

    private void initToolbar() {
        setSupportActionBar(toolbar);  // ✅ Không còn lỗi
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    private void initView() {
        redonhang = findViewById(R.id.recycleview_donhang);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        toolbar = findViewById(R.id.toobar);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        redonhang.setLayoutManager(layoutManager);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
