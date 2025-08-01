package com.manager.doandidong.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.manager.doandidong.Interface.llmageClickListenner;
import com.manager.doandidong.R;
import com.manager.doandidong.model.EventBus.TinhTongEvent;
import com.manager.doandidong.model.GioHang;
import com.manager.doandidong.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

import io.paperdb.Paper;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.MyViewHolder> {
    Context context;
    List<GioHang> gioHangList;
    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

    public GioHangAdapter(Context context, List<GioHang> gioHangList) {
        this.context = context;
        this.gioHangList = gioHangList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giohang, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GioHang gioHang = gioHangList.get(position);
        holder.item_giohang_tensp.setText(gioHang.getTensp());
        holder.item_giohang_soluong.setText(gioHang.getSoluong() + " ");
        Glide.with(context).load(gioHang.getHinhsp()).into(holder.item_giohang_image);

        holder.item_giohang_gia.setText(decimalFormat.format(gioHang.getGiasp()));
        long gia = gioHang.getSoluong() * gioHang.getGiasp();
        holder.item_giohang_giasp2.setText(decimalFormat.format(gia));

        // Checkbox
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(gioHang.isChecked());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                gioHang.setChecked(b);
                if (b) {
                    if (!Utils.mangmuahang.contains(gioHang)) {
                        Utils.mangmuahang.add(gioHang);
                    }
                } else {
                    for (int i = 0; i < Utils.mangmuahang.size(); i++) {
                        if (Utils.mangmuahang.get(i).getIdsp() == gioHang.getIdsp()) {

                            Utils.mangmuahang.remove(i);
                            break;
                        }
                    }
                }
                // Gửi event cập nhật tổng tiền
                EventBus.getDefault().postSticky(new TinhTongEvent());
            }
        });

        holder.setListenner(new llmageClickListenner() {
            @Override
            public void onImageClick(View view, int pos, int giatri) {
                if (giatri == 1) { // Trừ
                    if (gioHangList.get(pos).getSoluong() > 1) {
                        int soluongmoi = gioHangList.get(pos).getSoluong() - 1;
                        gioHangList.get(pos).setSoluong(soluongmoi);
                        holder.item_giohang_soluong.setText(soluongmoi + " ");
                        long gia = soluongmoi * gioHangList.get(pos).getGiasp();
                        holder.item_giohang_giasp2.setText(decimalFormat.format(gia));

                        // Cập nhật tổng tiền
                        EventBus.getDefault().postSticky(new TinhTongEvent());

                    } else {
                        // Hiển thị AlertDialog xoá
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setTitle("Thông báo");
                        builder.setMessage("Bạn có muốn xoá sản phẩm này khỏi giỏ hàng không");
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Utils.mangmuahang.remove(gioHang);
                                Utils.manggiohang.remove(pos);
                                Paper.book().write("giohang",Utils.manggiohang);
                                notifyDataSetChanged();
                                EventBus.getDefault().postSticky(new TinhTongEvent());
                            }
                        });
                        builder.setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }

                } else if (giatri == 2) { // Cộng
                    if (gioHangList.get(pos).getSoluong() < 10) { // Giới hạn tối đa 10
                        int soluongmoi = gioHangList.get(pos).getSoluong() + 1;
                        gioHangList.get(pos).setSoluong(soluongmoi);
                        holder.item_giohang_soluong.setText(soluongmoi + " ");
                        long gia = soluongmoi * gioHangList.get(pos).getGiasp();
                        holder.item_giohang_giasp2.setText(decimalFormat.format(gia));

                        // Cập nhật tổng tiền
                        EventBus.getDefault().postSticky(new TinhTongEvent());
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return gioHangList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView item_giohang_image, imgtru, imgcong;
        TextView item_giohang_tensp, item_giohang_gia, item_giohang_soluong, item_giohang_giasp2;
        llmageClickListenner listenner;
        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_giohang_image = itemView.findViewById(R.id.item_giohang_image);
            item_giohang_tensp = itemView.findViewById(R.id.item_giohang_tensp);
            item_giohang_gia = itemView.findViewById(R.id.item_giohang_gia);
            item_giohang_soluong = itemView.findViewById(R.id.item_giohang_soluong);
            item_giohang_giasp2 = itemView.findViewById(R.id.item_giohang_giasp2);
            imgtru = itemView.findViewById(R.id.item_giohang_tru);
            imgcong = itemView.findViewById(R.id.item_giohang_cong);
            checkBox = itemView.findViewById(R.id.item_giohang_check);

            imgcong.setOnClickListener(this);
            imgtru.setOnClickListener(this);
        }

        public void setListenner(llmageClickListenner listenner) {
            this.listenner = listenner;
        }

        @Override
        public void onClick(View view) {
            if (view == imgtru) {
                listenner.onImageClick(view, getAdapterPosition(), 1);
            } else if (view == imgcong) {
                listenner.onImageClick(view, getAdapterPosition(), 2);
            }
        }
    }
}
