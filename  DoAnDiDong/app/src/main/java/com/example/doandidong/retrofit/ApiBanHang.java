package com.example.doandidong.retrofit;

import io.reactivex.rxjava3.core.Observable;

import com.example.doandidong.model.DonHangModel;
import com.example.doandidong.model.KhuyenMaiModel;
import com.example.doandidong.model.LoaiSpModel;
import com.example.doandidong.model.MessageModel;
import com.example.doandidong.model.SanPhamMoiModel;
import com.example.doandidong.model.UserModel;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiBanHang {
    // GET DATA
    @GET("getloaisp.php")
    Observable<LoaiSpModel> getLoaiSp();

    @GET("khuyenmai.php")
    Observable<KhuyenMaiModel> getKhuyenMai();

    @GET("getspmoi.php")
    Observable <SanPhamMoiModel> getSpMoi();
    //POST DATA
    @POST("chitiet.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel> getSanPham(
            @Field("page") int page,
            @Field("loai") int  loai
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

    @POST("updatemomo.php")
    @FormUrlEncoded
    Observable<MessageModel> updateMomo(
            @Field("id") int id,
            @Field("token") String token

    );

    @POST("updatetoken.php")
    @FormUrlEncoded
    Observable<MessageModel> updateToken(
            @Field("id") String id,
            @Field("token") String token

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

    @POST("gettoken.php")
    @FormUrlEncoded
    Observable<UserModel> gettoken(
            @Field("status") int status
    );



    @POST("deleteOrder.php")
    @FormUrlEncoded
    Observable<MessageModel> deleteorder(
            @Field("iddonhang") int id
    );


}
