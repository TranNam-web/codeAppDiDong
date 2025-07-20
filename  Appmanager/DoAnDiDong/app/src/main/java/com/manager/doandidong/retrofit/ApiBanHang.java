package com.manager.doandidong.retrofit;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

import com.manager.doandidong.model.DonHangModel;
import com.manager.doandidong.model.LoaiSpModel;
import com.manager.doandidong.model.MessageModel;
import com.manager.doandidong.model.SanPhamMoiModel;
import com.manager.doandidong.model.ThongKeModel;
import com.manager.doandidong.model.UserModel;

import java.io.File;

public interface ApiBanHang {

    // GET DATA
    @GET("getloaisp.php")
    Observable<LoaiSpModel> getLoaiSp();

    @GET("getspmoi.php")
    Observable<SanPhamMoiModel> getSpMoi();
    //thong ke sp
    @GET("thongke.php")
    Observable<ThongKeModel> getthongke();
    ///Thongke theo thang
    @GET("thongkethang.php")
    Observable<ThongKeModel> getthongkeThang();

    // POST DATA
    @POST("chitiet.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel> getSanPham(
            @Field("page") int page,
            @Field("loai") int loai
    );

    @POST("dangki.php")
    @FormUrlEncoded
    Observable<UserModel> dangki(
            @Field("email") String email,
            @Field("pass") String pass,
            @Field("username") String username,
            @Field("mobile") String mobile,
            @Field("uid") String uid
    );

    @POST("dangnhap.php")
    @FormUrlEncoded
    Observable<UserModel> dangNhap(
            @Field("email") String email,
            @Field("pass") String pass
    );

    @POST("reset.php")
    @FormUrlEncoded
    Observable<UserModel> ResetPass(
            @Field("email") String email
    );

    @POST("donhang.php")
    @FormUrlEncoded
    Observable<MessageModel> createOder(
            @Field("email") String email,
            @Field("sdt") String sdt,
            @Field("tongtien") String tongtien,
            @Field("iduser") int id,
            @Field("diachi") String diachi,
            @Field("soluong") int soluong,
            @Field("chitiet") String chitiet,
            @Field("trangthai") int trangthai,
            @Field("momo") String momo


    );

    @POST("xemdonhang.php")
    @FormUrlEncoded
    Observable<DonHangModel> xemDonHang(
            @Field("iduser") int id
    );

    @POST("timkiem.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel> search(
            @Field("search") String search
    );
    @POST("xoa.php")
    @FormUrlEncoded
    Observable<MessageModel> xoaSanPham(
            @Field("id") int id
    );

    @POST("insertsp.php")
    @FormUrlEncoded
    Observable<MessageModel> insertSp(
            @Field("tensp") String tensp,
            @Field("gia") String gia,
            @Field("hinhanh") String hinhanh,
            @Field("mota") String mota,
            @Field("loai") int id,
            @Field("slsp") int sl,
            @Field("linkvideo") String linkvideo

    );


    @FormUrlEncoded
    @POST("updatesp.php")
    Observable<MessageModel> updatesp(
            @Field("tensp") String tensp,
            @Field("giasp") String giasp,
            @Field("hinhanh") String hinhanh,
            @Field("mota") String mota,
            @Field("loai") int loai,
            @Field("id") int id,
            @Field("linkvideo") String linkvideo,
            @Field("slsp") int slsp
    );




    @POST("updatetoken.php")
    @FormUrlEncoded
    Observable<MessageModel> updateToken(
            @Field("id") String id,
            @Field("token") String token

    );

    @POST("updateorder.php")
    @FormUrlEncoded
    Observable<MessageModel> updateOrder(
            @Field("id") int id,
            @Field("trangthai") int trangthai

    );
    @POST("gettoken.php")
    @FormUrlEncoded
    Observable<UserModel> gettoken(
            @Field("status") int status
    );




    // Upload file với Multipart
    @Multipart
    @POST("upload.php")
    Call<MessageModel> uploadFile(
            @Part MultipartBody.Part file,
            @Part("file") RequestBody name
    );

    // Hàm giúp tạo MultipartBody.Part từ File
    static MultipartBody.Part prepareFilePart(String partName, File file) {
        // Tạo RequestBody từ file
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        // Tạo MultipartBody.Part từ RequestBody
        return MultipartBody.Part.createFormData(partName, file.getName(), requestBody);
    }

    // Hàm giúp tạo RequestBody cho tên file
    static RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(MultipartBody.FORM, descriptionString);
    }
}
