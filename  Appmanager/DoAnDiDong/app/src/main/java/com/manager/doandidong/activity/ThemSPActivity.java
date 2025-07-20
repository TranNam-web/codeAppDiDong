package com.manager.doandidong.activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.manager.doandidong.R;
import com.manager.doandidong.databinding.ActivityThemspBinding;
import com.manager.doandidong.model.MessageModel;
import com.manager.doandidong.model.SanPhamMoi;
import com.manager.doandidong.retrofit.ApiBanHang;
import com.manager.doandidong.retrofit.RetrofitClient;
import com.manager.doandidong.utils.Utils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class ThemSPActivity extends AppCompatActivity {
    Spinner spinner;
    int loai = 0;
    ActivityThemspBinding binding;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    String mediaPath;
    SanPhamMoi sanPhamsSua;
    boolean flag =false;
    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        mediaPath = uri.toString();
                        uploadFile(uri);
                        Log.d("log", "Image picked: " + mediaPath);
                    }
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityThemspBinding.inflate(getLayoutInflater());
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        setContentView(binding.getRoot());
        initView();
        initData();
        Intent intent = getIntent();
        sanPhamsSua = (SanPhamMoi) intent.getSerializableExtra("sua");
        if (sanPhamsSua == null){
            //themmoi
            flag = false;
        }else {
            //sua
            flag = true;
            binding.btnthem.setText("Sửa sản phẩm");
            //show data
            binding.mota.setText(sanPhamsSua.getMota());
            binding.giasp.setText(sanPhamsSua.getGiasp()+"");
            binding.tensp.setText(sanPhamsSua.getTensp());
            binding.hinhanh.setText(sanPhamsSua.getHinhanh());
            binding.spinnerLoai.setSelection(sanPhamsSua.getLoai());
            binding.slsp.setText(sanPhamsSua.getSlsp()+"");
            binding.linkvideo.setText(sanPhamsSua.getLinkvideo());
        }
    }
    private void initView() {
        spinner = findViewById(R.id.spinner_loai);
    }
    private void initData() {
        List<String> stringList = new ArrayList<>();
        stringList.add("Vui lòng chọn loại");
        stringList.add("Loại 1");
        stringList.add("Loại 2");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, stringList);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                loai = i;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        binding.btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == false){
                    themsanpham();
                }else {

                    suaSanPhamn();
                }
            }
        });
        binding.imgcamera.setOnClickListener(v -> {
            ImagePicker.with(this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .createIntent(intent -> {
                        imagePickerLauncher.launch(intent);
                        return null;
                    });
        });
    }
    private void suaSanPhamn() {
        String str_ten = binding.tensp.getText().toString().trim();
        String str_gia = binding.giasp.getText().toString().trim();
        String str_mota = binding.mota.getText().toString().trim();
        String str_slsp = binding.slsp.getText().toString().trim();
        String str_linkvideo = binding.linkvideo.getText().toString().trim();
        String str_hinhanh = binding.hinhanh.getText().toString().trim();
        if (TextUtils.isEmpty(str_linkvideo) || TextUtils.isEmpty(str_slsp) ||TextUtils.isEmpty(str_ten) || TextUtils.isEmpty(str_gia) || TextUtils.isEmpty(str_mota)
                || TextUtils.isEmpty(str_hinhanh) || loai == 0) {
            Toast.makeText(getApplicationContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_LONG).show();
        } else {
            compositeDisposable.add(
                    apiBanHang.updatesp(
                                    str_ten,
                                    str_gia,
                                    str_hinhanh,
                                    str_mota,
                                    loai,
                                    sanPhamsSua.getId(),
                                    str_linkvideo,
                                    Integer.parseInt(str_slsp)
                            )
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    messageModel -> Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_LONG).show(),
                                    throwable -> Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show()
                            )
            );
        }
    }
    private void themsanpham() {
        String str_ten = binding.tensp.getText().toString().trim();
        String str_gia = binding.giasp.getText().toString().trim();
        String str_mota = binding.mota.getText().toString().trim();
        String str_hinhanh = binding.hinhanh.getText().toString().trim();
        String str_sl = binding.slsp.getText().toString();
        String str_linkvideo = binding.linkvideo.getText().toString().trim();

        if (TextUtils.isEmpty(str_linkvideo) ||  TextUtils.isEmpty(str_sl) || TextUtils.isEmpty(str_ten) || TextUtils.isEmpty(str_gia) || TextUtils.isEmpty(str_mota)
                || TextUtils.isEmpty(str_hinhanh) || loai == 0) {
            Toast.makeText(getApplicationContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_LONG).show();
        } else {
            compositeDisposable.add(apiBanHang.insertSp(str_ten, str_gia, str_hinhanh, str_mota, (loai),Integer.parseInt(str_sl),(str_linkvideo))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            messageModel -> Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_LONG).show(),
                            throwable -> Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show()
                    ));
        }
    }
    private void uploadFile(Uri uri) {
        String filePath = getPath(uri);
        File file = new File(filePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        Call<MessageModel> call = apiBanHang.uploadFile(fileToUpload, filename);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                MessageModel serverResponse = response.body();
                if (serverResponse != null) {
                    if (serverResponse.isSuccess()) {
                        binding.hinhanh.setText(serverResponse.getName());
                    } else {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.v("Response", "Empty response");
                }
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                Log.d("log", "Upload failed: " + t.getMessage());
            }
        });
    }
    private String getPath(Uri uri) {
        String result;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            result = uri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(index);
            cursor.close();
        }
        return result;
    }
    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
