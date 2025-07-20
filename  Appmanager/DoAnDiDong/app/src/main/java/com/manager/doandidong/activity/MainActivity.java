package com.manager.doandidong.activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.manager.doandidong.R;
import com.manager.doandidong.adapter.LoaiSpAdapter;
import com.manager.doandidong.adapter.SanPhamMoiAdapter;
import com.manager.doandidong.model.LoaiSp;
import com.manager.doandidong.model.SanPhamMoi;
import com.manager.doandidong.model.User;
import com.manager.doandidong.retrofit.ApiBanHang;
import com.manager.doandidong.retrofit.RetrofitClient;
import com.manager.doandidong.utils.Utils;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;
import java.util.ArrayList;
import java.util.List;
import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewManHinhChinh;
    NavigationView navigationView;
    ListView listViewManHinhChinh;
    DrawerLayout drawerLayout;
    LoaiSpAdapter loaiSpAdapter;
    List<LoaiSp> mangloaisp;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    List<SanPhamMoi> mangSpMoi;
    SanPhamMoiAdapter spAdapter;
    NotificationBadge badge;
    FrameLayout frameLayout;
    ImageView imgsearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        Paper.init(this);
        Anhxa();
        ActionBar();

        if (Paper.book().read("user") !=null){
            User user = Paper.book().read("user");
            Utils.user_current = user;
        }
        getToken();
        if(isConnected(this)){
            ActionViewFlipper();
            getLoaiSanPham();
            getSpMoi();
            getEventclick();

        }else {
            Toast.makeText(getApplicationContext(),"không có internet,vui lòng kết nối " ,Toast.LENGTH_LONG).show();
        }
    }
    private void getToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnSuccessListener(token -> {
                    if (!TextUtils.isEmpty(token) && Utils.user_current != null) {
                        compositeDisposable.add(apiBanHang.updateToken(String.valueOf(Utils.user_current.getId()), token)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        messageModel -> Log.d("log", "Token updated: " + token),
                                        throwable -> Log.d("log", throwable.getMessage())
                                ));
                    } else {
                        Log.d("log", "Token null or user not logged in");
                    }
                });



}
    private void getEventclick() {
        listViewManHinhChinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                switch (i){
                    case 0:
                        Intent trangchu = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(trangchu);
                        break;
                    case 1:
                        Intent dienthoai = new Intent(getApplicationContext(),DienThoaiActivity.class);
                        dienthoai.putExtra("loai",1);
                        startActivity(dienthoai);
                        break;
                    case 2:
                        Intent laptop = new Intent(getApplicationContext(),DienThoaiActivity.class);
                        laptop.putExtra("loai",2);
                        startActivity(laptop);
                        break;
                    case 5:
                        Intent donhang = new Intent(getApplicationContext(),XemDonActivity.class);
                        startActivity(donhang);
                        break;
                    case 6:
                        Intent quanli = new Intent(getApplicationContext(),QuanliActivity.class);
                        startActivity(quanli);
                        break;

                    case 7:
                        Intent chat = new Intent(getApplicationContext(),UserActivity.class);
                        startActivity(chat);
                        break;

                    case 8:
                        Intent tk = new Intent(getApplicationContext(),ThongKeActivity.class);
                        startActivity(tk);
                        break;

                    case 9:
                        //xoa key
                        Paper.book().delete("user");
                        FirebaseAuth.getInstance().signOut();
                        Intent dangnhap = new Intent(getApplicationContext(),DangNhapActivity.class);
                        startActivity(dangnhap);
                        finish();
                        break;
                }
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
                                mangSpMoi = sanPhamMoiModel.getResult();
                                spAdapter = new SanPhamMoiAdapter(getApplicationContext(),mangSpMoi);
                                recyclerViewManHinhChinh.setAdapter(spAdapter);

                            }

                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),"Không kết nối được với sever"+throwable.getMessage(),Toast.LENGTH_LONG).show();

                        }
                ));

    }
    private void getLoaiSanPham() {
        compositeDisposable.add(apiBanHang.getLoaiSp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        loaiSpModel-> {
                            if (loaiSpModel.isSuccess()){
                               mangloaisp = loaiSpModel.getResult();
                               mangloaisp.add(new LoaiSp("https://davicom.com.vn/wp-content/uploads/2023/12/lien-ket-nhieu-cong-cu-khac.jpg","Quản lí"));
                                mangloaisp.add(new LoaiSp("https://cdn-icons-png.flaticon.com/512/2899/2899298.png","ChatBox"));
                                mangloaisp.add(new LoaiSp("https://img.lovepik.com/element/45004/6149.png_860.png","Thống kê sản phẩm"));
                               mangloaisp.add(new LoaiSp("https://png.pngtree.com/png-vector/20231115/ourmid/pngtree-logout-icon-circle-png-image_10610262.png","Đăng xuất"));
                                loaiSpAdapter = new LoaiSpAdapter(getApplicationContext(), mangloaisp);
                                listViewManHinhChinh.setAdapter(loaiSpAdapter);

                            }

                }, throwable -> {
                    // Xử lý lỗi
                }));

    }
    private void ActionViewFlipper() {
        List<String> mangquangcao = new ArrayList<>();
        mangquangcao.add("https://kimlongcenter.com/upload/news/khuyen-mai-tet-2024.jpg");
        mangquangcao.add("https://didongthongminh.vn/upload_images/images/2022/01/15/1.png");
        mangquangcao.add("https://img.pikbest.com/origin/10/01/53/35bpIkbEsTBzN.png!w700wp");
        mangquangcao.add("https://didongthongminh.vn/images/news/resize/anh-bai-viet-km-tet-1.jpg?version=1636963297");
        mangquangcao.add("https://linhkienso.vn/wp-content/uploads/2023/03/banner-pcgame.jpg");
        mangquangcao.add("https://tintuc.dienthoaigiakho.vn/wp-content/uploads/2021/12/1200x628_FB-Ads.jpg");
        mangquangcao.add("https://hacom.vn/media/news/1904_Hacom1200x6753.jpg");
        mangquangcao.add("https://kenh14cdn.com/203336854389633024/2024/1/30/photo-2-1706620153192530710385.jpg");
        mangquangcao.add("https://thietkehaithanh.com/wp-content/uploads/2019/06/thietkehaithanh-banner-1-1.jpg");
        mangquangcao.add("https://pcchinhhang.com/wp-content/uploads/2024/03/banner-gaming.jpg");
        mangquangcao.add("https://anphat.com.vn/media/lib/29-04-2023/ctkmt5-1920x900.jpg");
        mangquangcao.add("https://kimlongcenter.com/upload/news/cung-cap-pc-laptop-gia-tot-cho-doanh-nghiep.jpg");
        mangquangcao.add("https://kimlongcenter.vn/upload/news/1200x625-tet2-1111111.jpg");
        for (int i = 0; i < mangquangcao.size(); i++) {
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(mangquangcao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);
    }
    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }
    private void Anhxa() {
        imgsearch =findViewById(R.id.imgsearch);
        toolbar = findViewById(R.id.toobarmanhinhchinh);
        viewFlipper = findViewById(R.id.viewlipper);
        recyclerViewManHinhChinh = findViewById(R.id.recycleview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerViewManHinhChinh.setLayoutManager(layoutManager);
        recyclerViewManHinhChinh.setHasFixedSize(true);
        listViewManHinhChinh = findViewById(R.id.lisviewmanhinhchinh);
        navigationView = findViewById(R.id.navigationview);
        drawerLayout = findViewById(R.id.drawerlayout);
        badge = findViewById(R.id.menu_sl);
        frameLayout = findViewById(R.id.framegiohang);
        // Khởi tạo LIST
        mangloaisp = new ArrayList<>();
        mangSpMoi = new ArrayList<>();
        if (Paper.book().read("giohang") !=null){
            Utils.manggiohang = Paper.book().read("giohang");
        }

        if (Utils.manggiohang == null){
            Utils.manggiohang = new ArrayList<>();
        }
        else {
                int totalItem = 0;
                for (int i =0;i<Utils.manggiohang.size();i++){
                    totalItem = totalItem+ Utils.manggiohang.get(i).getSoluong();
                }

                badge .setText(String.valueOf(totalItem));

            }
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent giohang = new Intent(getApplicationContext(),GiohangActivity.class);
                startActivity(giohang);
            }
        });

        imgsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
Intent intent = new Intent(getApplicationContext(),SearchActivity.class);
startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int totalItem = 0;
        for (int i =0;i<Utils.manggiohang.size();i++){
            totalItem = totalItem+ Utils.manggiohang.get(i).getSoluong();
        }

        badge .setText(String.valueOf(totalItem));

    }
    private boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected())) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}