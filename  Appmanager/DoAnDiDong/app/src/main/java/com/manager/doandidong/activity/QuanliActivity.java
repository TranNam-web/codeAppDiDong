package com.manager.doandidong.activity;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.media.metrics.Event;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.manager.doandidong.R;
import com.manager.doandidong.adapter.SanPhamMoiAdapter;
import com.manager.doandidong.model.EventBus.SuaXoaEvent;
import com.manager.doandidong.model.SanPhamMoi;
import com.manager.doandidong.retrofit.ApiBanHang;
import com.manager.doandidong.retrofit.RetrofitClient;
import com.manager.doandidong.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import soup.neumorphism.NeumorphCardView;

public class QuanliActivity extends AppCompatActivity {
    ImageView img_them;
    RecyclerView recyclerView;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    List<SanPhamMoi> list;
    SanPhamMoiAdapter adapter;
    SanPhamMoi sanPhamSuaxoa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quanli);
         apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);

        initView();
        initControl();
        getSpMoi();

    }

    private void initControl() {
        img_them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ThemSPActivity.class);
                startActivity(intent);
            }
        });
    }


    private void getSpMoi() {
        compositeDisposable.add(apiBanHang.getSpMoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(

                        sanPhamMoiModel -> {
                            if (sanPhamMoiModel.isSuccess()) {
                                list = sanPhamMoiModel.getResult();
                                adapter = new SanPhamMoiAdapter(getApplicationContext(),list);
                                recyclerView.setAdapter(adapter);






                            }

                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),"Không kết nối được với sever"+throwable.getMessage(),Toast.LENGTH_LONG).show();

                        }
                ));

    }

    private void initView() {
        img_them = findViewById(R.id.img_them);
        recyclerView =  findViewById(R.id.recycleview_ql);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals("Sửa")){
            suaSanPham();
        }else if (item.getTitle().equals("Xoá")){
            xoaSanPham();
        }
        return super.onContextItemSelected(item);
    }

    private void xoaSanPham() {
        compositeDisposable .add(apiBanHang.xoaSanPham(sanPhamSuaxoa.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messageModel -> {
                            if (messageModel.isSuccess()){
                                Toast.makeText(getApplicationContext(),messageModel.getMessage(),Toast.LENGTH_LONG).show();
                                getSpMoi();

                            }else {
                                Toast.makeText(getApplicationContext(),messageModel.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        },
                        throwable -> {
                            Log.d("long",throwable.getMessage());
                        }


                ));

    }

    private void suaSanPham() {
        Intent intent = new Intent(getApplicationContext(),ThemSPActivity.class);
        intent.putExtra("sua",sanPhamSuaxoa);

        startActivity(intent);

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void evenSuaXoa(SuaXoaEvent event){
        if (event !=null){
            sanPhamSuaxoa = event.getSanPhamMoi();

        }
    }

    @Override
    protected void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}