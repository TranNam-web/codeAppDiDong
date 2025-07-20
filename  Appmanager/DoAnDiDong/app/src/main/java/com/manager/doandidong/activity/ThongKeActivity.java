package com.manager.doandidong.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.manager.doandidong.R;
import com.manager.doandidong.retrofit.ApiBanHang;
import com.manager.doandidong.retrofit.RetrofitClient;
import com.manager.doandidong.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThongKeActivity extends AppCompatActivity {
    Toolbar toolbar;
    PieChart pieChart;
    BarChart barChart;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);

        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);

        innitView();
        ActionToolBar();
        settingBarchart();

        // Gọi mặc định hiển thị biểu đồ Pie
        getdataChart();
    }

    private void innitView() {
        toolbar = findViewById(R.id.toobar);
        pieChart = findViewById(R.id.piechart);
        barChart = findViewById(R.id.barchart);
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void settingBarchart() {
        barChart.getDescription().setEnabled(false);
        barChart.setDrawBarShadow(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setAxisMinimum(1f);
        xAxis.setAxisMaximum(12f);
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis yAxisRight = barChart.getAxisRight();
        yAxisRight.setAxisMinimum(0f);

        YAxis yAxisLeft = barChart.getAxisLeft();
        yAxisLeft.setAxisMinimum(0f);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_thongke, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.tkthang) {
            getTkThang();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getTkThang() {
        barChart.setVisibility(View.VISIBLE);
        pieChart.setVisibility(View.GONE);

        compositeDisposable.add(apiBanHang.getthongkeThang()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        thongKeModel -> {
                            if (thongKeModel.isSuccess()) {
                                List<BarEntry> listdata = new ArrayList<>();

                                for (int i = 0; i < thongKeModel.getResult().size(); i++) {
                                    String tongtien = thongKeModel.getResult().get(i).getTongtienthang();
                                    String thang = thongKeModel.getResult().get(i).getThang();

                                    Log.d("DEBUG", "Tháng: " + thang + " - Tổng tiền: " + tongtien);

                                    try {
                                        int monthInt = Integer.parseInt(thang);
                                        float amount = Float.parseFloat(tongtien);
                                        listdata.add(new BarEntry(monthInt, amount));
                                    } catch (NumberFormatException e) {
                                        Log.e("DEBUG", "Parse error: " + e.getMessage());
                                    }
                                }

                                if (listdata.isEmpty()) {
                                    Log.d("DEBUG", "Danh sách dữ liệu trống");
                                    return;
                                }

                                BarDataSet barDataSet = new BarDataSet(listdata, "Thống kê tháng");
                                barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                                barDataSet.setValueTextSize(14f);
                                barDataSet.setValueTextColor(Color.RED);

                                BarData data = new BarData(barDataSet);
                                barChart.setData(data);

                                barChart.animateXY(2000, 2000);
                                barChart.invalidate();

                            } else {
                                Log.d("DEBUG", "API trả về không thành công");
                            }
                        },
                        throwable -> {
                            Log.e("DEBUG", "Lỗi khi lấy dữ liệu: " + throwable.getMessage());
                        }
                ));
    }

    private void getdataChart() {
        pieChart.setVisibility(View.VISIBLE);
        barChart.setVisibility(View.GONE);

        List<PieEntry> listdata = new ArrayList<>();

        compositeDisposable.add(apiBanHang.getthongke()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        thongKeModel -> {
                            if (thongKeModel.isSuccess()) {
                                for (int i = 0; i < thongKeModel.getResult().size(); i++) {
                                    String tensp = thongKeModel.getResult().get(i).getTensp();
                                    int tong = thongKeModel.getResult().get(i).getTong();
                                    listdata.add(new PieEntry(tong, tensp));
                                }

                                PieDataSet pieDataSet = new PieDataSet(listdata, "Thống kê");
                                pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                                pieDataSet.setValueTextSize(12f);

                                PieData data = new PieData(pieDataSet);
                                data.setValueFormatter(new PercentFormatter(pieChart));

                                pieChart.setData(data);
                                pieChart.setUsePercentValues(true);
                                pieChart.getDescription().setEnabled(false);

                                pieChart.animateXY(2000, 2000);
                                pieChart.invalidate();

                            } else {
                                Log.d("DEBUG", "API trả về không thành công");
                            }
                        },
                        throwable -> {
                            Log.e("DEBUG", "Lỗi khi lấy dữ liệu: " + throwable.getMessage());
                        }
                ));
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
