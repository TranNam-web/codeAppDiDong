package com.example.doandidong.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doandidong.R;
import com.example.doandidong.adapter.DienThoaiAdapter;
import com.example.doandidong.model.SanPhamMoi;
import com.example.doandidong.retrofit.ApiBanHang;
import com.example.doandidong.retrofit.RetrofitClient;
import com.example.doandidong.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DienThoaiActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    int page = 1;
    int loai;
    DienThoaiAdapter adapterDt;
    List<SanPhamMoi> sanPhamMoiList;
    LinearLayoutManager linearLayoutManager;
    Handler handler = new Handler();
    boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dien_thoai);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        loai = getIntent().getIntExtra("loai", 1);
        Anhxa();
        ActionToolBar();
        getData(page);
        addEventLoad();
    }

    private void addEventLoad() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // Kiểm tra nếu item cuối cùng đã hiển thị
                if (!isLoading && linearLayoutManager.findLastCompletelyVisibleItemPosition() == sanPhamMoiList.size() - 1) {
                    isLoading = true;
                    loadMore();
                }
            }
        });
    }

    private void loadMore() {
        // Thêm item null để hiển thị progress bar (nếu Adapter hỗ trợ)
        handler.post(() -> {
            sanPhamMoiList.add(null);
            adapterDt.notifyItemInserted(sanPhamMoiList.size() - 1);
        });
        handler.postDelayed(() -> {
            // Loại bỏ item null (progress bar)
            int removeIndex = sanPhamMoiList.size() - 1;
            sanPhamMoiList.remove(removeIndex);
            adapterDt.notifyItemRemoved(removeIndex);

            page = page + 1;
            getData(page);
            adapterDt.notifyDataSetChanged();
            isLoading = false;
        }, 2000);
    }

    private void getData(int page) {
        compositeDisposable.add(apiBanHang.getSanPham(page, loai)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if (sanPhamMoiModel.isSuccess()) {
                                if (adapterDt == null) {
                                    sanPhamMoiList = sanPhamMoiModel.getResult();
                                    adapterDt = new DienThoaiAdapter(sanPhamMoiList, DienThoaiActivity.this);
                                    recyclerView.setAdapter(adapterDt);
                                } else {
                                    // Tính vị trí bắt đầu thêm mới dữ liệu
                                    int startPos = sanPhamMoiList.size();
                                    List<SanPhamMoi> newData = sanPhamMoiModel.getResult();
                                    sanPhamMoiList.addAll(newData);
                                    adapterDt.notifyItemRangeInserted(startPos, newData.size());
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Hết dữ liệu rùi pé ơi", Toast.LENGTH_LONG).show();
                                isLoading = true; // Ngăn chặn load tiếp nếu không có dữ liệu
                            }
                        },
                        throwable -> {
                            Log.e("API_ERROR", "Không kết nối được với Sever: " + throwable.getMessage());
                            Toast.makeText(getApplicationContext(), "Không kết nối được với Sever", Toast.LENGTH_LONG).show();
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

    private void Anhxa() {
        toolbar = findViewById(R.id.toobar);
        recyclerView = findViewById(R.id.recycleview_dt);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        sanPhamMoiList = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
