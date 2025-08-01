package com.manager.doandidong.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar; // ✅ Dùng AppCompat Toolbar
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.manager.doandidong.R;
import com.manager.doandidong.adapter.DonHangAdapter;
import com.manager.doandidong.model.DonHang;
import com.manager.doandidong.model.EventBus.DonHangEvent;
import com.manager.doandidong.retrofit.ApiBanHang;
import com.manager.doandidong.retrofit.RetrofitClient;
import com.manager.doandidong.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class XemDonActivity extends AppCompatActivity {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    RecyclerView redonhang;
    Toolbar toolbar;
    DonHang donHang;
    int tinhtrang ;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_don);
        initView();
        initToolbar();
        getOrder();
    }

    private void getOrder() {
        compositeDisposable.add(apiBanHang.xemDonHang(0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        donHangModel -> {
                            DonHangAdapter adapter = new DonHangAdapter(getApplicationContext(), donHangModel.getResult());
                            redonhang.setAdapter(adapter);
                        },
                        throwable -> {
                            // Thêm dòng log lỗi hoặc thông báo nếu muốn
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


    private void showCustumDialog() {
        LayoutInflater inflater =getLayoutInflater();
        View view =inflater.inflate(R.layout.dialog_donhang,null);
        Spinner spinner = view.findViewById(R.id.spinner_dialog);
        AppCompatButton btndongy = view.findViewById(R.id.dongy_dialog);
        List<String>list = new ArrayList<>();
        list.add("Đơn hàng đang được xử lý");
        list.add("Đơn hàng đã được chấp nhận");
        list.add("Đơn hàng đã giao cho đơn vị vận chuyển");
        list.add("Giao hàng thành công");
        list.add("Đơn hàng đã huỷ");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,list);
        spinner.setAdapter(adapter);
        spinner.setSelection(donHang.getTrangthai());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                tinhtrang = i;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btndongy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capnhatDonHang();

            }
        });



        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
         dialog = builder.create();

        if (XemDonActivity.this != null && !isFinishing()) {
            dialog.show(); // hoặc addView()
        }



    }

    private void capnhatDonHang() {
        compositeDisposable.add(apiBanHang.updateOrder(donHang.getId(),tinhtrang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messageModel -> {
                            getOrder();
                            dialog.dismiss();
                        },
                        throwable -> {

                        }
                ));

    }



    @Subscribe(sticky = false,threadMode = ThreadMode.MAIN)
    public  void evenDonHang(DonHangEvent event){
        if (event != null){
            donHang = event.getDonHang();
            showCustumDialog();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }



    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
